/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms.service;

/**
 *
 * @author kwong
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TeamStatsService {

    public String getStat(String label) {
        String url = "https://www.nba.com/stats/team/1610612759";

        try {
            // Connect to the NBA team stats page and parse the HTML
            Document doc = Jsoup.connect(url).get();
            // Select the element containing the specific label
            Elements statElement = doc.select("div:containsOwn(" + label + ")");
            if (!statElement.isEmpty()) {
                // Get the next sibling element which should contain the value of the stat
                Element valueElement = statElement.first().nextElementSibling();
                if (valueElement != null) {
                    // Extract and return the text value of the statistic
                    String statValue = valueElement.text().trim();
                    System.out.println(label + ": " + statValue);
                    return statValue;
                }
            }
        } catch (IOException e) {
            // Print stack trace if an IOException occurs
            e.printStackTrace();
        }
        // Return "N/A" if the stat is not found or an error occurs
        return "N/A";
    }
    
        public String getRanking() {
        String url = "https://www.nba.com/stats/team/1610612759";
        
        try {
            // Connect to the NBA team stats page and parse the HTML
            Document doc = Jsoup.connect(url).get();
            // Select the element containing the team's record information
            Element recordElement = doc.selectFirst("div.TeamHeader_record__wzofp");
            if (recordElement != null) {
                // Split the text content by "|" to isolate the ranking part
                String[] recordTextParts = recordElement.text().split("\\|");
                if (recordTextParts.length > 1) {
                    // Extract and return the ranking
                    String rankingPart = recordTextParts[1].trim();
                    String ranking = rankingPart.split(" ")[0]; 
                    System.out.println("Ranking: " + ranking);
                    return ranking;
                }
            }
        } catch (IOException e) {
            // Print stack trace if an IOException occurs
            e.printStackTrace();
        }
        // Return "N/A" if the ranking is not found or an error occurs
        return "N/A";
    }

}
