package com.zerogreen.zerogreeneco.dto.store;

import com.sun.istack.Nullable;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import com.zerogreen.zerogreeneco.entity.userentity.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Getter
@Setter
public class StoreDto {

    private Long id;
    private UserRole userRole;
    @NotBlank(message = "가게 전화번호를 입력해 주세요")
    private String storePhoneNumber;
    private String openTime;
    private String closeTime;
    private String storeDescription;
    private String socialAddress1, socialAddress2;

    private Long menuId;
    private String menuName;
    private String menuPrice;

    private Long sno;
    private String storeName;

    private StoreType storeType;
    private StoreInfo storeInfo;

    private Long likesCount;
    private Long like;

    private Long reviewCount;

    @Nullable
    private VegetarianGrade vegetarianGrades;

    private List<MultipartFile> uploadFiles;

    private Long imageId;
    private String fileName;
    private String storeFileName;
    private String thumbnailName;
    private String filePath;
    private String thumbPath;
    private StoreMember storeMember;


    public StoreDto(){}

    //image list
    public StoreDto(StoreImageFile storeImageFile) {
        this.imageId = storeImageFile.getId();
        this.fileName = storeImageFile.getFileName();
        this.storeFileName = storeImageFile.getStoreFileName();
        this.filePath = storeImageFile.getFilePath();
        this.storeMember = storeImageFile.getStoreMember();
        this.thumbnailName = storeImageFile.getThumbnailName();
        this.thumbPath = storeImageFile.getThumbPath();
    }

    //Store db (Detail)
    public StoreDto(Long sno, String storeName, StoreType storeType, StoreInfo storeInfo, Long likesCount, Long reviewCount) {
        this.sno = sno;
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeInfo = storeInfo;
        this.likesCount = likesCount;
        this.reviewCount = reviewCount;
    }

    //List DTO
    public StoreDto(Long id, String storeName, String storePhoneNumber,
                    String openTime, String closeTime, Long like, String menuName,  String thumbnailName) {
        this.id = id;
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.menuName = menuName;
        this.like = like;
        this.thumbnailName = thumbnailName;
    }

    //StoreUpdate
    public StoreDto(String storeName, StoreType storeType, String storePhoneNumber, String openTime, String closeTime,
                    String storeDescription, String socialAddress1, String socialAddress2){
        this.storeName = storeName;
        this.storeType = storeType;
        this.storePhoneNumber = storePhoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.storeDescription = storeDescription;
        this.socialAddress1 = socialAddress1;
        this.socialAddress2 = socialAddress2;
    }

/*
    public StoreDto(Long id, UserRole userRole, StoreType storeType, String storeName, String storePhoneNumber,
                    String openTime, String closeTime, List<StoreImageFile> imageFile, List<StoreMenu> menuList) {
        this.id = id;
        this.userRole = userRole;
        this.storeType = storeType;
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.imageFile = imageFile;
        this.menuList = menuList;
    }
*/

}

