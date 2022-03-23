package com.zerogreen.zerogreeneco.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zerogreen.eco.entity.userentity.BasicUser;

import java.util.Optional;

public interface BasicUserRepository extends JpaRepository<BasicUser, Long>, BasicUserRepositoryCustom {

    Optional<BasicUser> findByUsername(String username);

    @Query("select u from BasicUser u where u.username =:username ")
    BasicUser findByChatUsername(@Param("username") String username);

    long countByUsernameAndPhoneNumber(@Param("username") String username, @Param("phoneNumber") String phoneNumber);
    int countByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    long countByUsername(@Param("username") String username);


    Optional<BasicUser> findByUsernameAndPhoneNumber(@Param("username") String username, @Param("phoneNumber") String phoneNumber);
    Optional<BasicUser> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
