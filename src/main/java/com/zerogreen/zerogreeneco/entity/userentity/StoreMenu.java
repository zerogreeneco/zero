package com.zerogreen.zerogreeneco.entity.userentity;

import com.zerogreen.zerogreeneco.entity.baseentity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@ToString(exclude = {"storeMember"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String menuName;
    private String menuPrice;

    @Enumerated(EnumType.STRING)
    private VegetarianGrade vegetarianGrade;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private StoreMember storeMember;

    public StoreMenu(String menuName, String menuPrice, VegetarianGrade vegetarianGrade, StoreMember storeMember) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.vegetarianGrade = vegetarianGrade;
        this.storeMember = storeMember;
    }

    public StoreMenu(String menuName, String menuPrice, StoreMember storeMember) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.storeMember = storeMember;
    }

    /*
    * Setter
    * */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public void setVegetarianGrade(VegetarianGrade vegetarianGrade) {
        this.vegetarianGrade = vegetarianGrade;
    }

    public void setStoreMember(StoreMember storeMember) {
        this.storeMember = storeMember;
    }
}
