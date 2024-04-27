/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.service;

/**
 *
 * @author limziyang
 */
import com.basketball.cms.controller.PlayerDropRequest;
import com.basketball.cms.model.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public void updatePlayerPosition(int playerId, int dropZoneId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player != null) {
            player.setDroppedZone(dropZoneId);
            playerRepository.save(player);
        }
    }
    public List<PlayerDropRequest> getAllPlayerDropRequestPositions() {
        List<PlayerDropRequest> playerDropRequests = new ArrayList<>();
        List<Player> players = playerRepository.findAll(); // Fetch all players from the database
        for (Player player : players) {
            PlayerDropRequest playerDropRequest = new PlayerDropRequest();
            playerDropRequest.setPlayerId(player.getPlayerId());
            playerDropRequest.setDropZoneId(player.getDroppedZone());
            playerDropRequests.add(playerDropRequest);
        }
        return playerDropRequests;
    }





}
