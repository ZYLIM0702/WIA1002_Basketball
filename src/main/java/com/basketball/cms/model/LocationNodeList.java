/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    // Method to sort neighbors and their corresponding distances in ascending order
    public void sortNeighboursByDistance() {
        ArrayList<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < neighbour.size(); i++) {
            pairs.add(new Pair(neighbour.get(i), neighbourDistance.get(i)));
        }

        Collections.sort(pairs, Comparator.comparingDouble(Pair::getDistance));

        neighbour.clear();
        neighbourDistance.clear();

        for (Pair pair : pairs) {
            neighbour.add(pair.nodeList);
            neighbourDistance.add(pair.distance);
        }
    }

    // Helper class to store pairs of nodeList and distance
    private class Pair {

        LocationNodeList nodeList;
        double distance;

        public Pair(LocationNodeList nodeList, double distance) {
            this.nodeList = nodeList;
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }
    }

// Implementing compareTo method from Comparable interface
    @Override
    public int compareTo(LocationNodeList other) {
        return Double.compare(this.shortestDistFromSun, other.shortestDistFromSun);
    }

    @Override
    public String toString() {
        return "LocationNodeList{" + "city=" + city + '}';
    }

}
