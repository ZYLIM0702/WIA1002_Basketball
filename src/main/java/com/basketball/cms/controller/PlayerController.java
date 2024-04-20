/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

/**
 *
 * @author limziyang
 */
import com.basketball.cms.model.Player;
import com.basketball.cms.service.PlayerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerRepository repo;

    @GetMapping({"", "/"})
    public String showPlayerList(Model model) {
        List<Player> players = repo.findAll(Sort.by(Sort.Direction.DESC, "name"));
        model.addAttribute("players", players);
        return "players/index";
    }

    @GetMapping("/edit")
    public String showEditPlayerPage(@RequestParam int playerId, Model model) {
        Player player = repo.findById(playerId).orElse(null);
        if (player == null) {
            // Handle player not found error
            return "redirect:/players";
        }
        model.addAttribute("player", player);
        return "players/edit";
    }

    @PostMapping("/update")
    public String updatePlayer(@ModelAttribute("player") @DateTimeFormat(pattern = "yyyy-MM-dd") Player player) {
        repo.save(player);
        return "redirect:/players";
    }

    @GetMapping("/profile/{playerId}")
    public String showPlayerProfile(@PathVariable int playerId, Model model) {
        Player player = repo.findById(playerId).orElse(null);
        if (player == null) {
            // Handle player not found error
            return "redirect:/players";
        }
        model.addAttribute("player", player);
        return "players/playerProfile";
    }

    @GetMapping("/add")
    public String showAddPlayerForm(Model model) {
        model.addAttribute("player", new Player());
        return "players/addPlayer";
    }

    @PostMapping("/add")
    public String addPlayer(@ModelAttribute("player") Player player) {
        // Perform validation if needed
        repo.save(player);
        return "redirect:/players";
    }
   


}
