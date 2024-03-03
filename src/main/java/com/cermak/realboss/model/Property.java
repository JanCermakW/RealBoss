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
    @Column(columnDefinition="TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "realman_id")
    private User realman;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    private String MainPicturePath;

    private String priceNote;
    private String building;
    private String objectState;
    private String objectPlacement;
    private Long usableArea;
    private Long floorArea;
    private String floor;
    private Long basement;
    private Long approvalYear;
    private Long reconstructionYear;
    private String water;
    private String electricity;
    private String communicationRoad;

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

    public String getPriceNote() {
        return priceNote;
    }

    public void setPriceNote(String priceNote) {
        this.priceNote = priceNote;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectPlacement() {
        return objectPlacement;
    }

    public void setObjectPlacement(String objectPlacement) {
        this.objectPlacement = objectPlacement;
    }

    public Long getUsableArea() {
        return usableArea;
    }

    public void setUsableArea(Long usableArea) {
        this.usableArea = usableArea;
    }

    public Long getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(Long floorArea) {
        this.floorArea = floorArea;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Long getBasement() {
        return basement;
    }

    public void setBasement(Long basement) {
        this.basement = basement;
    }

    public Long getApprovalYear() {
        return approvalYear;
    }

    public void setApprovalYear(Long approvalYear) {
        this.approvalYear = approvalYear;
    }

    public Long getReconstructionYear() {
        return reconstructionYear;
    }

    public void setReconstructionYear(Long reconstructionYear) {
        this.reconstructionYear = reconstructionYear;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getCommunicationRoad() {
        return communicationRoad;
    }

    public void setCommunicationRoad(String communicationRoad) {
        this.communicationRoad = communicationRoad;
    }
}
