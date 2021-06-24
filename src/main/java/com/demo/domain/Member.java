package com.demo.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 團隊成員
 */
@Entity
@Table(name = "demo_member")
@Data
public class Member implements Serializable {

    /**
     * 成員編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "no_")
    private Long no;

    /**
     * 姓名
     */
    @Column(name = "name_")
    private String name;


    @Column(name = "title_")
    private Title title;
}
