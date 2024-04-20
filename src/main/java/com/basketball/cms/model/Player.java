/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.model;

/**
 *
 * @author limziyang
 */
import jakarta.persistence.*;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PLAYER")
@SecondaryTables({
    @SecondaryTable(name = "STATISTIC", pkJoinColumns = @PrimaryKeyJoinColumn(name = "player_id")),
    @SecondaryTable(name = "CONTRACT", pkJoinColumns = @PrimaryKeyJoinColumn(name = "player_id"))
})

public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private int playerId;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;
    @Column(name = "age", table = "PLAYER")
    private int age;
    @Column(name = "height", table = "PLAYER")
    private double height;
    @Column(name = "weight", table = "PLAYER")
    private double weight;
    @Column(name = "Image", table = "PLAYER")
    private String Image;
    @Column(name = "jersey_num", table = "PLAYER")
    private int jerseyNum;

    @Column(name = "position", table = "PLAYER", columnDefinition = "TEXT")
    private String position;
    @Column(name = "salary", table = "PLAYER")
    private double salary;
    @Column(name = "Is_Added", table = "PLAYER")
    private int is_added;

    @Column(name = "points", table = "STATISTIC")
    private double points;

    @Column(name = "blocks", table = "STATISTIC")
    private double blocks;

    @Column(name = "rebounds", table = "STATISTIC")
    private double rebounds;

    @Column(name = "assists", table = "STATISTIC")
    private double assists;

    @Column(name = "steals", table = "STATISTIC")
    private double steals;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_created", table = "CONTRACT")
    private Date dateCreated;

    @Column(name = "contract_status", table = "CONTRACT")
    private String contractStatus;
    @Column(name= "status_id")
    private int statusId;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
   

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getJerseyNum() {
        return jerseyNum;
    }

    public void setJerseyNum(int jerseyNum) {
        this.jerseyNum = jerseyNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getBlocks() {
        return blocks;
    }

    public void setBlocks(double blocks) {
        this.blocks = blocks;
    }

    public double getRebounds() {
        return rebounds;
    }

    public void setRebounds(double rebounds) {
        this.rebounds = rebounds;
    }

    public double getAssists() {
        return assists;
    }

    public void setAssists(double assists) {
        this.assists = assists;
    }

    public double getSteals() {
        return steals;
    }

    public void setSteals(double steals) {
        this.steals = steals;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public int getIs_added() {
        return is_added;
    }

    public void setIs_added(int is_added) {
        this.is_added = is_added;
    }

}



