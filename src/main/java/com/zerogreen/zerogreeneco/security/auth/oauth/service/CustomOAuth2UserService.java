package com.zerogreen.zerogreeneco.security.auth.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zerogreen.zerogreeneco.entity.userentity.Member;
import com.zerogreen.zerogreeneco.repository.user.MemberRepository;
import com.zerogreen.zerogreeneco.security.auth.PrincipalDetails;
import com.zerogreen.zerogreeneco.security.dto.OAuthAttributes;
import com.zerogreen.zerogreeneco.security.dto.SessionUser;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId : 현재 로그인 중인 서비스를 구분 (구글과 같이 시큐리티에서 기본으로 제공하는 경우에는 필요없지만 네이버, 카카오 등과 구분하기 위해서)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName : OAuth2 로그인 시 키가 되는 필드값. PK와 같은 의미
        // 구글은 'sub'이라는 기본 코드를 제공하지만, 네이버, 카카오 등은 제공 X
        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes : OAuth2UserService를 통해 가져온 OAuth2User와 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuthAttributes에서 가져온 username으로 DB에서 해당 회원을 찾아서
        // 소셜 서비스에서 가져온 정보로 새로 저장하거나 기존에 존재하는 회원이면 update
        Member auth2Member = saveOrUpdate(attributes, registrationId);

        // session에 사용자 정보를 저장하기 위한 DTO
        // 엔티티를 사용하지 않고 별도의 DTO를 사용하는 이유는 session에 저장할 때 직렬화를 구현하지 않은 경우 에러가 발생
        // 엔티티에 직렬화 코드를 넣으면 연관된 엔티티에도 영향을 주기 때문에 성능 이슈와 부수 효과가 발생 가능
        httpSession.setAttribute("member", new SessionUser(auth2Member));
        httpSession.setAttribute("veganGrade", auth2Member.getVegetarianGrade());

        return new PrincipalDetails(auth2Member, attributes.getAttributes());
    }

    @Transactional
    public Member saveOrUpdate(OAuthAttributes attributes, String socialType) {

        Member member = memberRepository.findByUsername(attributes.getUsername())
                .map(entity -> entity.update(attributes.getNickname(), socialType))
                .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}
