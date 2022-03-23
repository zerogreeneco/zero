package com.zerogreen.zerogreeneco.entity.userentity;

public enum VegetarianGrade {
    VEGAN("비건"), LACTO("락토 베지테리언"), OVO("오보 베지테리언"),
    LACTO_OVO("락토-오보 베지테리언"), PESCO("페스코 베지테리언"), POLLO("폴로 베지테리언"), PLEXITARIAN("플렉시타리언");

    private final String vegetarian;

    VegetarianGrade(String vegetarian) {
        this.vegetarian = vegetarian;
    }

    public String getVegetarian() {
        return vegetarian;
    }
}
