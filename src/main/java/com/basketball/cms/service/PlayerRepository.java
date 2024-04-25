/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.service;

import com.basketball.cms.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author limziyang
 */
public interface PlayerRepository extends JpaRepository<Player, Integer>{


}
