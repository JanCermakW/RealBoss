package com.cermak.realboss.model;

import jakarta.persistence.*;

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private String city;
    @Column(name = "post_num")
    private String postNum;

    private String street;
    private String description;
    @ManyToOne
    @JoinColumn(name = "realman_id")
    private User realman;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    private String MainPicturePath;

    public Property(String name, Long price, String city, String postNum, String street, String description, User realman, User customer, String mainPicturePath) {
        this.name = name;
        this.price = price;
        this.city = city;
        this.postNum = postNum;
        this.street = street;
        this.description = description;
        this.realman = realman;
        this.customer = customer;
        MainPicturePath = mainPicturePath;
    }

    public Property() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getRealman() {
        return realman;
    }

    public void setRealman(User user) {
        this.realman = user;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getMainPicturePath() {
        return MainPicturePath;
    }

    public void setMainPicturePath(String mainPicturePath) {
        MainPicturePath = mainPicturePath;
    }
}
