/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

/**
 *
 * @author limziyang
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
import java.util.ArrayList;
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
    private LocationEdgeRepository repoEdge;
    
    @Autowired
    private LocationNodeRepository repoNode;
    
    private ArrayList<LocationNodeList> nodeLists = new ArrayList<>();
    private Map<LocationNode, LocationNodeList> nodeMap = new HashMap<>();
    private List<LocationNodeList> shortestPath = new ArrayList<>();
    private double minDistance = Double.MAX_VALUE;
    private boolean graphBuilt = false;
    

    @GetMapping({"","/"})
    public String showLocationGraph(Model model) {
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
        graphBuilt = true;
        System.out.println("Node list : " + nodeLists);
    }
    
    @GetMapping("/dfs/path")
    public String showDfsPath(Model model) {
        LocationNode sourceLocationNode = repoNode.findById(1).orElse(null);
        if (!graphBuilt) {
            buildGraph();
        }

        if (sourceLocationNode == null) {
            // Handle error if source location not found
            return "locations/index";
        }
        
        shortestPath.clear();
        dfsPathDist(nodeMap.get(sourceLocationNode), new ArrayList<>(), new HashSet<>(), 0.0);
        
        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + shortestPath);
        System.out.println("Optimal distance: " + minDistance);
        
        model.addAttribute("path", shortestPath);
        model.addAttribute("optimumDist", minDistance);
        return "locations/graph";
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
    
    @GetMapping("/greedy/path")
    public String showGreedyPath(Model model) {
        LocationNode sourceLocationNode = repoNode.findById(1).orElse(null);
        if(!graphBuilt)
            buildGraph();
        
        if (sourceLocationNode == null) {
            // Handle error if source location not found
            return "locations/index";
        }
        
        shortestPath.clear();
        greedyPathDist(nodeMap.get(sourceLocationNode));


        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + shortestPath);
        System.out.println("Optimal distance: " + minDistance);

        // Add necessary attributes to the model
        model.addAttribute("path", shortestPath);
        model.addAttribute("optimumDist", minDistance);

        return "locations/graph";
    }

    private void greedyPathDist(LocationNodeList source) {
        minDistance = 0;
        Set<LocationNodeList> visited = new HashSet<>();
        LocationNodeList current = source;

        while (visited.size() != nodeLists.size()) {
            if (!visited.contains(current)) {
                shortestPath.add(current);
                visited.add(current);
                
                current.sortNeighboursByDistance();
                ArrayList<LocationNodeList> neighbours = current.getNeighbour();
                                
                for (int i = 0; i < neighbours.size(); i++) {
                    LocationNodeList neighbour = neighbours.get(i);
                    if (visited.contains(neighbour)) {
                        continue;
                    }
                    current = neighbour;
                    minDistance += neighbour.getNeighbourDistance().get(i);
                    break;
                }
            }
        }
    }
    
    @GetMapping("/dijkstra/path/{destinationId}")
    public String showDijkstra(@PathVariable int destinationId, Model model) {
        if (!graphBuilt) {
            buildGraph();
        }
        
        System.out.println("Destination ID : " + destinationId);
        LocationNode sourLocationNode = repoNode.findById(1).orElse(null);
        LocationNode destLocationNode = repoNode.findById(destinationId).orElse(null);

        if (sourLocationNode == null || destLocationNode == null) {
            // Handle error if source location not found
            return "locations/index";
        }
        
        
        // Print statements for troubleshooting
        System.out.println("Source Location Node: " + sourLocationNode.getCityName());
        System.out.println("Destination Location Node: " + destLocationNode.getCityName());
        
        if (destLocationNode == null || destinationId == 1) {
            // Handle player not found error  and  destination is same as the source
            return "locations/index";
        }
        
        LocationNodeList source = nodeMap.get(sourLocationNode);
        LocationNodeList destination = nodeMap.get(destLocationNode);
        
        calculateShortestPathDijkstra(source);
        List<LocationNodeList> shortestPath = getShortestPathDijkstra(destination);
        
        for (int i = shortestPath.size()-1; i >= 0; i--) {
            LocationNodeList path = shortestPath.get(i);
            System.out.println(path.getCity() + " -> ");
        }

        //model.addAttribute("destination", destLocationNode);
        return "locations/graph";
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
        return shortestPath;
    }
}
