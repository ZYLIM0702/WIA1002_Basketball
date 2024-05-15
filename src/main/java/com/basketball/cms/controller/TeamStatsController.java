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

    public TeamStatsController(TeamStatsService teamStatsService) {
        this.teamStatsService = teamStatsService;
    }

    @GetMapping("/teamStats")
    public Map<String, String> getTeamStats() {
        Map<String, String> stats = new HashMap<>();
        stats.put("teamRanking", teamStatsService.getStat("TEAM RANK"));
        stats.put("ppg", teamStatsService.getStat("PPG"));
        stats.put("rpg", teamStatsService.getStat("RPG"));
        stats.put("apg", teamStatsService.getStat("APG"));
        stats.put("oppg", teamStatsService.getStat("OPPG"));
        return stats;
    }
}
