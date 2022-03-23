package com.zerogreen.zerogreeneco.service.community;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerogreen.eco.dto.community.CommunityRequestDto;
import zerogreen.eco.dto.community.CommunityResponseDto;
import zerogreen.eco.dto.search.SearchCondition;
import zerogreen.eco.entity.community.BoardImage;
import zerogreen.eco.entity.community.Category;
import zerogreen.eco.entity.community.CommunityBoard;
import zerogreen.eco.entity.community.CommunityLike;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.entity.userentity.Member;
import zerogreen.eco.repository.community.BoardImageRepository;
import zerogreen.eco.repository.community.CommunityBoardRepository;
import zerogreen.eco.repository.community.CommunityLikeRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityBoardServiceImpl implements CommunityBoardService {

    private final CommunityBoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final CommunityLikeRepository communityLikeRepository;

    /*
     * 게시글 저장
     * */
    @Override
    @Transactional
    public Long boardRegister(CommunityRequestDto dto, Member writer, List<BoardImage> imageList) {

        CommunityBoard saveBoard = boardRepository.save(CommunityBoard.builder()
                .text(dto.getText())
                .category(dto.getCategory())
                .member(writer)
                .isChat(dto.isChat())
                .build());
        // 트랜젝션 전에 지연 SQL 저장소 -> DB로 전송 (이미지 파일 저장을 위해서)
        boardRepository.flush();

        // 이미지 리스트에 데이터가 있다면 저장 (미리 INSERT한 Board를 호출해서 연관관계 저장)
        if (imageList.size() != 0) {
            for (BoardImage boardImage : imageList) {
                boardImageRepository.save(new BoardImage(
                        boardImage.getUploadFileName(), boardImage.getStoreFileName(), boardImage.getFilePath(), saveBoard, "thumb_" + boardImage.getStoreFileName()));
            }
        }
        return saveBoard.getId();
    }

    /*
     * 상세 보기 (조회수 중복 방지)
     * */
    @Override
    public CommunityResponseDto findDetailView(Long boardId, HttpServletRequest request, HttpServletResponse response) {

        Cookie viewCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(boardId + "_community")) {
                    viewCookie = cookie;
                }
            }
        } else {
            log.info("COOKIE IS NOT EXIST!");
        }

        if (viewCookie == null) {
            Cookie newCookie = new Cookie(boardId + "_community", "boardView");
            response.addCookie(newCookie);
            boardRepository.addViewCount(boardId);
        } else {
            log.info("COOKIE IS EXIST");
            log.info("EXIST COOKIE={}", viewCookie.getValue());
        }
        return boardRepository.findDetailView(boardId);
    }

    /* 게시글 수정 */
    @Override
    @Transactional
    public void boardModify(Long boardId, Category category, String text) {
        CommunityBoard communityBoard = boardRepository.findById(boardId).orElseThrow();
        communityBoard.changeBoard(category, text);
    }

    /* 게시글 삭제 */
    @Override
    @Transactional
    public void boardDelete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    /* 수정용 데이터 불러오기 */
    @Override
    public CommunityRequestDto boardModifyRequest(Long boardId) {
        return boardRepository.boardModify(boardId);
    }

    /* 조회수 */
    @Override
    public int boardCount(Long boardId) {
        return boardRepository.boardCount(boardId);
    }

    /* 좋아요 추가 */
    @Override
    @Transactional
    public void insertLike(Long boardId, BasicUser basicUser) {

        CommunityBoard communityBoard = boardRepository.findById(boardId).orElseThrow();
        communityLikeRepository.save(new CommunityLike(communityBoard, basicUser));
    }

    /* 좋아요 삭제 */
    @Override
    public void deleteLike(Long boardId, Long memberId) {
        communityLikeRepository.deleteLike(boardId, memberId);
    }

    /* 좋아요 수 */
    @Override
    public int countLike(Long boardId, Long memberId) {
        return communityLikeRepository.countLike(boardId, memberId);
    }

    @Override
    public int countLikeByBoard(Long boardId) {
        return communityLikeRepository.countByBoard(boardId);
    }

    /* 검색 조건 페이징 */
    @Override
    public Slice<CommunityResponseDto> findAllCommunityBoard(Pageable pageable, SearchCondition condition) {
        return boardRepository.findAllCommunityList(pageable, condition);
    }

    /* 페이징 */
    @Override
    public Slice<CommunityResponseDto> findAllCommunityBoard(Pageable pageable) {
        return boardRepository.findAllCommunityList(pageable);
    }

    /* 카테고리 */
    @Override
    public Slice<CommunityResponseDto> findByCategory(Pageable pageable, Category category) {
        return boardRepository.findByCategory(pageable, category);
    }
}