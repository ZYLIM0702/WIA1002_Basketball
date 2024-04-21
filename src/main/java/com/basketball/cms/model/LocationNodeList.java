/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.model;

import java.util.ArrayList;

/**
 *
 * @author szeyu
 */
public class LocationNodeList {
    private LocationNode city;
    private ArrayList<LocationNodeList> neighbour = new ArrayList<>();
    private ArrayList<Double> neighbourDistance = new ArrayList<>();
    private LocationNode parentPath;
    private int shortestDistFromSun;

    public LocationNodeList(LocationNode city) {
        this.city = city;
        this.parentPath = null;
        this.shortestDistFromSun = Integer.MAX_VALUE;
    }

    public LocationNode getParentPath() {
        return parentPath;
    }

    public void setParentPath(LocationNode parentPath) {
        this.parentPath = parentPath;
    }

    public int getShortestDistFromSun() {
        return shortestDistFromSun;
    }

    public void setShortestDistFromSun(int shortestDistFromSun) {
        this.shortestDistFromSun = shortestDistFromSun;
    }

    public LocationNode getCity() {
        return city;
    }

    public ArrayList<LocationNodeList> getNeighbour() {
        return neighbour;
    }

    public ArrayList<Double> getNeighbourDistance() {
        return neighbourDistance;
    }
    
}
