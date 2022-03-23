package com.zerogreen.zerogreeneco.service.store;


import com.zerogreen.zerogreeneco.dto.store.StoreDto;

import java.util.List;

public interface StoreImageService {
    //Image List (Detail)
    List<StoreDto> getImageByStore(Long sno);
   //이미지요 이미지
//    StoreDto getThumbnail(Long sno);
    void deleteImg(Long id, String filePath, String thumbnail);
    }