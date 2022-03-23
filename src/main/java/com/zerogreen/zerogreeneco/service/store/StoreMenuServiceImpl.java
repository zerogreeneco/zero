package com.zerogreen.zerogreeneco.service.store;

import com.zerogreen.zerogreeneco.dto.store.StoreMenuDto;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMenu;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import com.zerogreen.zerogreeneco.repository.store.StoreMenuRepository;
import com.zerogreen.zerogreeneco.repository.user.StoreMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMenuServiceImpl implements StoreMenuService {

    private final StoreMenuRepository storeMenuRepository;
    private final StoreMemberRepository storeMemberRepository;

    @Transactional
    @Override
    public void updateStoreMenu(Long id, String menuName, String menuPrice, VegetarianGrade vegetarianGrade) {
        StoreMember storeMember = storeMemberRepository.findById(id).orElseThrow();

        storeMenuRepository.save(new StoreMenu(menuName, menuPrice, vegetarianGrade, storeMember));
    }

    @Override
    public void updateStoreMenu(Long id, String menuName, String menuPrice) {
        StoreMember storeMember = storeMemberRepository.findById(id).orElseThrow();

        storeMenuRepository.save(new StoreMenu(menuName, menuPrice, storeMember));
    }

    @Override
    public List<StoreMenuDto> getStoreMenu(Long id) {
        List<StoreMenu> tableList = storeMenuRepository.getStoreMenu(id);
        return tableList.stream().map(StoreMenuDto::new).collect(Collectors.toList());
    }

    @Override
    public void deleteMenu(Long id) {
        log.info("KGH"+id);
        storeMenuRepository.deleteById(id);
    }


    //테스트 데이터
    @Transactional
    @Override
    public Long saveStoreMenuTest(StoreMenu storeMenu) {
        StoreMember storeMember = storeMemberRepository.findById(storeMenu.getStoreMember().getId()).orElseThrow();

        return storeMenuRepository.save(new StoreMenu(storeMenu.getMenuName(), storeMenu.getMenuPrice(), storeMenu.getVegetarianGrade() ,storeMember))
                .getId();
    }

}
