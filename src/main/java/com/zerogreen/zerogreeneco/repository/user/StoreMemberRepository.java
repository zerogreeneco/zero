package com.zerogreen.zerogreeneco.repository.user;

import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import com.zerogreen.zerogreeneco.entity.userentity.UserRole;
import com.zerogreen.zerogreeneco.repository.list.StoreListRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreMemberRepository extends JpaRepository<StoreMember, Long>,
        StoreMemberRepositoryCustom, StoreListRepository {

    Optional<StoreMember> findByUsername(String username);

    Optional<StoreMember> findById(Long id);

    int countByStoreRegNum(@Param("storeRegNum") String storeRegNum);

    //승인받은 스토어
    @Query("select sm from StoreMember sm left outer join BasicUser bu on sm.id = bu.id " +
            "where bu.userRole ='STORE' ")
    List<StoreMember> findByApprovedStore(UserRole userRole);

}
