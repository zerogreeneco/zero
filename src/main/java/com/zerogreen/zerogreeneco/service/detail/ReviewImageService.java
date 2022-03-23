package com.zerogreen.zerogreeneco.service.detail;


import com.zerogreen.zerogreeneco.dto.detail.ReviewImageDto;

import java.util.List;

public interface ReviewImageService {

    //listing
    List<ReviewImageDto> findByStore(Long sno);
    //delete review Images
    void deleteReviewImage(Long id, String filePath, String thumbnail);

}
