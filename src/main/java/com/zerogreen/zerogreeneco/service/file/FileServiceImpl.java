package com.zerogreen.zerogreeneco.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zerogreen.eco.entity.community.BoardImage;
import zerogreen.eco.entity.detail.ReviewImage;
import zerogreen.eco.entity.file.RegisterFile;
import zerogreen.eco.entity.file.StoreImageFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${file.regFiledir}")
    private String regFileDir;

    @Value("C:/imageUpload/")
    private String imageFileDir;

    @Override
    public String getFullPath(String filename) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(System.currentTimeMillis());

        File dir = new File(fileDir + date);

        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("이미 존재하는 폴더입니다.");
        }

        return dir + "/" + filename;
    }

    // 사업자 등록증
    @Override
    public String getFullPathRegFile(String filename) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(System.currentTimeMillis());

        File dir = new File(regFileDir + date);

        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("이미 존재하는 폴더입니다.");
        }

        return dir + "/" + filename;
    }

    @Override
    public String getFullPathImage(String filename, String storeName) {
        return imageFileDir + storeName + filename;
    }

    /*
     * 다수의 파일 저장 (이미지)
     * */
    @Override
    public List<StoreImageFile> storeImageFiles(List<MultipartFile> multipartFiles, String storeName) throws IOException {

        List<StoreImageFile> storeImageFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImageFileResult.add(saveImageFile(multipartFile, storeName));
            }
        }
        return storeImageFileResult;
    }

    /*
     * 단일 파일 저장(사업자 등록증)
     * */
    @Override
    public RegisterFile saveFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }
        // 사용자가 저장한 파일 이름
        String originalFilename = multipartFile.getOriginalFilename();

        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPathRegFile(storeFilename)));

        return new RegisterFile(originalFilename, storeFilename, getFullPathRegFile(storeFilename));

    }

    /*
     * 단일 파일 저장 (이미지)
     * */
    @Override
    public StoreImageFile saveImageFile(MultipartFile multipartFile, String storeName) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        // 사용자가 저장한 파일 이름
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        String thumbnailName = "thumb_" + storeFilename;

        File saveFile = new File(getFullPathImage(storeFilename, storeName));
        multipartFile.transferTo(saveFile);

        File thumbnailFile = new File(getFullPathImage(thumbnailName, storeName));

        BufferedImage readImage = ImageIO.read(saveFile);
        //burrefredImage 생성
        BufferedImage thumbImage = new BufferedImage(300, 300, BufferedImage.TYPE_3BYTE_BGR);
        //그래픽 생성
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.drawImage(readImage, 0, 0, 300, 300, null);
        //썸네일 생성
        ImageIO.write(thumbImage, "png", thumbnailFile);

        return new StoreImageFile(originalFilename, storeFilename, thumbnailName, getFullPathImage(storeFilename, storeName), getFullPathImage(thumbnailName, storeName));
    }

    /*
     * 커뮤니티 이미지 업로드
     * */
    @Override
    public BoardImage saveBoardImageFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        // 사용자가 저장한 파일 이름
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        String thumbnailName = "thumb_" + storeFilename;
        File saveFile = new File(getFullPath(storeFilename));
        multipartFile.transferTo(saveFile);

        makeThumbnail(thumbnailName, saveFile, 146, 146);

        return new BoardImage(originalFilename, storeFilename, getFullPath(storeFilename), thumbnailName);
    }

    @Override
    public List<BoardImage> boardImageFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<BoardImage> boardImages = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                boardImages.add(saveBoardImageFile(multipartFile));
            }
        }
        return boardImages;

    }

    private BufferedImage resize(InputStream inputStream, int width, int height) throws IOException {

        BufferedImage inputImage = ImageIO.read(inputStream);

        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
    }

    //리뷰이미지
    @Override
    public ReviewImage saveReviewImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String reviewFileName = createStoreFilename(originalFilename);
        String thumbnailName = "thumb_" + reviewFileName;
        File saveFile = new File(getFullPath(reviewFileName));
        multipartFile.transferTo(saveFile);

        makeThumbnail(thumbnailName, saveFile, 120, 120);

        return new ReviewImage(originalFilename, reviewFileName, getFullPath(reviewFileName), thumbnailName);

    }

    //리뷰이미지
    @Override
    public List<ReviewImage> reviewImageFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                reviewImages.add(saveReviewImage(multipartFile));
            }
        }
        return reviewImages;
    }

    /*
     * 서버에 저장될 이름 생성
     * */
    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        //uuid
        String uuid = UUID.randomUUID().toString();
        // uuid.확장자 형태로 저장
        return uuid + "." + ext;
    }

    /*
     * 확장자
     * */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /*
    * 썸네일 생성
    * */
    private void makeThumbnail(String thumbnailName, File saveFile, int width, int height) throws IOException {
        File thumbnailFile = new File(getFullPath(thumbnailName));
        BufferedImage readImage = ImageIO.read(saveFile);

        int w = readImage.getWidth();
        int h = readImage.getHeight();
        int min = Math.min(w, h);

        BufferedImage tmpImage = Scalr.crop(readImage, (w-min)/2, (h-min)/2, min, min);
        BufferedImage thumbImage = Scalr.resize(tmpImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 178);
        //BufferedImage thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        //Graphics2D graphics = thumbImage.createGraphics();
        //graphics.drawImage(readImage, 0, 0, width, height, null);
        ImageIO.write(thumbImage, "png", thumbnailFile);
    }

}
