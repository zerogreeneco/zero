package com.zerogreen.zerogreeneco.dto.store;

import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NonApprovalStoreDto {

    private Long fileId;
    private Long memberId;
    private String username;
    private String storeName;
    private String storePhoneNumber;
    private String storeAddress;
    private String storeRegNum;
    private String uploadFileName;
    private StoreType storeType;

    public NonApprovalStoreDto(String username, String storeName, String storePhoneNumber) {
        this.username = username;
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
    }

    public NonApprovalStoreDto(Long memberId, String username, String storeName, String storePhoneNumber,
                               String storeAddress, String storeRegNum, Long fileId , String uploadFileName) {
        this.fileId = fileId;
        this.memberId = memberId;
        this.username = username;
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
        this.storeAddress = storeAddress;
        this.storeRegNum = storeRegNum;
        this.uploadFileName = uploadFileName;
    }

    public NonApprovalStoreDto(Long memberId, String storeName, String storeAddress, StoreType storeType) {
        this.memberId = memberId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeType = storeType;
    }
}
