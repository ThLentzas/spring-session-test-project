package com.example.session.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/*
    User is reserved word in psql. Anyway we use plural for naming tables.

    The reason why both Role and User Entities have to implement Serializable is because they are part of the
    authentication object of the Security Context that is stored in Redis as the value of the SPRING_SECURITY_CONTEXT
    KEY
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    /*
        If we leave the default type of LAZY it will fail to get the roles. The UserDetails will be called and the
        transaction will end. In another session will try to grab the authorities, but it will fail since the transaction
        is closed.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
