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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

@Controller
@RequestMapping("/locations")
public class LocationController {
    private Map<LocationNode, LocationNodeList> getNodeLists(){
        List<LocationEdge> nodeEdges = repoEdge.findAll();
        
        Map<LocationNode, LocationNodeList> nodeMap = new HashMap<>();

        for (LocationEdge nodeEdge : nodeEdges) {
            LocationNode city = nodeEdge.getCity1();
            LocationNode neighbour = nodeEdge.getCity2();
            double distance = nodeEdge.getDistance();

            LocationNodeList cityNodeList = nodeMap.getOrDefault(city, new LocationNodeList(city));
            LocationNodeList neighbourNodeList = nodeMap.getOrDefault(neighbour, new LocationNodeList(neighbour));

            // Add neighbour and distance to city node list
            cityNodeList.getNeighbour().add(neighbourNodeList);
            cityNodeList.getNeighbourDistance().add(distance);

            // Add city and neighbour to the map
            nodeMap.put(city, cityNodeList);
            nodeMap.put(neighbour, neighbourNodeList);
        }

        // Populate nodeLists from nodeMap
        return nodeMap;
    }

    @Autowired
    private LocationEdgeRepository repoEdge;
    private LocationNodeRepository repoNode;
    
    @GetMapping({"","/"})
    public String showLocationGraph(Model model) {
        return "locations/index";
    }
    
    @GetMapping("/path/{destinationId}")
    public String showDijkstra(@PathVariable int destinationId, Model model) {
        Optional<LocationNode> sourLocationNode = repoNode.findById(1);
        LocationNode destLocationNode = repoNode.findById(destinationId).orElse(null);
        if (destLocationNode == null || sourLocationNode.equals(destLocationNode)) {
            // Handle player not found error  and  destination is same as the source
            return "redirect:/locations";
        }
        
        ArrayList<LocationNodeList> nodeLists = new ArrayList<>();
        Map<LocationNode, LocationNodeList> nodeMap = getNodeLists();
        nodeLists.addAll(nodeMap.values());
        LocationNodeList source = nodeMap.get(sourLocationNode);
        LocationNodeList destination = nodeMap.get(destLocationNode);
        


        //model.addAttribute("destination", destLocationNode);
        return "locations/graph";
    }
    
    
    public static void calculateShortestPath(LocationNodeList source) {
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

    public static List<LocationNodeList> getShortestPath(LocationNodeList destination) {
        List<LocationNodeList> shortestPath = new ArrayList<>();
        for (LocationNodeList node = destination; node != null; node = node.getParentPath()) {
            shortestPath.add(node);
        }
        return shortestPath;
    }
}
