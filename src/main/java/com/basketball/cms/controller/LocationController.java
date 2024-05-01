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
    private List<LocationNodeList> path = new ArrayList<>();
    
    private boolean graphBuilt = false;

    @GetMapping({"","/"})
    public String showLocationGraph(Model model) {
        return "locations/index";
    }
    
    @GetMapping("/greedy/path")
    public String showGreedyPath(Model model) {
        LocationNode sourceLocationNode = repoNode.findById(1).orElse(null);
        if(!graphBuilt)
            buildGraph();
        
        if (sourceLocationNode == null) {
            // Handle error if source location not found
            return "redirect:/locations";
        }
        
        double optimumDist = greedyPathDist(nodeMap.get(sourceLocationNode));


        // Print the path and total distance traveled
        System.out.println("Optimal travel path: " + path);
        System.out.println("Optimal distance: " + optimumDist);

        // Add necessary attributes to the model
        model.addAttribute("path", path);
        model.addAttribute("optimumDist", optimumDist);

        return "locations/graph";
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

    private double greedyPathDist(LocationNodeList source) {
        double optimumDist = 0;
        Set<LocationNodeList> visited = new HashSet<>();
        LocationNodeList current = source;

        while (visited.size() != nodeLists.size()) {
            if (!visited.contains(current)) {
                path.add(current);
                visited.add(current);
                
                current.sortNeighboursByDistance();
                ArrayList<LocationNodeList> neighbours = current.getNeighbour();
                                
                for (int i = 0; i < neighbours.size(); i++) {
                    LocationNodeList neighbour = neighbours.get(i);
                    if (visited.contains(neighbour)) {
                        continue;
                    }
                    current = neighbour;
                    optimumDist += neighbour.getNeighbourDistance().get(i);
                    break;
                }
            }
        }
        return optimumDist;
    }
    
    @GetMapping("/dijkstra/path/{destinationId}")
    public String showDijkstra(@PathVariable int destinationId, Model model) {
        if(!graphBuilt)
            buildGraph();
        System.out.println("Destination ID : " + destinationId);
        LocationNode sourLocationNode = repoNode.findById(1).orElse(null);
        LocationNode destLocationNode = repoNode.findById(destinationId).orElse(null);
        
        // Print statements for troubleshooting
        System.out.println("Source Location Node: " + sourLocationNode.getCityName());
        System.out.println("Destination Location Node: " + destLocationNode.getCityName());
        
        if (destLocationNode == null || destinationId == 1) {
            // Handle player not found error  and  destination is same as the source
            return "redirect:/locations";
        }
        
        LocationNodeList source = nodeMap.get(sourLocationNode);
        LocationNodeList destination = nodeMap.get(destLocationNode);
        
        calculateShortestPathDijkstra(source);
        List<LocationNodeList> shortestPath = getShortestPathDijkstra(destination);
        for(LocationNodeList path : shortestPath){
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
