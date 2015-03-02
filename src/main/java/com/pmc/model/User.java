package com.pmc.model;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Column(unique=true, nullable = false)
    private String login;

    @NotEmpty
    private String password;

    public User() {}

    public User(User user) {
        super();
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

}
