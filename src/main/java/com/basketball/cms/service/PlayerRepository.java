/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.service;

import com.basketball.cms.controller.PlayerDropRequest;
import com.basketball.cms.model.Player;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 *
 * @author limziyang
 */
public interface PlayerRepository extends JpaRepository<Player, Integer>{
    @Query("SELECT p FROM Player p WHERE p.is_added = 1")
    List<Player> findIsAddedPlayers();
}
