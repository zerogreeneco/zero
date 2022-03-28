package com.zerogreen.zerogreeneco.dto.store;

import com.zerogreen.zerogreeneco.entity.userentity.StoreMenu;
import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
public class StoreMenuDto {

    private Long id;
    private String menuName;
    private String menuPrice;
    private VegetarianGrade vegetarianGrade;

    public StoreMenuDto(String menuName, String menuPrice, VegetarianGrade vegetarianGrade){
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.vegetarianGrade = vegetarianGrade;
    }

    public StoreMenuDto(StoreMenu storeMenu) {
        this.id = storeMenu.getId();
        this.menuName = storeMenu.getMenuName();
        this.menuPrice = storeMenu.getMenuPrice();
        this.vegetarianGrade = storeMenu.getVegetarianGrade();
    }

}
