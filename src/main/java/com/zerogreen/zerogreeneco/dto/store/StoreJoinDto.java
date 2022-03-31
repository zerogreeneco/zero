package com.zerogreen.zerogreeneco.dto.store;

import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreType;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class StoreJoinDto {
    @NotBlank
    @Email
    private String username;

    @NotBlank(message = "공백 X")
    private String phoneNumber;

    @NotBlank(message = "가게이름")
    private String storeName;

    @NotBlank
//    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    private String password;

    @NotBlank
    private String storeRegNum;

    @NotBlank
    private String storePhoneNumber;

    @NotBlank
    private String storeAddress;

    private String storeDetailAddress;

    @NotNull
    private String postalCode;

    private StoreType storeType;

    private MultipartFile attachFile;

    public StoreMember toStoreMember(StoreJoinDto storeDto) {

        return new StoreMember(storeDto.getUsername(), storeDto.getPhoneNumber(), storeDto.getPassword(),
                UserRole.UN_STORE, storeDto.getStoreName(), storeDto.getStoreRegNum(),
                storeDto.getStoreType(), storeDto.getStoreAddress(), storeDto.getStoreDetailAddress(), storeDto.getStorePhoneNumber(), storeDto.getPostalCode());
    }
}
