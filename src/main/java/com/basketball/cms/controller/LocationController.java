/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

/**
 *
 * @author szeyu
 */
import com.basketball.cms.model.LocationEdge;
import com.basketball.cms.model.LocationNode;
import com.basketball.cms.model.LocationNodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.basketball.cms.service.LocationEdgeRepository;
import com.basketball.cms.service.LocationNodeRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

@Controller
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationNodeRepository repoNode;

    @Autowired
    private LocationEdgeRepository repoEdge;

    private ArrayList<LocationNodeList> nodeLists = new ArrayList<>();
    private Map<LocationNode, LocationNodeList> nodeMap = new HashMap<>();

    private LocationNode sourceLocationNode;
    public static List<LocationNodeList> shortestPath = new ArrayList<>();
    private double minDistance = Double.MAX_VALUE;

    @PostConstruct
    private void init() {
        sourceLocationNode = repoNode.findById(1).orElse(null);
        System.out.println(sourceLocationNode);
        buildGraph();
    }

    @GetMapping({"", "/"})
    public String showLocationGraph(Model model) {
        shortestPath.clear();
        minDistance = Double.MAX_VALUE;
        nodeLists.clear();
        nodeMap.clear();
        init();

        if (sourceLocationNode == null || nodeMap.get(sourceLocationNode) == null) {
            System.out.println("null node fetched.");
            return "redirect:/locations";
        }

        dfsPathDist(nodeMap.get(sourceLocationNode), new ArrayList<>(), new HashSet<>(), 0.0);

        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + shortestPath);
        System.out.println("Optimal distance: " + minDistance);

        model.addAttribute("path", shortestPath);
        model.addAttribute("optimumDist", minDistance);
        return "locations/index";

    }

    private void buildGraph() {
        List<LocationEdge> nodeEdges = repoEdge.findAll();

        for (LocationEdge nodeEdge : nodeEdges) {
            LocationNode city = nodeEdge.getCity1();
            LocationNode neighbour = nodeEdge.getCity2();
            double distance = nodeEdge.getDistance();

            // Initialize LocationNodeList objects if not already present in the map
            LocationNodeList cityNodeList = nodeMap.computeIfAbsent(city, LocationNodeList::new);
            LocationNodeList neighbourNodeList = nodeMap.computeIfAbsent(neighbour, LocationNodeList::new);

            // Add neighbour and distance to city node list
            cityNodeList.getNeighbour().add(neighbourNodeList);
            cityNodeList.getNeighbourDistance().add(distance);

            // Add city and neighbour to the map
            nodeMap.put(city, cityNodeList);
            nodeMap.put(neighbour, neighbourNodeList);
        }

        // Populate nodeLists from nodeMap
        nodeLists.addAll(nodeMap.values());
        System.out.println("Node list : " + nodeLists);
    }

    @GetMapping("/dfs/path")
    public String showDfsPath(Model model) {
        shortestPath.clear();
        minDistance = Double.MAX_VALUE;
        nodeLists.clear();
        nodeMap.clear();
        init();

        if (sourceLocationNode == null || nodeMap.get(sourceLocationNode) == null) {
            System.out.println("null node fetched.");
            // Handle error if source location not found
            return "redirect:/locations";
        }

        dfsPathDist(nodeMap.get(sourceLocationNode), new ArrayList<>(), new HashSet<>(), 0.0);
        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + shortestPath);
        System.out.println("Optimal distance: " + minDistance);

        model.addAttribute("path", shortestPath);
        model.addAttribute("optimumDist", minDistance);
        return "locations/index";
    }

    private void dfsPathDist(LocationNodeList current, List<LocationNodeList> path, Set<LocationNodeList> visited, double currentDist) {
        visited.add(current);
        path.add(current);

        // Check if all nodes are visited
        if (visited.size() == nodeMap.size()) {
            if (currentDist < minDistance) {
                minDistance = currentDist;
                shortestPath = new ArrayList<>(path); // Copy the path to shortestPath
            }
        } else {
            boolean deadEnd = true;
            for (int i = 0; i < current.getNeighbour().size(); i++) {
                LocationNodeList neighbour = current.getNeighbour().get(i);
                if (!visited.contains(neighbour)) {
                    deadEnd = false; // There's still a way to proceed
                    double distance = current.getNeighbourDistance().get(i);
                    dfsPathDist(neighbour, path, visited, currentDist + distance);
                }
            }

            // If dead end is detected and it's not the end of complete traversal
            if (deadEnd && visited.size() < nodeMap.size()) {
                currentDist = Double.MAX_VALUE; // Invalidate this path by setting distance to max
            }
        }

        // Backtrack
        visited.remove(current);
        path.remove(path.size() - 1);
    }

    @GetMapping("/dijkstra/path/{destinationId}")
    public String showDijkstra(@PathVariable int destinationId, Model model) {
        nodeLists.clear();
        nodeMap.clear();
        init();

        System.out.println("Destination ID : " + destinationId);
        LocationNode destLocationNode = repoNode.findById(destinationId).orElse(null);

        if (sourceLocationNode == null || destLocationNode == null || nodeMap.get(sourceLocationNode) == null
                || nodeMap.get(destLocationNode) == null || destinationId == 1) {
            // Handle error if source location not found
            return "redirect:/locations";
        }

        // Print statements for troubleshooting
        System.out.println("Source Location Node: " + sourceLocationNode.getCityName());
        System.out.println("Destination Location Node: " + destLocationNode.getCityName());

        LocationNodeList source = nodeMap.get(sourceLocationNode);
        LocationNodeList destination = nodeMap.get(destLocationNode);

        calculateShortestPathDijkstra(source);
        shortestPath = getShortestPathDijkstra(destination);
        minDistance = shortestPath.get(shortestPath.size() - 1).getShortestDistFromSun();

        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + shortestPath);
        System.out.println("Optimal distance: " + minDistance);

        model.addAttribute("path", shortestPath);
        model.addAttribute("optimumDist", minDistance);
        return "locations/index";
    }

    public static void calculateShortestPathDijkstra(LocationNodeList source) {
        source.setShortestDistFromSun(0.0);
        PriorityQueue<LocationNodeList> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(source);

        while (!priorityQueue.isEmpty()) {
            LocationNodeList current = priorityQueue.poll();

            for (int i = 0; i < current.getNeighbour().size(); i++) {
                LocationNodeList neighbour = current.getNeighbour().get(i);
                double distanceToNeighbour = current.getNeighbourDistance().get(i);
                double newShortestDist = current.getShortestDistFromSun() + distanceToNeighbour;

                if (newShortestDist < neighbour.getShortestDistFromSun()) {
                    neighbour.setShortestDistFromSun(newShortestDist);
                    neighbour.setParentPath(current);
                    priorityQueue.add(neighbour);
                }
            }
        }
    }

    public static List<LocationNodeList> getShortestPathDijkstra(LocationNodeList destination) {
        List<LocationNodeList> shortestPath = new ArrayList<>();
        for (LocationNodeList node = destination; node != null; node = node.getParentPath()) {
            shortestPath.add(node);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }
}
