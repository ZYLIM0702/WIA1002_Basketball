/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

/**
 *
 * @author kwong
 */

import com.basketball.cms.service.TeamStatsService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TeamStatsController {

    private final TeamStatsService teamStatsService;
    
    //Constructor for TeamStatsController
    public TeamStatsController(TeamStatsService teamStatsService) {
        this.teamStatsService = teamStatsService;
    }

    @GetMapping("/teamStats")
    public Map<String, String> getTeamStats() {
        Map<String, String> stats = new HashMap<>();
        // Fetch and add team ranking to the stats map
        stats.put("teamRanking", teamStatsService.getRanking());
        // Fetch and add points per game to the stats map
        stats.put("ppg", teamStatsService.getStat("PPG"));
        // Fetch and add rebounds per game to the stats map
        stats.put("rpg", teamStatsService.getStat("RPG"));
        // Fetch and add assists per game to the stats map
        stats.put("apg", teamStatsService.getStat("APG"));
        // Fetch and add opponent points per game to the stats map
        stats.put("oppg", teamStatsService.getStat("OPPG"));
        return stats;
    }
}
