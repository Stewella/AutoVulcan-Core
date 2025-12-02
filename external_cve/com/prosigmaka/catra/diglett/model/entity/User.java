package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(generator = "user-generator")
    @GenericGenerator(name = "user-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "USR"),
            strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    @Column(name = "userid")
    private String idUser;
    @Column(name = "usernm", nullable = false)
    private String nama;
    @Column(name = "usertype", nullable = false)
    private String type;
    @Column(name = "usermail", nullable = false)
    private String email;
    @Column(nullable = false)
    private Integer isDelete;
}
