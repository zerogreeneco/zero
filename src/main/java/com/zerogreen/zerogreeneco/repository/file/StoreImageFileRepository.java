package com.zerogreen.zerogreeneco.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerogreen.eco.entity.file.StoreImageFile;

import java.util.List;

public interface StoreImageFileRepository extends JpaRepository<StoreImageFile, Long> {

    @Query("select si from StoreImageFile si where si.storeMember.id =:sno")
    List<StoreImageFile> getImageByStore(@Param("sno") Long sno);
}
