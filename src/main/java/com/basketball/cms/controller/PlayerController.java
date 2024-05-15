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
import com.basketball.cms.service.PlayerService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/gameplay")
    public String playerManagementInGame(Model model) {
        List<Player> players = repo.findIsAddedPlayers();
        model.addAttribute("players", players);
        return "players/gameplay";
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
        player.setOverallScore();
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
            @RequestParam(required = false, defaultValue = "false") boolean starred,
            @RequestParam(required = false) Integer injury,
            //@RequestParam(required = false) boolean starred,
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
                    .filter(player -> player.getPosition().contains(position))
                    .collect(Collectors.toList());
        }

        if (country != null && !country.isEmpty()) {
            players = players.stream()
                    .filter(player -> player.getCountry().contains(country))
                    .collect(Collectors.toList());
        }

        if (injury != null) {
            players = players.stream()
                    .filter(player -> injury == player.getStatusId())
                    .collect(Collectors.toList());
        }

        if (starred == true) {
            players = players.stream()
                    .filter(player -> player.getStarred() == 1)
                    .collect(Collectors.toList());
        }

        for (Player player : players) { //calculate overallScore
            player.setOverallScore();
        }

        //Rank By Selection & Rank checkbox 
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

//        if (starred) { //not done yet cannot 
//    players = players.stream()
//                    .filter(Player::getStarred)
//                    .collect(Collectors.toList());
//}
//
        model.addAttribute("players", players);
        return "players/search";
    }

    @ModelAttribute("allPositions")
    public List<String> populatePositions() {
        List<String> positions = new ArrayList<>(); //later need to edit the positions 
        positions.add("GUARD");
        positions.add("FORWARD");
        positions.add("CENTER");
        //positions.add("GUARD-FORWARD");
        //positions.add("CENTER-FORWARD");
//        positions.add("FORWARD-CENTER");
//        positions.add("FORWARD-GUARD");
//        positions.add("GUARD-FORWARD");
        return positions;
    }

    @ModelAttribute("allCountries")
    public List<String> allCountries() {
        String[] cc = {"AT", "AU", "BS", "CA", "CD", "CH", "CM", "DE", "DO", "FI", "FR", "GB", "GR", "HR", "IT", "JP", "LC", "LT", "LV", "ME", "NG", "RS", "SI", "SS", "TR", "UA", "US"};
        //List<String>countries = new ArrayList<>(Arrays.asList(cc));
        List<String> countries = new ArrayList<>();
        for (int i = 0; i < cc.length; i++) {
            countries.add(cc[i]);
        }
        return countries;
    }

    @ModelAttribute("allInjuries")
    public List<Integer> allInjuries() {
        //String[]injury = {"0 - No Injuries", "1 - Ankle Sprains", "2 - Facial Cuts", "3 - Knee Injuries", "4 - Jammed Fingers", "5 - Calf Strains / Achilles Tears", "6 - Thigh Bruises"};
        Integer[] injury = {0, 1, 2, 3, 4, 5, 6};
        //List<String>countries = new ArrayList<>(Arrays.asList(cc));
        List<Integer> injuries = new ArrayList<>();
        for (int i = 0; i < injury.length; i++) {
            injuries.add(injury[i]);
        }
        return injuries;
    }

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
    public String sortStarredPlayersByOverallScore(@RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            Model model) {
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
        Comparator<Player> comparator;
        switch (sortBy) {
            case "overallScore":
                comparator = Comparator.comparingDouble(Player::getOverallScore);
                break;
            case "height":
                comparator = Comparator.comparingDouble(Player::getHeight);
                break;
            case "age":
                comparator = Comparator.comparingInt(Player::getAge);
                break;
            case "salary":
                comparator = Comparator.comparingDouble(Player::getSalary);
                break;
            case "name":
            default:
                comparator = Comparator.comparing(Player::getName);
                break;
        }

        if ("desc".equals(order)) {
            comparator = comparator.reversed();
        }

        addedPlayers.sort(comparator);

        model.addAttribute("players", addedPlayers);
        model.addAttribute("order", order);
        model.addAttribute("sortBy", sortBy);

        return "players/sort";
    }
//        if ("asc".equals(order)) {
//            addedPlayers = addedPlayers.stream()
//                    .sorted(Comparator.comparingDouble(Player::getOverallScore))
//                    .collect(Collectors.toList());
//        } else if ("desc".equals(order)) {
//            addedPlayers = addedPlayers.stream()
//                    .sorted(Comparator.comparingDouble(Player::getOverallScore).reversed())
//                    .collect(Collectors.toList());
//        }
//
//        model.addAttribute("players", addedPlayers);
//        model.addAttribute("order", order); // Add order to pass the sort order to the view
//        return "players/sort";
//    }



    @GetMapping("/contract")
    public String sortStarredPlayersByPriority(@RequestParam(required = false, defaultValue = "asc") String order, Model model) {
        List<Player> players = repo.findAll();
        List<Player> addedPlayers = players.stream()
                .filter(player -> player.getIs_added() > 0)
                .collect(Collectors.toList());

        List<Player> unaddedPlayers = players.stream()
                .filter(player -> player.getIs_added() == 0)
                .collect(Collectors.toList());

        // Calculate contract status for each player
        for (Player player : addedPlayers) {
            LocalDate currentDate = LocalDate.now();
            LocalDate expirationDate = player.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusYears(3);
            Date dateCreated = player.getDateCreated();

            String contractStatus;
            if (isMinDate(dateCreated)) {
                contractStatus = "No Contract";
            } else if (expirationDate.isBefore(currentDate)) {
                contractStatus = "Expired";
            } else if (currentDate.plusMonths(3).isAfter(expirationDate)) {
                contractStatus = "Almost Expired";
            } else {
                contractStatus = "Active";
            }

            player.setContractStatus(contractStatus);
        }

        // Create a priority queue that sorts players based on isStarPlayer and dateCreated
        Queue<Player> priorityQueue = new PriorityQueue<>(Comparator.comparing(Player::getDateCreated, Comparator.reverseOrder()).thenComparing(Player::getIsStarPlayer));
        // Add players to the priority queue
        for (Player player : addedPlayers) {
            priorityQueue.add(player);
        }

        // Convert the priority queue back to a list
        List<Player> priorityPlayers = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            priorityPlayers.add(priorityQueue.poll());
        }

        if ("asc".equals(order)) {
            Collections.sort(priorityPlayers, Comparator.comparing(Player::getDateCreated, Comparator.reverseOrder()).thenComparing(Player::getIsStarPlayer));
        } else if ("desc".equals(order)) {
            Collections.sort(priorityPlayers, Comparator.comparing(Player::getDateCreated).thenComparing(Player::getIsStarPlayer));

        }

        model.addAttribute("players", priorityPlayers);
        model.addAttribute("unaddedPlayers", unaddedPlayers);
        model.addAttribute("order", order); // Add order to pass the sort order to the view
        return "players/contract";
    }

    private boolean isMinDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) == 1 && cal.get(Calendar.MONTH) == Calendar.JANUARY && cal.get(Calendar.DAY_OF_MONTH) == 1;
    }

    @PostMapping("/contract/save")
    public String saveContract(@RequestParam("playerId") Integer playerId,
            @RequestParam("contractStatus") String contractStatus,
            @RequestParam(value = "dateCreated") String dateCreated,
            Model model) {
        Date parsedDate;
        try {
            parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateCreated);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            // You might want to return an error response here if parsing fails
            return "error";
        }

        // Retrieve player from the database
        Optional<Player> playerOptional = repo.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setDateCreated(parsedDate);
            repo.save(player);

        }

        // Redirect back to the contract page
        return "redirect:/players/contract";
    }

    @PostMapping("/contract/remove")
    public String removeContract(@RequestParam("playerId") Integer playerId, Model model) {
        // Retrieve player from the database
        Optional<Player> playerOptional = repo.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setDateCreated(new Date(-1900, Calendar.JANUARY, 1));; // Setting dateCreated to "0001-01-01"
            String contractStatus;
            contractStatus = "Removed";
            player.setContractStatus(contractStatus);
            repo.save(player);
        }

        // Redirect back to the contract page
        return "redirect:/players/contract";
    }
    @ResponseBody
    @PostMapping("/toggleStar")
    public ResponseEntity<Object> toggleStar(@RequestParam("playerId") int playerId) {
        Player player = repo.findById(playerId).orElse(null);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        // Toggle the starred attribute
        player.setStarred(player.getStarred() == 1 ? 0 : 1);
        repo.save(player);
        return ResponseEntity.ok().build();
    }


}
