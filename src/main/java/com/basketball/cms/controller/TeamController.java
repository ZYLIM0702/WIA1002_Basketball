/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

import com.basketball.cms.model.Player;
import com.basketball.cms.service.PlayerRepository;
import com.basketball.cms.service.PlayerService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author limziyang
 */
@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private PlayerRepository repo;

    @GetMapping({"", "/"})
    public String teamPlayersSidebar(@RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false, defaultValue = "false") boolean starred,
            @RequestParam(required = false, defaultValue = "name") String sort,
            Model model) {

        List<Player> players;

        players = repo.findAll();
        for (Player player : players) {
            player.setOverallScore();
        }

        // Filter players based on search criteria
        if (name != null && !name.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (position != null && !position.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getPosition().toUpperCase().contains(position.toUpperCase()))
                    .collect(Collectors.toList());
        }

        if (starred == true) {
            players = players.stream()
                    .filter(player -> player.getStarred() == 1)
                    .collect(Collectors.toList());
        }

        // Sort players based on the selected criteria
        switch (sort) {
            case "name":
                players.sort(Comparator.comparing(Player::getName));
                break;
            case "overallScore":
                players.sort(Comparator.comparing(Player::getOverallScore).reversed());
                break;
            case "isStarred":
                players.sort(Comparator.comparing(Player::getIsStarPlayer).reversed());
                break;
            case "salary":
                players.sort(Comparator.comparing(Player::getSalary).reversed());
                break;
            // Add more cases for sorting by other criteria if needed
            default:
                // Default sorting by name
                players.sort(Comparator.comparing(Player::getName));
                break;
        }

        model.addAttribute("players", players);
        return "team/index";
    }
    
        @GetMapping("/final")
    public String teamPlayers(@RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false, defaultValue = "false") boolean starred,
            @RequestParam(required = false, defaultValue = "name") String sort,
            Model model) {

        List<Player> players;

        players = repo.findAll();
        for (Player player : players) {
            player.setOverallScore();
        }

        // Filter players based on search criteria
        if (name != null && !name.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (position != null && !position.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getPosition().toUpperCase().contains(position.toUpperCase()))
                    .collect(Collectors.toList());
        }

        if (starred == true) {
            players = players.stream()
                    .filter(player -> player.getStarred() == 1)
                    .collect(Collectors.toList());
        }

        // Sort players based on the selected criteria
        switch (sort) {
            case "name":
                players.sort(Comparator.comparing(Player::getName));
                break;
            case "overallScore":
                players.sort(Comparator.comparing(Player::getOverallScore).reversed());
                break;
            case "isStarred":
                players.sort(Comparator.comparing(Player::getIsStarPlayer).reversed());
                break;
            case "salary":
                players.sort(Comparator.comparing(Player::getSalary).reversed());
                break;
            // Add more cases for sorting by other criteria if needed
            default:
                // Default sorting by name
                players.sort(Comparator.comparing(Player::getName));
                break;
        }

        model.addAttribute("players", players);
        return "team/final";
    }

    
    @Autowired
    private PlayerService playerService;
    
    @ResponseBody
    @PostMapping("/api/update-position")
    public ResponseEntity<String> updatePlayerPosition(@RequestBody PlayerDropRequest playerDropRequest) {
        try {
            playerService.updatePlayerPosition(playerDropRequest.getPlayerId(), playerDropRequest.getDropZoneId());
            return ResponseEntity.ok("Player position updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update player position: " + e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/api/saved-positions")
    public List<PlayerDropRequest> getSavedPositions() {
        return playerService.getAllPlayerDropRequestPositions();
    }

    @ResponseBody
    @PostMapping("/save")
        public ResponseEntity<String> savePlayers(@RequestBody List<String> playerIds) {
        // Convert player IDs from String to Integer
        List<Integer> playerIntIds = playerIds.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        // Fetch player objects based on player IDs
        List<Player> players = repo.findAllById(playerIntIds);

        // Apply conditions
        // Team Size Regulations
        if (players.size() < 10 || players.size() > 15) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Team must consist of 10 to 15 players.");
        }

        // Positional Requirements
        int guardCount = 0;
        int forwardCount = 0;
        int centerCount = 0;

        for (int i = 0; i < 6; i++) {
            if (playerIntIds.get(i) != 0) {
                guardCount++;
            }
        }
        for (int i = 6; i < 9; i++) {
            if (playerIntIds.get(i) != 0) {
                centerCount++;
            }
        }
        for (int i = 9; i < 15; i++) {
            if (playerIntIds.get(i) != 0) {
                forwardCount++;
            }
        }

        if (guardCount < 2 || centerCount < 2 || forwardCount < 2) {
            System.out.println(guardCount + " " + centerCount + " " + forwardCount);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Positional requirements not met. Each position must have at least 2 players.");
        }

        // Salary Cap Limitations
        double totalSalary = players.stream().mapToDouble(Player::getSalary).sum();
        if (totalSalary > 20000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Team salary cap exceeded. \nWith total salary : " + totalSalary);
        }

        List<Player> allPlayers = repo.findAll();
        for (Player player : allPlayers) {
            player.setIs_added(0);
        }
        repo.saveAll(allPlayers);

        // Update is_added field for each player
        for (Player player : players) {
            player.setIs_added(1);
        }

        // Save updated players
        repo.saveAll(players);
        if (players.size() == 10 && totalSalary > 19000) {
            return ResponseEntity.ok("Players saved successfully. Additional players' salaries must not exceed $1,000.");
        }
        return ResponseEntity.ok("Players saved successfully.");

    }
}
