package com.zerogreen.zerogreeneco.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import zerogreen.eco.entity.userentity.Member;
import zerogreen.eco.entity.userentity.UserRole;
import zerogreen.eco.entity.userentity.VegetarianGrade;

import java.util.Map;
import java.util.UUID;

@Getter
@Slf4j
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String username;
    private String socialType;
    private String mobile;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String nickname, String username, String socialType, String mobile) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.username = username;
        this.socialType = socialType;
        this.mobile = mobile;
    }

    // OAuth2User에서 반환되는 사용자 정보는 Map 형태. of()에서 값을 받으면 registrationId로 어떤 서비스인지 구분한 뒤 of~를 통해서 값을 변환
    public static OAuthAttributes of(String registrationId, String usernameAttributesName, Map<String, Object> attributes) {

        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes, registrationId);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes, registrationId);
        }

        return ofGoogle(usernameAttributesName, attributes, registrationId);
    }

    // of()에서 넘어온 값을 변환
    private static OAuthAttributes ofKakao(String usernameAttributesName, Map<String, Object> attributes, String registrationID) {

        Map<String, Object> nickname = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> username = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .username(String.valueOf(username.get("email")))
                .nickname(String.valueOf(nickname.get("nickname")))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributesName)
                .socialType(registrationID)
                .build();
    }

    private static OAuthAttributes ofNaver(String usernameAttributesName, Map<String, Object> attributes, String registrationID) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username(String.valueOf(response.get("email")))
                .nickname(String.valueOf(response.get("nickname")))
                .attributes(response)
                .nameAttributeKey(usernameAttributesName)
                .socialType(registrationID)
                .mobile(splitMobile(String.valueOf(response.get("mobile"))))
                .build();
    }

    private static OAuthAttributes ofGoogle(String usernameAttributesName, Map<String, Object> attributes, String registrationID) {
        return OAuthAttributes.builder()
                .nickname(String.valueOf(attributes.get("name")))
                .username(String.valueOf(attributes.get("email")))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributesName)
                .socialType(registrationID)
                .build();
    }

    // Member 엔티티를 생성(처음 가입할 때 한번만 생성)
    public Member toEntity() {
        return Member.OauthRegister()
                .username(username)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .userRole(UserRole.AUTH_USER)
                .phoneNumber(mobile)
                .authState(false)
                .nickname(nickname)
                .vegetarianGrade(VegetarianGrade.LACTO)
                .socialType(socialType)
                .oauthRegister();
    }

    private static String splitMobile(String mobile) {
        String[] mobileNum = mobile.split("-");
        String mobile1 = mobileNum[0];
        String mobile2 = mobileNum[1];
        String mobile3 = mobileNum[2];

        return mobile1 + mobile2 + mobile3;
    }
}
