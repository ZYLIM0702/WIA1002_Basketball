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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.basketball.cms.service.LocationEdgeRepository;
import com.basketball.cms.service.LocationNodeRepository;
import java.util.Optional;

@Controller
@RequestMapping("/locations")
public class LocationController {

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
        
        


        //model.addAttribute("destination", destLocationNode);
        return "locations/graph";
    }
    

}
