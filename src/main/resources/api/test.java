//template 
//import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.net.*;
// import java.util.*;
// import javax.net.ssl.HttpsURLConnection;

// public class test{

//     public static void main(String[]args)throws IOException, InterruptedException{
//         //connect the URL and open the connection
//         boolean fetch = true;
//         int id = 1;

//         while(fetch){
//         try{
//         PrintWriter out = new PrintWriter(new FileOutputStream("src/testing.txt", true));
//         @SuppressWarnings("deprecation")
//         //URL url = new URL("https://api.balldontlie.io/v1/players/"+id);
//         URL url = new URL("https://docs.balldontlie.io/#season-averages?seasons[]=2024&player_ids[]=115");
//         HttpsURLConnection connection = (HttpsURLConnection) url .openConnection();
//         connection.setRequestMethod("GET");

//          // set API key
//         String apiKey = "cde1b221-fbfc-4118-87f7-3ad69bdc5877";
//         connection.setRequestProperty("Authorization", apiKey);   

//         int responseCode = connection.getResponseCode();
//         System.out.println("Response code : "+ responseCode);

//         if(responseCode == HttpsURLConnection.HTTP_OK){
//             StringBuilder sb = new StringBuilder();
//             Scanner input= new Scanner(connection.getInputStream());
//             while(input.hasNextLine()){
//                 //Thread.sleep(5000); // 5 second delay
//                 sb.append(input.nextLine());
//             }

//             String responseData = sb.toString();
//             if(responseData != null){
//                 System.out.println(responseData);
//                 System.out.println();
//                 out.write(responseData);
//                 out.write("\n");
//                 out.close();
//                 //id++;
//             }
//             else{
//                 System.out.println("No available id : "+id);
//                 break;
//             }
//         }
//         else {
//             System.out.println("Error: HTTP " + responseCode + " " + connection.getResponseMessage());
//             fetch = false; // break the loop on error
//         }
//         connection.disconnect();
//         // introduce a delay to avoid hitting rate limits

//         //out.close();
//         //input.close();
//     }catch(IOException e){
//         e.printStackTrace();
//     }
// }//while loop
// }//main
// }//class

/*
 * -------------------START CODING----------------------
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

public class test {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */

     /*
      * --------------------MAIN CLASS--------------------------------
      */
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean fetch = true;
        int id = 2303;

        while(fetch){
            fetch = getPlayers(id);
            //fetch = getAvg(id);
             if(fetch==true){
               id++;
             }
             else break;}
            
        }// main

/*
 * -------------------------------GET PLAYER--------------------------------------
 */

    public static boolean getPlayers(int id) throws InterruptedException {
        boolean fetch = true; //set true variable
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("src/player_info.csv", true));
            @SuppressWarnings("deprecation")
            //URL url = new URL("https://api.balldontlie.io/v1/season_averages?season=2023&player_ids[]="+id);
            URL url = new URL("https://api.balldontlie.io/v1/players/"+id);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // set API key
            String apiKey = "cde1b221-fbfc-4118-87f7-3ad69bdc5877";
            connection.setRequestProperty("Authorization", apiKey);

            int responseCode = connection.getResponseCode();
            System.out.println("Response code : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                Scanner input = new Scanner(connection.getInputStream());
                while (input.hasNextLine()) {
                    // introduce a delay to avoid hitting rate limits --> BALLDONTLIE 30 REQUEST PER MINUTE
                    Thread.sleep(5000); // 5 second delay
                    sb.append(input.nextLine());
                }

                String responseData = sb.toString();
                if (responseData != null) {
                    System.out.println(responseData);
                    System.out.println();
                    out.write(responseData);
                    out.write("\n");
                    out.close();
                    // id++;
                } else {
                    System.out.println("No available id : " + id);
                    //break;
                }
            } else {
                System.out.println("Error: HTTP " + responseCode + " " + connection.getResponseMessage());
               // fetch = false; // break the loop on error
                fetch =  false;
            }
            
            connection.disconnect();
            

            // out.close();
            // input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fetch;
    }
/*
 * ---------------------------- GET AVG -----------------------------------
 */
    public static boolean getAvg(int id) throws InterruptedException {
        // connect the URL and open the connection
        boolean fetch = true;
        // int id = 1;

        // while(fetch){
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("src/season_avg.csv", true));
            @SuppressWarnings("deprecation")
            // URL url = new URL("https://api.balldontlie.io/v1/players/"+id);
            // https://api.balldontlie.io/v1/stats
            // https://docs.balldontlie.io/season-averages?seasons[]=2024&player_ids[]=115
            // https://api.balldontlie.io/v1/season-averages?seasons[]=2024&player_ids[]=115
            URL url = new URL("https://api.balldontlie.io/v1/season_averages?season=2023&player_ids[]="+id);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //set API key
            String apiKey = "cde1b221-fbfc-4118-87f7-3ad69bdc5877";
            connection.setRequestProperty("Authorization", apiKey);

            int responseCode = connection.getResponseCode();
            System.out.println("Response code : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                Scanner input = new Scanner(connection.getInputStream());
                while (input.hasNextLine()) {
                    // introduce a delay to avoid hitting rate limits
                    Thread.sleep(2000); // 5 second delay
                    sb.append(input.nextLine());
                }

                String responseData = sb.toString();
                System.out.println("baba");
                if (responseData != null) {
                    System.out.println(id);//testing
                    System.out.println(responseData);
                    System.out.println();
                    out.write(responseData);
                    out.write("\n");
                    out.close();
                }
            } else {
                System.out.println("Error: HTTP " + responseCode + " " + connection.getResponseMessage());
                fetch = false; // break the loop on error
            }
            connection.disconnect();
            // input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fetch;
    }
}// class