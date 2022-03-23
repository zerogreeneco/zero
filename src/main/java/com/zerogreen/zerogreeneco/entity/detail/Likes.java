package com.zerogreen.zerogreeneco.entity.detail;

import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.entity.userentity.StoreMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeMember")
    private StoreMember storeMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private BasicUser basicUser;


    //add Likes
    public Likes(BasicUser basicUser, StoreMember storeMember) {
        this.basicUser = basicUser;
        this.storeMember = storeMember;
    }

}
