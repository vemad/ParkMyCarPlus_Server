package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@JsonIgnoreProperties({"enabled", "authorities", "accountNonLocked", "accountNonExpired"})
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Column(unique=true, nullable = false)
    private String username;

    @NotEmpty
    private String password;

    public User() {
    }

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name="taken_place_id")
    private Place takenPlace;

	private int score;

<<<<<<< HEAD
    private int confianceScore;
=======
    private String macAddress;
>>>>>>> 0f3f9a8f9f992bdc20ce0917aeaccca458d24219

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }


    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public Place getPlace() { return takenPlace; }

    public int getScore() { return this.score; }

<<<<<<< HEAD
    public int getConfianceScore() { return this.confianceScore; }
=======
    public String getMacAddress() { return this.macAddress; }

    public User setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }
>>>>>>> 0f3f9a8f9f992bdc20ce0917aeaccca458d24219

    public User takePlace(Place place) {
        this.takenPlace = place;
        return this;
    }

    public User releasePlace() {
        this.takenPlace = null;
        return this;
    }

    public void addScore(int value){
        this.score += value;
        if(this.score<0) this.score = 0;
    }

    public void addConfianceScore(int value){
        this.confianceScore += value;
    }

    @JsonProperty
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonProperty("level")
    public UserLevel getLevel(){
        return UserLevel.getLevel(this.score);
    }

}
