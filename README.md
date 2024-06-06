# NBA Team Management System

Welcome to the NBA Team Management System project, a comprehensive basketball team management system developed by the Hokkien Mee Is Red team for the WIA1002 Data Structure course at the Faculty of Computer Science and Information Technology, Universiti Malaya.

## Project Overview

Our project is structured as a web application built using Java 21, Maven, Spring Boot, and Thymeleaf for UI design, integrating with HTML front-end elements. The project is designed to manage a basketball team system and is hosted online for easy access.

###  Hosted Application

The application is hosted on Heroku and can be accessed ***[basketball.planetic.net](https://basketball.planetic.net)***.

## Basic Requirements
- **Dependencies**:
  - Spring Boot Starter Security
  - Spring Boot Starter Thymeleaf
  - Spring Boot Starter Web
  - Thymeleaf Extras Spring Security
  - Spring Boot DevTools
  - MySQL Connector
  - Lombok
  - Jsoup

## Hosting and Accessibility

The application is hosted on a Heroku server, allowing team managers to access the system remotely using mobile devices. The application features two views:
1. **Guest View**: Public pages
2. **Administrator View**: Enhanced functionalities and management tools

## Features

### 1. Build a Championship Contender Team
- **Search Page**: Displays players' essential information. Detailed statistics can be viewed by clicking on a player.
- **Player Profile**: Includes personal information and performance statistics, with an option to edit player details.

### 2. Add Player
- **Team Formation**: Drag and drop players into specific positions while adhering to NBA team composition rules.
- **Validation**: Ensures team meets positional and salary cap requirements before saving.

### 3. Remove Player
- **Player Removal**: Simple click on the cross button to remove a player from the team.

### 4. Dynamic Searching
- **Search Functionality**: Search players using various criteria including name, age range, height range, country code, and position.

### 5. Construct the NBA Cities
- **Graph Representation**: Displays NBA cities linked together using Graph data structure, with data stored in a MySQL database.

### 6. Crafting the Journey
- **Optimal Path**: Uses Depth First Search (DFS) algorithm to find the shortest path between NBA cities, starting from San Antonio.

### 7. Injury Reserve Management
- **Injury Management**: Allows managers to view and manage injured players, with options to specify injury reasons and move players between healthy and injured lists.

### 8. Contract Extension Queue
- **Contract Management**: Manage player contracts with options to renew, remove, or extend contracts. 

### 9. Player Performance Ranking
- **Ranking System**: Players are ranked based on their overall score calculated from season average statistics.

### Extra Features

#### GUI Enhancements
- **Dashboard**: Team Manager Dashboard with navigational buttons.
- **Interactive Elements**: Hover effects, drag and release functionality, and clickable footers for enhanced user experience.

#### Database
- **Setup**: Database managed with phpMyAdmin, storing city distance, contract, location, player, statistics, and status data.

#### NBA API Integration
- **Data Collection**: Uses APIs to collect and process player information and statistics.

#### Fan Engagement
- **Fans' Page**: A separate webpage for fans with team rankings, awards, news, and highlights.

## Contribution Acknowledgement

### Team Members and Contributions
1. **Lim Zi Yang**: Database setup, UI & UX enhancements, application deployment.
2. **Sim Sze Yu**: UML diagrams, graph of locations, shortest path journey, dynamic searching, player performance ranking.
3. **Ooi Kwong Yang**: Team formation page, guest page development, security enhancement.
4. **Lai Min Han**: Dynamic searching, player profile GUI, player performance ranking.
5. **Darrell Lee Wern Han**: Contract extension queue, contract management functionalities.

## Conclusion

This project aimed to create a robust basketball team management system using advanced data structures and frameworks like Spring Boot. Through this project, team members enhanced their technical skills, learned effective teamwork, and developed a comprehensive understanding of software development processes. The project showcases our ability to manage dependencies, develop efficient backends, and implement a wide range of functionalities, providing a valuable learning experience.

---
