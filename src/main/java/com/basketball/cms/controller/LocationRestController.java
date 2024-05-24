/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

import com.basketball.cms.model.LocationEdge;
import com.basketball.cms.model.LocationNodeList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.basketball.cms.service.LocationEdgeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author szeyu
 */


@RestController
@RequestMapping("/locations/api")
public class LocationRestController {
    @Autowired
    private LocationEdgeRepository repo;
    
    @GetMapping("/graph-data")
    public List<Object> getGraphData() {
        
        List<LocationEdge> nodes = repo.findAll();
        List<Object> graphData = nodes.stream().map(node -> {
            Object graphObject = new Object(){
                public final String source = node.getCity1().getCityName();
                public final String target = node.getCity2().getCityName();
                public final double distance = node.getDistance();
                
                public String toString(){
                    return source + " -> " + target + " : " + distance;
                }
            };
            //System.out.println("Graph Object: " + graphObject.toString()); // Log the graph object
            return graphObject;
        }).collect(Collectors.toList());
        System.out.println("Graph Data: " + graphData.toString()); // Log the graph data
        return graphData;
    }
    
    @GetMapping("/path-data")
    public ResponseEntity<String> getShortestPathData() {
        System.out.println("path-data");
        // get the shortest path from calculated before
        List<LocationNodeList> shortestPath = LocationController.shortestPath;
        System.out.println("shortest path : " + shortestPath);
        if (shortestPath == null || shortestPath.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Prepare nodes and links for D3.js
        List<Map<String, String>> links = new ArrayList<>();
        List<String> nodes = new ArrayList<>();

        LocationNodeList previousNode = null;
        for (LocationNodeList currentNode : shortestPath) {
            if (previousNode != null) {
                Map<String, String> link = new HashMap<>();
                link.put("source", previousNode.getCity().getCityName());
                link.put("target", currentNode.getCity().getCityName());
                links.add(link);
            }
            nodes.add(currentNode.getCity().getCityName());
            previousNode = currentNode;
        }

        Map<String, Object> graphData = new HashMap<>();
        graphData.put("nodes", nodes.stream().distinct().collect(Collectors.toList()));
        graphData.put("links", links);

        // Serialize the graphData to JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonData;
        try {
            jsonData = mapper.writeValueAsString(graphData);
            System.out.println(jsonData); // Output the JSON data
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error output json");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON data");
        }

        return ResponseEntity.ok(jsonData);
    }

}
