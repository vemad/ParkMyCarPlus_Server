package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties({"authorities", "accountNonLocked"})
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

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private List<Favorite> favorites = new ArrayList<Favorite>();

    public User() {
    }

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name="taken_place_id")
    private Place takenPlace;

    public User(User user) {
        super();
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.favorites = user.getFavorites();
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Place getPlace() { return takenPlace; }

    public User takePlace(Place place) {
        this.takenPlace = place;
        return this;
    }

    public User releasePlace() {
        this.takenPlace = null;
        return this;
    }

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

    public User addFavorite(Favorite favorite) {
        favorites.add(favorite);
        return this;
    }

    public User removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
        return this;
    }

    //TODO Delete this or refactor it
    @Override
    public String toString() {
        String s1 ="";
        for(Favorite f :favorites){
            s1+=Integer.toString(f.getId())+' ';
        }
        return s1;
    }
}
