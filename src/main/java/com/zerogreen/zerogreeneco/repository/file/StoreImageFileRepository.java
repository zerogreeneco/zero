package com.zerogreen.zerogreeneco.repository.file;

import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreImageFileRepository extends JpaRepository<StoreImageFile, Long> {

    @Query("select si from StoreImageFile si where si.storeMember.id =:sno")
    List<StoreImageFile> getImageByStore(@Param("sno") Long sno);
}
