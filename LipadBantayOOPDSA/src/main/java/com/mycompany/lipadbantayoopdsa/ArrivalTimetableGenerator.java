package com.mycompany.lipadbantayoopdsa; 

import java.io.*;
import java.util.Scanner;

// THIS IS THE ARRIVAL TIMETABLE GENERATOR OF THE PROGRAM
public class ArrivalTimetableGenerator {

    
    private static final String INPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";
    private static final String OUTPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";

    public static void generate() {
        System.out.println("Starting Arrival Timetable Generation");
        try {
           // opens the database of the input 
            File checkFile = new File(INPUT_PATH);
            
  
               // error handling for files
            if (!checkFile.exists()) {
                System.err.println("Error: Departure Timetable file not found");
                return;
            }
            
            //reads the input file (depature timetable)
            Scanner fileScanner = new Scanner(checkFile);
            FileWriter writer = new FileWriter(OUTPUT_PATH);

            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()){
                    continue; 
                }
                
                String[] parts = line.split("-");

                if (parts.length >= 7) {
                    String airline = parts[0];
                    String aircraft = parts[1];
                    String origin = parts[2];
                    String dest = parts[3];
                    String freq = parts[4];
                    String depTime = parts[5];
                    String flightNum = parts[6];
                    // assigns the current line's value to each seperate String

                    // CALL THE DISTANCE CALUCLATOR TO DETERMINE THE DISTANCE 
                    distanceCalculator dc = new distanceCalculator(origin, dest, aircraft);
                    
                    // based from the ac typical crusing speed and the distance between the aiports
                    int durationMinutes = dc.calculateFlightDurationMinutes();
                    // calculates the arrival time 
                    String arrivalTime = calculateArrivalTime(depTime, durationMinutes);
                    
                    // formats the string
                    // This is just simply 
                    // airline + "-" + ...
                    String newLine = String.join("-", 
                        airline, 
                        aircraft, 
                        dest,   
                        origin, 
                        freq, 
                        arrivalTime, 
                        flightNum
                    );
                    
                    //Writs the formatted flight to the output path 
                    writer.write(newLine + "\n");
                }
            }

            fileScanner.close();
            writer.close();
            System.out.println("--- Arrival Timetable Generated Successfully ---");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETS THE CURRENT TIME OF DEPARTURE OF A LINE AND THEN ADDS THE DURATION OF THE FLIGHT 
    // THE TIME FORMAT IS ALWAYS ON ZULU OR UTC
    private static String calculateArrivalTime(String depTime, int durationMinutes) {
        try {
            // seperates hours and the minutes 
            int hours = Integer.parseInt(depTime.substring(0, 2));
            int minutes = Integer.parseInt(depTime.substring(2, 4));

            
            int totalMinutes = minutes + durationMinutes;
            int additionalHours = totalMinutes / 60;
            int finalMinutes = totalMinutes % 60;
            int finalHours = (hours + additionalHours) % 24; 
            
            
            // found a way to make a similar method like the printf statement in order to display 2 digits only
            return String.format("%02d%02d", finalHours, finalMinutes);
        } catch (NumberFormatException e) {
            return "0000"; // defaults to 0000z
        }
    }
}