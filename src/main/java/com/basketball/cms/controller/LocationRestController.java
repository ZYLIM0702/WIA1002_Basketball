/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

import com.basketball.cms.model.LocationEdge;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.basketball.cms.service.LocationEdgeRepository;

/**
 *
 * @author szeyu
 */
@RestController
@RequestMapping("/locations")
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
            System.out.println("Graph Object: " + graphObject.toString()); // Log the graph object
            return graphObject;
        }).collect(Collectors.toList());
        System.out.println("Graph Data: " + graphData.toString()); // Log the graph data
        return graphData;
    }
}
