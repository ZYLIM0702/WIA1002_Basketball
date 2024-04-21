/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.controller;

/**
 *
 * @author limziyang
 */
import com.basketball.cms.model.LocationNodeList;
import com.basketball.cms.service.LocationRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository repo;
    
    @GetMapping({"","/"})
    public String showLocationGraph(Model model) {
        return "locations/index";
    }
    
    @GetMapping("/path/{destinationId}")
    public String showDijkstra(@PathVariable int destinationId, Model model) {
        LocationNodeList sourLocationNode = repo.findById(1).orElse(null);
        LocationNodeList destLocationNode = repo.findById(destinationId).orElse(null);
        if (destLocationNode == null || sourLocationNode == destLocationNode) {
            // Handle player not found error  and  destination is same as the source
            return "redirect:/locations";
        }
        
        


        //model.addAttribute("destination", destLocationNode);
        return "locations/graph";
    }
    

}
