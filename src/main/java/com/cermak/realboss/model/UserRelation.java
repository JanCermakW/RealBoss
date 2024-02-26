package com.cermak.realboss.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_relation")
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "realman_id")
    private User realman;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRelation(User realman, User user, Role role) {
        this.realman = realman;
        this.user = user;
        this.role = role;
    }

    public UserRelation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRealman() {
        return realman;
    }

    public void setRealman(User realman) {
        this.realman = realman;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
