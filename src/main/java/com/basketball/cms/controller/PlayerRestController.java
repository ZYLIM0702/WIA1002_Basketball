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

@RestController
@RequestMapping("/players/api")
public class PlayerRestController {

    private static final Logger logger = Logger.getLogger(PlayerRestController.class.getName());

    @Autowired
    private PlayerRepository repo;
    
    private Stack<Player> injuryStack = new Stack<>();
    private List<Player> bench = new ArrayList<>();
    private List<Player> inGame = new ArrayList<>();
    
    @GetMapping("/bench")
    public List<Player> getBenchPlayers() {
        List<Player> players = repo.findIsAddedPlayers();
        bench.addAll(players);
        logger.info("Fetching bench players: " + bench);
        return bench;
    }

    @GetMapping("/inGame")
    public List<Player> getInGamePlayers() {
        logger.info("Fetching in-game players: " + inGame);
        return inGame;
    }

    @GetMapping("/injuryStack")
    public Stack<Player> getInjuryStack() {
        logger.info("Fetching injury stack: " + injuryStack);
        return injuryStack;
    }

    @PostMapping("/addInjury")
    public ResponseEntity<Void> addInjury(@RequestBody Player player) {
        injuryStack.push(player);
        bench.remove(player);
        logger.info("Added injury for player: " + player);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeInjury")
    public ResponseEntity<Player> removeInjury() {
        if (!injuryStack.isEmpty()) {
            Player player = injuryStack.pop();
            bench.add(player);
            logger.info("Removed injury for player: " + player);
            return ResponseEntity.ok(player);
        } else {
            logger.info("Injury stack is empty, no player to remove");
            return ResponseEntity.noContent().build();
        }
    }
}