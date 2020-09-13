package com.cho.songstagram.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class IpBanList extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name="ip_id")
    private Long id;

    private String ip; //차단할 IP

    public IpBanList(String ip){
        this.ip = ip;
    }
}
