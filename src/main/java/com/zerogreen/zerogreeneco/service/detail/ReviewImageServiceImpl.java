package com.zerogreen.zerogreeneco.service.detail;

import com.zerogreen.zerogreeneco.dto.detail.ReviewImageDto;
import com.zerogreen.zerogreeneco.repository.detail.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;

    //List of Images only..
    @Override
    public List<ReviewImageDto> findByStore(Long sno) {
        return reviewImageRepository.findByStore(sno).stream().map(ReviewImageDto::new).collect(Collectors.toList());
    }

    // delete images
    @Override
    public void deleteReviewImage(Long id, String filePath, String thumbnail) {
        File file = new File(filePath);
        File file2 = new File(thumbnail);

        try {
            if (file.exists()) {
                file.delete();
                file2.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("파일 삭제에 실패했습니다.");
        }
    }


}
