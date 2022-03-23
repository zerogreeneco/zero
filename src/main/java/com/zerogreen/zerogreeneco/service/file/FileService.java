package com.zerogreen.zerogreeneco.service.file;

import com.zerogreen.zerogreeneco.entity.community.BoardImage;
import com.zerogreen.zerogreeneco.entity.detail.ReviewImage;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.file.StoreImageFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    String getFullPath(String filename);
    String getFullPathRegFile(String filename);
    String getFullPathImage(String filename, String storeName);

    RegisterFile saveFile(MultipartFile multipartFile) throws IOException;
    StoreImageFile saveImageFile(MultipartFile multipartFile, String storeName) throws IOException;
    BoardImage saveBoardImageFile(MultipartFile multipartFile) throws IOException;

    List<StoreImageFile> storeImageFiles(List<MultipartFile> multipartFiles, String storeName) throws IOException;
    List<BoardImage> boardImageFiles(List<MultipartFile> multipartFiles) throws IOException;

    //리뷰 이미지
    ReviewImage saveReviewImage(MultipartFile multipartFile) throws IOException;
    List<ReviewImage> reviewImageFiles(List<MultipartFile> multipartFiles) throws IOException;

}
