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
public class LocationNodeList implements Comparable<LocationNodeList> {
    private LocationNode city;
    private ArrayList<LocationNodeList> neighbour = new ArrayList<>();
    private ArrayList<Double> neighbourDistance = new ArrayList<>();
    private LocationNodeList parentPath;
    private double shortestDistFromSun;

    public LocationNodeList(LocationNode city) {
        this.city = city;
        this.parentPath = null;
        this.shortestDistFromSun = Integer.MAX_VALUE;
    }

    public LocationNodeList getParentPath() {
        return parentPath;
    }

    public void setParentPath(LocationNodeList parentPath) {
        this.parentPath = parentPath;
    }

    public double getShortestDistFromSun() {
        return shortestDistFromSun;
    }

    public void setShortestDistFromSun(double shortestDistFromSun) {
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
    
// Implementing compareTo method from Comparable interface
    @Override
    public int compareTo(LocationNodeList other) {
        return Double.compare(this.shortestDistFromSun, other.shortestDistFromSun);
    }
}
