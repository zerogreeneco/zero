package com.zerogreen.zerogreeneco;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.zerogreen.zerogreeneco.dto.community.CommunityRequestDto;
import com.zerogreen.zerogreeneco.entity.community.Category;
import com.zerogreen.zerogreeneco.entity.community.CommunityBoard;
import com.zerogreen.zerogreeneco.entity.detail.DetailReview;
import com.zerogreen.zerogreeneco.entity.file.RegisterFile;
import com.zerogreen.zerogreeneco.entity.userentity.*;
import com.zerogreen.zerogreeneco.repository.community.CommunityBoardRepository;
import com.zerogreen.zerogreeneco.repository.detail.DetailReviewRepository;
import com.zerogreen.zerogreeneco.repository.user.MemberRepository;
import com.zerogreen.zerogreeneco.repository.user.StoreMemberRepository;
import com.zerogreen.zerogreeneco.service.detail.DetailReviewService;
import com.zerogreen.zerogreeneco.service.store.StoreMenuService;
import com.zerogreen.zerogreeneco.service.user.BasicUserService;
import com.zerogreen.zerogreeneco.service.user.MemberService;
import com.zerogreen.zerogreeneco.service.user.StoreMemberService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void initService() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {
        private final EntityManager em;

        private final MemberService memberService;
        private final StoreMemberService storeMemberService;
        private final BasicUserService basicUserService;
        private final DetailReviewService detailReviewService;
        private final StoreMenuService storeMenuService;

        private final MemberRepository memberRepository;
        private final StoreMemberRepository storeMemberRepository;
        private final CommunityBoardRepository communityBoardRepository;
        private final DetailReviewRepository detailReviewRepository;

        public void init() {

            List<String> ecoAddress = new ArrayList<>();
            ecoAddress.add("부산광역시 수영구 장대골로 76");
            ecoAddress.add("부산광역시 북구 덕천1길 93 2층");
            ecoAddress.add("부산광역시 부산진구 성지로14번길 13");
            ecoAddress.add("부산광역시 해운대구 대천로103번길 61");
            ecoAddress.add("부산광역시 연제구 신금로 25 2층 227호");
            ecoAddress.add("부산광역시 수영구 민락로33번길 5");
            ecoAddress.add("부산광역시 부산진구 중앙대로 821");
            ecoAddress.add("부산광역시 기장군 정관읍 모전3길 32-8 1층");
            ecoAddress.add("부산광역시 남구 수영로 149 2층");
            ecoAddress.add("부산광역시 사하구 낙동대로 129 지하1F");

            List<String> foodAddress = new ArrayList<>();
            foodAddress.add("부산광역시 부산진구 동성로87번길 9 2층");
            foodAddress.add("부산광역시 남구 수영로39번나길 53 미인당");
            foodAddress.add("부산광역시 동래구 온천천로471번가길 20");
            foodAddress.add("부산광역시 서구 구덕로124번길 13 3층");
            foodAddress.add("부산광역시 해운대구 해운대해변로 280");
            foodAddress.add("부산광역시 동구 중앙대로 526");
            foodAddress.add("부산광역시 해운대구 센텀중앙로 145 2상가 104호");
            foodAddress.add("부산광역시 수영구 망미번영로70번길 16 1층");
            foodAddress.add("부산광역시 동래구 온천천로471번길 7 1층 104호");
            foodAddress.add("부산광역시 부산진구 전포대로186번길 33 1층 그리구요거트");
            foodAddress.add("부산광역시 수영구 광안해변로 193");
            foodAddress.add("부산광역시 수영구 수영로510번길 42");
            foodAddress.add("부산광역시 북구 금곡대로303번길 36 스페이스303 1층 106호");
            foodAddress.add("부산광역시 남구 진남로 54 대동래미안3 수수베이커리");
            foodAddress.add("부산광역시 부산진구 범양로120번길 6 1층,세자매바른빵");
            foodAddress.add("부산광역시 금정구 중앙대로 1667 삼신빌딩 1층");
            foodAddress.add("부산광역시 동래구 동래로79번길 26 1층");

            List<String> general = new ArrayList<>();
            general.add("부산광역시 중구 중앙대로 17 광복지하도상가A57~59");
            general.add("부산광역시 연제구 아시아드대로19번길 16");
            general.add("부산광역시 강서구 명지국제5로 165");
            general.add("부산광역시 해운대구 선수촌로 95 센텀대림아파트 대림상가 12동 3층");
            general.add("부산 수영구 민락수변로 29 3층 디에이블");
            general.add("부산 부산진구 동천로 92 NC서면점 5층");
            general.add("부산 동구 중앙대로209번길 12");
            general.add("부산 부산진구 서면문화로 19");
            general.add("부산 부산진구 서면로 26");


            for (int i = 0; i < ecoAddress.size(); i++) {
                storeMemberService.saveV3(new StoreMember("ecotest" + i, "01033334444", "1",
                        UserRole.UN_STORE, "ECO_SHOP" + i, "REGNUM1111", StoreType.ECO_SHOP, "1111",
                        ecoAddress.get(i), "DA", "0517778888", "descript", "alalal", "dddd", "10:00", "22:00", new RegisterFile("origin3", "store3", "path3"))
                );
            }

            for (int i = 0; i < ecoAddress.size(); i++) {
                storeMemberService.saveV2(new StoreMember("ecoTest" + i, "01033334444", "1",
                                UserRole.UN_STORE, "ECO_SHOP" + i, "REGNUM1111", StoreType.ECO_SHOP,
                                ecoAddress.get(i), "DA", "0517778888", "1111")
                        , new RegisterFile("origin3", "store3", "path3"));
            }

            for (int i = 0; i < foodAddress.size(); i++) {
                storeMemberService.saveV2(new StoreMember("foodTest" + i, "01033334444", "1",
                        UserRole.UN_STORE, "FOOD_SHOP" + i, "REGNUM1111", StoreType.VEGAN_FOOD,
                        foodAddress.get(i), "", "0517778888", "11111"), new RegisterFile("origin3", "store3", "path3"));
            }

            for (int i = 0; i < general.size(); i++) {
                storeMemberService.saveV2(new StoreMember("generalTest" + i, "01033334444", "1",
                        UserRole.UN_STORE, "GENERAL_SHOP" + i, "REGNUM1111", StoreType.GENERAL_FOOD,
                        general.get(i), "", "0517778888", "11111"), new RegisterFile("origin3", "store3", "path3"));
            }

            StoreMember ecoTest = new StoreMember("ecoTest", "01033334444", "1",
                    UserRole.UN_STORE, "ECO_SHOP", "REGNUM1111", StoreType.ECO_SHOP,
                    "부산 해운대구 중동2로 11", "우동", "0517778888", "11111");

            StoreMember foodTest = new StoreMember("foodTest", "01044445555", "1",
                    UserRole.UN_STORE, "VEGAN_FOOD", "0987654321", StoreType.VEGAN_FOOD,
                    "부산 동래구 온천천로359번길 70", "안락동", "0519998888", "99999");

            StoreMember noVegan = new StoreMember("noVeganlTest", "01044445555", "1",
                    UserRole.UN_STORE, "NO_VEGAN_FOOD", "785654321", StoreType.GENERAL_FOOD,
                    "부산 연제구 연제로 2", "연산동", "0517776666", "88888");

            StoreMember ecoTest2 = new StoreMember("ecoTest2", "01012345678", "1",
                    UserRole.STORE, "동영동영의 ECO_SHOP2", "0511234567", StoreType.ECO_SHOP,
                    "부산 사하구 낙동대로398번길 12", "하단동", "0511234567", "22222");

            StoreMember foodTest2 = new StoreMember("foodTest2", "01098765432", "1",
                    UserRole.STORE, "Ran ran VEGAN_FOOD2", "0519876543", StoreType.VEGAN_FOOD,
                    "부산시 동래구 명장로 22번길 29", "덕천동", "0519876541", "33333");

            StoreMember noVegan2 = new StoreMember("noVeganlTest2", "01799997777", "1",
                    UserRole.STORE, "Ben NO_VEGAN_FOOD2", "0517894561", StoreType.GENERAL_FOOD,
                    "부산 금정구 중앙대로 1777", "장전동", "0517894561", "44444");

            // 일반 회원
            Member member = new Member("test", "김그네", "01022223333", "1",
                    UserRole.USER, VegetarianGrade.VEGAN);
            CommunityRequestDto dto = new CommunityRequestDto("TEST TEXT", Category.PLOGGING);
            memberService.save(member);

            Member member2 = new Member("test2", "오융", "01022224444", "1",
                    UserRole.USER, VegetarianGrade.LACTO);
            CommunityRequestDto dto2 = new CommunityRequestDto("TEST TEXT2", Category.PLOGGING);
            memberService.save(member2);

            // 가게 회원
            for (int i = 0; i < 1; i++) {
                storeMemberService.save(ecoTest, new RegisterFile("origin1", "store1", "path1"));
                storeMemberService.save(foodTest, new RegisterFile("origin2", "store2", "path2"));
                storeMemberService.save(noVegan, new RegisterFile("origin3", "store3", "path3"));
            }
            storeMemberService.saveV2(ecoTest2, new RegisterFile("origin1", "store1", "path1"));
            storeMemberService.saveV2(foodTest2, new RegisterFile("origin2", "store2", "path2"));
            storeMemberService.saveV2(noVegan2, new RegisterFile("origin3", "store3", "path3"));

            // 관리자 계정
            basicUserService.adminSave();


            em.flush();
            em.clear();

            //Detail 부모리뷰 추가
            Member findMember1 = memberRepository.findByUsername("test").get();
            Member findMember2 = memberRepository.findByUsername("test2").get();
            StoreMember findEcoStore1 = storeMemberRepository.findByUsername("ecotest5").get();
            StoreMember findEcoStore2 = storeMemberRepository.findByUsername("ecotest6").get();
            StoreMember foodTest1 = storeMemberRepository.findByUsername("foodTest1").get();
            StoreMember generalTest1 = storeMemberRepository.findByUsername("generalTest1").get();


            DetailReview detailReview1 = new DetailReview("mReview1 by test", findMember1, findEcoStore1);
            detailReviewService.saveReviewTest(detailReview1);
            DetailReview detailReview2 = new DetailReview("mReview3 by test2", findMember2, findEcoStore1);
            detailReviewService.saveReviewTest(detailReview2);

            DetailReview detailReview3 = new DetailReview("mReview1 by test", findMember1, findEcoStore2);
            detailReviewService.saveReviewTest(detailReview3);
            DetailReview detailReview4 = new DetailReview("mReview3 by test2", findMember2, findEcoStore2);
            detailReviewService.saveReviewTest(detailReview4);

            DetailReview detailReview5 = new DetailReview("mReview1 by test", findMember1, foodTest1);
            detailReviewService.saveReviewTest(detailReview5);
            DetailReview detailReview6 = new DetailReview("mReview3 by test2", findMember2, foodTest1);
            detailReviewService.saveReviewTest(detailReview6);

            DetailReview detailReview7 = new DetailReview("mReview1 by test", findMember1, generalTest1);
            detailReviewService.saveReviewTest(detailReview7);
            DetailReview detailReview8 = new DetailReview("mReview3 by test2", findMember2, generalTest1);
            detailReviewService.saveReviewTest(detailReview8);


            // 커뮤니티 리스트 추가
            communityBoardRepository.save(new CommunityBoard("TEST TEXT", findMember1, Category.PLOGGING, true));
            communityBoardRepository.save(new CommunityBoard("TEST TEXT2", findMember1, Category.PLOGGING, true));
            communityBoardRepository.save(new CommunityBoard("TEST TEXT3, " +
                    "TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, " +
                    "TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, " +
                    "TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, " +
                    "TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, TEST TEXT3, " +
                    "TEST TEXT3, TEST TEXT3, TEST TEXT3, END", findMember1, Category.NEWS, true));
            for (int i = 0; i < 10; i++) {
                communityBoardRepository.save(new CommunityBoard("TEST TEXT" + i, findMember1, Category.PLOGGING, true));
                communityBoardRepository.save(new CommunityBoard("테스트입니다" + i, findMember2, Category.NEWS, true));
            }
            communityBoardRepository.save(new CommunityBoard("TEST TEXT", findMember2, Category.NEWS, false));

            em.flush();
            em.clear();


            //Detail 자식리뷰 추가
            Long parentsReview1 = detailReviewRepository.findById(1L).get().getId();
            Long parentsReview2 = detailReviewRepository.findById(2L).get().getId();
            Long parentsReview3 = detailReviewRepository.findById(5L).get().getId();
            Long parentsReview4 = detailReviewRepository.findById(6L).get().getId();


            DetailReview detailReview9 = new DetailReview("childReview1 by Store", findEcoStore1, findEcoStore1);
            detailReviewService.saveNestedReviewTest(detailReview9, parentsReview1);
            DetailReview detailReview10 = new DetailReview("childReview3 by Store", findEcoStore1, findEcoStore1);
            detailReviewService.saveNestedReviewTest(detailReview10, parentsReview2);

            DetailReview detailReview11 = new DetailReview("childReview1 by Store", foodTest1, foodTest1);
            detailReviewService.saveNestedReviewTest(detailReview11, parentsReview3);
            DetailReview detailReview12 = new DetailReview("childReview3 by Store", foodTest1, foodTest1);
            detailReviewService.saveNestedReviewTest(detailReview12, parentsReview4);


            em.flush();
            em.clear();


            //StoreMenu
            StoreMenu storeMenu1 = new StoreMenu("menu1-1", "1,000", VegetarianGrade.LACTO, foodTest1);
            storeMenuService.saveStoreMenuTest(storeMenu1);
            StoreMenu storeMenu11 = new StoreMenu("menu1-2 Luv", "1,000", VegetarianGrade.PLEXITARIAN, foodTest1);
            storeMenuService.saveStoreMenuTest(storeMenu11);
            StoreMenu storeMenu2 = new StoreMenu("menu2-1", "5,000", VegetarianGrade.PLEXITARIAN, generalTest1);
            storeMenuService.saveStoreMenuTest(storeMenu2);
            StoreMenu storeMenu22 = new StoreMenu("menu2-2 XO", "5,000", VegetarianGrade.PLEXITARIAN, generalTest1);
            storeMenuService.saveStoreMenuTest(storeMenu22);

            em.flush();
            em.clear();


        }


    }

}