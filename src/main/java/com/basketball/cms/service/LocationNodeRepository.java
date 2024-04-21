/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.basketball.cms.service;

import com.basketball.cms.model.LocationNode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author szeyu
 */
public interface LocationNodeRepository extends JpaRepository<LocationNode, Integer>{
    
}
