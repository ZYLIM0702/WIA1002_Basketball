/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.model;

/**
 *
 * @author szeyu
 */
import jakarta.persistence.*;

@Entity
@Table(name = "STATUS")
public class Status {

    @Id
    @Column(name = "status_id")
    public int statusId;

    @Column(name = "injury_details", columnDefinition = "TEXT")
    private String injuryDetails;

    // Getters and Setters
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getInjuryDetails() {
        return injuryDetails;
    }
}

