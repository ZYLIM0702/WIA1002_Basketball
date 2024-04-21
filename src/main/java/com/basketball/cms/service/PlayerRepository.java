/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.service;

import com.basketball.cms.model.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author limziyang
 */
public interface PlayerRepository extends JpaRepository<Player, Integer>{

    public List<Player> findByNameContainingIgnoreCase(String name);

    public List<Player> findByHeightGreaterThanEqual(Double minHeight);

    public List<Player> findByAgeLessThanEqual(Integer maxAge);

    public List<Player> findByHeightLessThanEqual(Double maxHeight);

    public List<Player> findByAgeGreaterThanEqual(Integer minAge);

    public List<Player> findByPosition(String position);

}

