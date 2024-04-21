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

@Entity
@Table(name = "CITY_DISTANCE")
public class LocationNodeList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "distance_id")
    private int distance_id;

    @ManyToOne
    @JoinColumn(name = "city1_id", referencedColumnName = "location_id")
    private LocationNode city1;

    @ManyToOne
    @JoinColumn(name = "city2_id", referencedColumnName = "location_id")
    private LocationNode city2;

    @Column(name = "distance")
    private double distance;


    public int getDistance_id() {
        return distance_id;
    }

    public void setDistance_id(int distance_id) {
        this.distance_id = distance_id;
    }

    public LocationNode getCity1() {
        return city1;
    }

    public void setCity1(LocationNode city1) {
        this.city1 = city1;
    }

    public LocationNode getCity2() {
        return city2;
    }

    public void setCity2(LocationNode city2) {
        this.city2 = city2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    

}
