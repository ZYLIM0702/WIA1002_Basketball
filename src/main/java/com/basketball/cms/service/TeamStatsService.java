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
            Document doc = Jsoup.connect(url).get();
            Elements statElement = doc.select("div:containsOwn(" + label + ")");
            if (!statElement.isEmpty()) {
                Element valueElement = statElement.first().nextElementSibling();
                if (valueElement != null) {
                    String statValue = valueElement.text().trim();
                    System.out.println(label + ": " + statValue);
                    return statValue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}
