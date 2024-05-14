package com.basketball.cms.controller;

import com.basketball.cms.model.Player;
import com.basketball.cms.service.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

class InjuryUpdateRequest {

    private int playerId;
    private String injuryStatus;

    // Getters and setters
    public int getPlayerId() {
        return playerId;
    }

    public String getInjuryStatus() {
        return injuryStatus;
    }
}

@RestController
@RequestMapping("/players/api")
public class PlayerRestController {

    private static final Logger logger = Logger.getLogger(PlayerRestController.class.getName());

    @Autowired
    private PlayerRepository repo;

    private Stack<Player> injuryStack = new Stack<>();
    private List<Player> bench = new ArrayList<>();

    @GetMapping("/bench")
    public List<Player> getBenchPlayers() {
        bench.clear();
        List<Player> players = repo.findIsAddedPlayers();
        for (Player player : players) {
            boolean isInInjuryStack = injuryStack.stream()
                    .anyMatch(p -> p.getPlayerId() == player.getPlayerId());
            if (!isInInjuryStack) {
                bench.add(player);
            }
        }
        logger.info("Fetching bench players: " + bench);
        return bench;
    }

    @GetMapping("/injuryStack")
    public Stack<Player> getInjuryStack() {
        logger.info("Fetching injury stack: " + injuryStack);
        return injuryStack;
    }

    @PostMapping("/addInjury")
    public ResponseEntity<Void> addInjury(@RequestBody InjuryUpdateRequest request) {
        Player player = repo.findById(request.getPlayerId()).orElse(null);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }

        injuryStack.push(player);
        System.out.println("Added injury for player: " + player);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeInjury")
    public ResponseEntity<Player> removeInjury() {
        if (!injuryStack.isEmpty()) {
            Player player = injuryStack.pop();
            logger.info("Removed injury for player: " + player);
            return ResponseEntity.ok(player);
        } else {
            logger.info("Injury stack is empty, no player to remove");
            return ResponseEntity.noContent().build();
        }
    }
}
