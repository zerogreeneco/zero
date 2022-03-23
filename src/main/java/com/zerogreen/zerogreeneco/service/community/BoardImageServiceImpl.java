package com.zerogreen.zerogreeneco.service.community;

import com.zerogreen.zerogreeneco.dto.community.ImageFileDto;
import com.zerogreen.zerogreeneco.entity.community.BoardImage;
import com.zerogreen.zerogreeneco.entity.community.CommunityBoard;
import com.zerogreen.zerogreeneco.repository.community.BoardImageRepository;
import com.zerogreen.zerogreeneco.repository.community.CommunityBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardImageServiceImpl implements BoardImageService {

    private final BoardImageRepository boardImageRepository;
    private final CommunityBoardRepository boardRepository;

    /* 이미지 Entity List -> Dto List */
    @Override
    public List<ImageFileDto> findByBoardId(Long boardId) {
        return boardImageRepository.findByBoard(boardId)
                .stream().map(ImageFileDto::new).collect(Collectors.toList());
    }

    // 이미지 추가
    @Override
    public void modifyImage(Long boardId, String storeName, String originalName, String path) {
        CommunityBoard communityBoard = boardRepository.findById(boardId).orElseThrow();
        boardImageRepository.save(new BoardImage(originalName, storeName, path, communityBoard, "thumb_"+storeName));
    }

    // 이미지 삭제
    @Override
    public void deleteImage(Long imageId, String filePath) {
        Path directoryPath = Paths.get("C:/upload//");
        String[] split = filePath.split("/");
        String thumbnail = split[0] + "/" + split[1] + "/" + "thumb_" + split[2];

        log.info("THUMB PATH={}", thumbnail);
        File file = new File(filePath);
        File thumbFile = new File(thumbnail);

        try {
            if (file.exists()) {
                boolean delete = file.delete();
                boolean thumbDelete = thumbFile.delete();
                if (delete) {
                    boardImageRepository.deleteById(imageId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("파일 삭제에 실패했습니다.");
        }
    }

}
