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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
        List<Player> players = repo.findAll(Sort.by(Sort.Direction.ASC, "age"));
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

    @GetMapping("/search")
    public String searchPlayers(@RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double minHeight,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String country,
            @RequestParam(required = false, defaultValue = "false") boolean rank,
            @RequestParam(required = false) Double overallScore,
            @RequestParam(required = false) String rankBy,
            Model model) {

        List<Player> players;

        players = repo.findAll();

        if (name != null && !name.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (minAge != null) {
            players = players.stream()
                    .filter(player -> player.getAge() >= minAge)
                    .collect(Collectors.toList());
        }

        if (maxAge != null) {
            players = players.stream()
                    .filter(player -> player.getAge() <= maxAge)
                    .collect(Collectors.toList());
        }

        if (minHeight != null) {
            players = players.stream()
                    .filter(player -> player.getHeight() >= minHeight)
                    .collect(Collectors.toList());
        }

        if (maxHeight != null) {
            players = players.stream()
                    .filter(player -> player.getHeight() <= maxHeight)
                    .collect(Collectors.toList());
        }

        if (position != null && !position.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getPosition().equalsIgnoreCase(position))
                    .collect(Collectors.toList());
        }

        if (country != null && !country.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getCountry().equalsIgnoreCase(country))
                    .collect(Collectors.toList());
        }

        for (Player player : players) { //calculate overallScore
            player.setOverallScore();
        }

        if (rankBy != null && !rankBy.isEmpty() && rank) {
            players = (List<Player>) players.stream()
                    .sorted((p1, p2) -> {
                        switch (rankBy) {
                            case "rebounds":
                                return Double.compare(p2.getRebounds(), p1.getRebounds());
                            case "blocks":
                                return Double.compare(p2.getBlocks(), p1.getBlocks());
                            case "steals":
                                return Double.compare(p2.getSteals(), p1.getSteals());
                            case "points":
                                return Double.compare(p2.getPoints(), p1.getPoints());
                            case "assists":
                                return Double.compare(p2.getAssists(), p1.getAssists());
                            default:
                                return 0;
                        }
                    }).collect(Collectors.toList());
        } else if (rank) {
            players = players.stream()
                    .sorted(Comparator.comparingDouble(Player::getOverallScore).reversed())
                    .collect(Collectors.toList());
        }

        model.addAttribute("players", players);
        return "players/search";
    }

    @ModelAttribute("allPositions")
    public List<String> populatePositions() {
        List<String> positions = new ArrayList<>();
        positions.add("GUARD");
        positions.add("FORWARD");
        positions.add("CENTER");
        positions.add("GUARD-FORWARD");
        positions.add("CENTER-FORWARD");
        positions.add("FORWARD-CENTER");
        positions.add("FORWARD-GUARD");
        positions.add("GUARD-FORWARD");
        return positions;
    }

    @ModelAttribute()

    @GetMapping("/sort")
//    public String sortPlayersByOverallScore(@RequestParam(required = false, defaultValue = "asc") String order, Model model) {
//        List<Player> players = repo.findAll();
//
//        // Calculate overall score for each player
//        for (Player player : players) {
//            player.setOverallScore();
//        }
//
//        if ("asc".equals(order)) {
//            players = players.stream()
//                    .sorted(Comparator.comparingDouble(Player::getOverallScore))
//                    .collect(Collectors.toList());
//        } else if ("desc".equals(order)) {
//            players = players.stream()
//                    .sorted(Comparator.comparingDouble(Player::getOverallScore).reversed())
//                    .collect(Collectors.toList());
//        }
//
//        model.addAttribute("players", players);
//        model.addAttribute("order", order); // Add order pass the sort order to the view
//        return "players/sort"; 
//    }
    public String sortStarredPlayersByOverallScore(@RequestParam(required = false, defaultValue = "asc") String order, Model model) {
        List<Player> players = repo.findAll();

        //    // Filter and calculate overall score for only starred players
        //    List<Player> starredPlayers = players.stream()
        //            .filter(Player::getIsStarPlayer)
        //            .peek(Player::setOverallScore) // Calculate overall score
        //            .collect(Collectors.toList());
        // Find all added players
        List<Player> addedPlayers = players.stream()
                .filter(player -> player.getIs_added() > 0)
                .collect(Collectors.toList());

        // Calculate overall score for each player
        for (Player player : addedPlayers) {
            player.setOverallScore();
        }
        if ("asc".equals(order)) {
            addedPlayers = addedPlayers.stream()
                    .sorted(Comparator.comparingDouble(Player::getOverallScore))
                    .collect(Collectors.toList());
        } else if ("desc".equals(order)) {
            addedPlayers = addedPlayers.stream()
                    .sorted(Comparator.comparingDouble(Player::getOverallScore).reversed())
                    .collect(Collectors.toList());
        }

        model.addAttribute("players", addedPlayers);
        model.addAttribute("order", order); // Add order to pass the sort order to the view
        return "players/sort";
    }

@GetMapping("/team")
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
            players.sort(Comparator.comparing(Player::getStarred).reversed());
            break;
        // Add more cases for sorting by other criteria if needed
        default:
            // Default sorting by name
            players.sort(Comparator.comparing(Player::getName));
            break;
    }

    model.addAttribute("players", players);
    return "players/team";
}

}
