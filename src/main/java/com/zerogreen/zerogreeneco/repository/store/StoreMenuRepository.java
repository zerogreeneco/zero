package com.zerogreen.zerogreeneco.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerogreen.eco.entity.userentity.StoreMenu;

import java.util.List;

public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long> {

    @Query("select menu from StoreMenu menu where menu.storeMember.id=:id")
    List<StoreMenu> getStoreMenu(@Param("id") Long id);



}
