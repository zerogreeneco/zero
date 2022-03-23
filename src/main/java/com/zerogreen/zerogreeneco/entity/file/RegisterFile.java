package com.zerogreen.zerogreeneco.entity.file;

import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;
    private String filePath;

    @OneToOne(mappedBy = "registerFile")
    private StoreMember storeMember;

    public RegisterFile(String uploadFileName, String storeFileName,String filePath) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.filePath = filePath;
    }
}
