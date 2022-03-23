package com.zerogreen.zerogreeneco.entity.userentity;

import com.zerogreen.zerogreeneco.entity.detail.ReviewImage;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@DiscriminatorValue("STORE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@OnDelete(action = OnDeleteAction.CASCADE)
public class StoreMember extends BasicUser{

    private String storeName;
    // 사업자 등록번호
    private String storeRegNum;

    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Embedded
    private StoreInfo storeInfo;

    @OneToMany(mappedBy = "storeMember", cascade = ALL)//영속성 전이, 부모 엔티티를 저장/삭제 자식 엔티티도 함께 저장/삭제
    private List<StoreMenu> menuList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = ALL)
    @JoinColumn(name = "reg_file_id")
    private RegisterFile registerFile;

    @OneToMany(mappedBy = "storeMember", cascade = ALL)
    private List<StoreImageFile> imageFile = new ArrayList<>();

    @OneToMany(mappedBy = "detailReview", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<ReviewImage> reviewImageList = new ArrayList<>();


    // 회원 가입 (saveV2까지)
    @Builder(builderMethodName = "regBuilder")
    public StoreMember(String username, String phoneNumber, String password, UserRole userRole,
                       String storeName, String storeRegNum, StoreType storeType, String postalCode, String storeAddress,
                       String storeDetailAddress, String storePhoneNumber, RegisterFile registerFile) {

        super(username, phoneNumber, password, userRole);
        this.storeName = storeName;
        this.storeRegNum = storeRegNum;
        this.storeType = storeType;
        this.storeInfo = new StoreInfo(postalCode, storeAddress, storeDetailAddress, storePhoneNumber);
        this.setRegisterFile(new RegisterFile(registerFile.getUploadFileName(), registerFile.getStoreFileName(), registerFile.getFilePath()));
    }

    //회원가입 테스트데이터용, 예진이
    @Builder(builderMethodName = "testBuilder")
    public StoreMember(String username, String phoneNumber, String password, UserRole userRole,
                       String storeName, String storeRegNum, StoreType storeType, String postalCode, String storeAddress,
                       String storeDetailAddress, String storePhoneNumber, String storeDescription, String socialAddress1,
                       String socialAddress2, String openTime, String closeTime, RegisterFile registerFile) {

        super(username, phoneNumber, password, userRole);
        this.storeName = storeName;
        this.storeRegNum = storeRegNum;
        this.storeType = storeType;
        this.storeInfo = new StoreInfo(postalCode, storeAddress, storeDetailAddress, storePhoneNumber, storeDescription,
                socialAddress1, socialAddress2, openTime, closeTime);
        this.setRegisterFile(new RegisterFile(registerFile.getUploadFileName(), registerFile.getStoreFileName(), registerFile.getFilePath()));
    }


    // DTO -> 엔티티 (테스트 saveV2에서 쓰는 생성자)
    public StoreMember(String username, String phoneNumber, String password, UserRole userRole, String storeName,
                       String storeRegNum, StoreType storeType, String storeAddress,
                       String storeDetailAddress, String storePhoneNumber, String postalCode) {
        super(username, phoneNumber, password, userRole);
        this.storeName = storeName;
        this.storeRegNum = storeRegNum;
        this.storeType = storeType;
        this.storeInfo = new StoreInfo(postalCode, storeAddress, storeDetailAddress, storePhoneNumber);
//        new StoreInfo(postalCode, storeAddress, storeDetailAddress, storePhoneNumber);
    }

    // Test 데이터 용 (삭제 예정)
    public StoreMember(String username, String phoneNumber, String password, UserRole userRole,
                       String storeRegNum, StoreType storeType) {

        super(username, phoneNumber, password, userRole);
        this.storeRegNum = storeRegNum;
        this.storeType = storeType;
    }

    // 가게 정보 등록
    public StoreMember(String storeAddress, String storePhoneNumber, String storeDescription,
                       String openTime, String closeTime) {
        storeInfo = new StoreInfo(storeAddress, storePhoneNumber, storeDescription, openTime, closeTime);
    }

}
