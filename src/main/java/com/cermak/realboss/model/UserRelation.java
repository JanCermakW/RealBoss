package com.cermak.realboss.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_relation")
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "realman_id")
    private User realman;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserRelation(User realman, User user, Role role) {
        this.realman = realman;
        this.user = user;
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
}
