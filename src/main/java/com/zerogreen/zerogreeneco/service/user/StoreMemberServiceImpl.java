package com.zerogreen.zerogreeneco.service.user;

import com.zerogreen.zerogreeneco.dto.detail.DetailReviewDto;
import com.zerogreen.zerogreeneco.dto.search.SearchCondition;
import com.zerogreen.zerogreeneco.dto.store.NonApprovalStoreDto;
import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.repository.detail.DetailReviewRepository;
import com.zerogreen.zerogreeneco.repository.file.StoreImageFileRepository;
import com.zerogreen.zerogreeneco.repository.user.StoreMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMemberServiceImpl implements StoreMemberService {

    private final StoreMemberRepository storeMemberRepository;
    private final StoreImageFileRepository storeImageFileRepository;
    private final DetailReviewRepository detailReviewRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    /*
     * Store_member 회원가입(RegisterFile 포함)
     * */
    @Transactional
    @Override
    public Long save(StoreMember storeMember, RegisterFile registerFile) {

        String encPassword = passwordEncoder.encode(storeMember.getPassword());

        return storeMemberRepository.save(StoreMember.regBuilder()
                        .username(storeMember.getUsername())
                        .password(encPassword)
                        .phoneNumber(storeMember.getPhoneNumber())
                        .userRole(UserRole.UN_STORE)
                        .storeName(storeMember.getStoreName())
                        .storeRegNum(storeMember.getStoreRegNum())
                        .storeType(storeMember.getStoreType())
                        .storeAddress(storeMember.getStoreInfo().getStoreAddress())
                        .storeDetailAddress(storeMember.getStoreInfo().getStoreDetailAddress())
                        .postalCode(storeMember.getStoreInfo().getPostalCode())
                        .storePhoneNumber(storeMember.getStoreInfo().getStorePhoneNumber())
                        .registerFile(registerFile)
                        .build())
                .getId();
    }

    //상세페이지 DB
    @Override
    public StoreDto getStore(Long sno) {
        return storeMemberRepository.getStoreById(sno);
    }


    @Override
    public List<NonApprovalStoreDto> findByApprovalStore(UserRole userRole) {
        return storeMemberRepository.findByApprovalStore(userRole);
    }

    //Shop List 출력
    @Override
    public Slice<StoreDto> getShopList(Pageable pageable) {
        return storeMemberRepository.getShopList(pageable);
    }

    @Override
    public Slice<StoreDto> getShopSearchList(Pageable pageable, SearchCondition searchCondition) {
        return storeMemberRepository.getShopSearchList(pageable, searchCondition);
    }

    //Food List 출력
    @Override
    public Slice<StoreDto> getFoodList(Pageable pageable) {
        return storeMemberRepository.getFoodList(pageable);
    }

    @Override
    public Slice<StoreDto> getFoodTypeList(Pageable pageable, StoreType storeType) {
        return storeMemberRepository.getFoodTypeList(pageable, storeType);
    }

    @Override
    public Slice<StoreDto> getFoodSearchList(Pageable pageable, SearchCondition searchCondition) {
        return storeMemberRepository.getFoodSearchList(pageable, searchCondition);
    }

    //storeMyInfo 리뷰
    @Override
    public List<DetailReviewDto> getReviewByStore(Long id) {
        return detailReviewRepository.getReviewByStore(id);
    }

    //가게정보 수정
    @Override
    public StoreDto getStoreInfo(Long id, StoreDto storeDto) {
        StoreMember storeMember = storeMemberRepository.findById(id).orElseThrow();
        return new StoreDto(storeMember.getStoreName(), storeMember.getStoreType(),
                storeMember.getStoreInfo().getStorePhoneNumber(), storeMember.getStoreInfo().getOpenTime(),
                storeMember.getStoreInfo().getCloseTime(), storeMember.getStoreInfo().getStoreDescription(),
                storeMember.getStoreInfo().getSocialAddress1(), storeMember.getStoreInfo().getSocialAddress2());
    }

    @Transactional
    @Override
    public void updateStore(Long id, StoreDto storeDto, List<StoreImageFile> storeImageFile) {
        StoreMember storeMember = storeMemberRepository.findById(id).orElseThrow();
        //더티 체킹
        storeMember.getStoreInfo().setStorePhoneNumber(storeDto.getStorePhoneNumber());
        storeMember.getStoreInfo().setOpenTime(storeDto.getOpenTime());
        storeMember.getStoreInfo().setCloseTime(storeDto.getCloseTime());
        storeMember.getStoreInfo().setStoreDescription(storeDto.getStoreDescription());
        storeMember.getStoreInfo().setSocialAddress1(storeDto.getSocialAddress1());
        storeMember.getStoreInfo().setSocialAddress2(storeDto.getSocialAddress2());

        // 트랜젝션 전에 지연 SQL 저장소 -> DB로 전송
        storeMemberRepository.flush();

        //이미지 DB 저장
        if (storeImageFile.size() != 0) {
            for (StoreImageFile image : storeImageFile) {
                storeImageFileRepository.save(new StoreImageFile(image.getFileName(),
                        image.getStoreFileName(), image.getThumbnailName(), image.getFilePath(), image.getThumbPath(), storeMember));
            }
        }
    }

    @Override
    public int countByStoreRegNum(String storeRegNum) {
        return storeMemberRepository.countByStoreRegNum(storeRegNum);
    }


    //test data
    @Transactional
    @Override
    public Long saveV2(StoreMember storeMember, RegisterFile registerFile) {
        String encPassword = passwordEncoder.encode(storeMember.getPassword());
        return storeMemberRepository.save(new StoreMember(storeMember.getUsername(), storeMember.getPhoneNumber(),
                        encPassword, UserRole.STORE, storeMember.getStoreName(), storeMember.getStoreRegNum(), storeMember.getStoreType(), storeMember.getStoreInfo().getPostalCode(),
                        storeMember.getStoreInfo().getStoreAddress(), storeMember.getStoreInfo().getStoreDetailAddress(), storeMember.getStoreInfo().getStorePhoneNumber(), registerFile))
                .getId();
    }

    //test data V2,
    @Transactional
    @Override
    public Long saveV3(StoreMember storeMember) {
        String encPassword = passwordEncoder.encode(storeMember.getPassword());
        RegisterFile registerFile1 = new RegisterFile("testFile", "testFile", "testFile");
        return storeMemberRepository.save(new StoreMember(storeMember.getUsername(), storeMember.getPhoneNumber(),
                        encPassword, UserRole.STORE, storeMember.getStoreName(), storeMember.getStoreRegNum(), storeMember.getStoreType(), storeMember.getStoreInfo().getPostalCode(),
                        storeMember.getStoreInfo().getStoreAddress(), storeMember.getStoreInfo().getStoreDetailAddress(), storeMember.getStoreInfo().getStorePhoneNumber(),
                        storeMember.getStoreInfo().getStoreDescription(), storeMember.getStoreInfo().getSocialAddress1(), storeMember.getStoreInfo().getSocialAddress2(),
                        storeMember.getStoreInfo().getOpenTime(), storeMember.getStoreInfo().getCloseTime(), registerFile1))
                .getId();
    }


}