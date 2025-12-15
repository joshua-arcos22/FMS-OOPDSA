package com.mycompany.lipadbantayoopdsa; // Make sure package matches your project

import java.io.*;
import java.util.Scanner;

public class ArrivalTimetableGenerator {

    // You can update these paths to match your absolute file paths if needed
    private static final String INPUT_PATH = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt";
    private static final String OUTPUT_PATH = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";

    // This is the method you will call from flightCreator
    public static void generate() {
        System.out.println("--- Starting Arrival Timetable Generation ---");
        try {
            // Check if input file exists before proceeding
            File checkFile = new File(INPUT_PATH);
            if (!checkFile.exists()) {
                // Fallback for the absolute path found in your code snippet if relative fails
                // You can edit this path to match your exact computer setup
                 checkFile = new File("C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt");
            }
            
            if (!checkFile.exists()) {
                System.err.println("Error: Departure Timetable file not found.");
                return;
            }

            Scanner fileScanner = new Scanner(checkFile);
            FileWriter writer = new FileWriter(OUTPUT_PATH);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Format: AIRLINE-AIRCRAFT-ORIGIN-DEST-FREQ-TIME-FLIGHTNUM
                String[] parts = line.split("-");

                if (parts.length >= 7) {
                    String airline = parts[0];
                    String aircraft = parts[1];
                    String origin = parts[2];
                    String dest = parts[3];
                    String freq = parts[4];
                    String depTime = parts[5];
                    String flightNum = parts[6];

                    // --- CALLING YOUR PROVIDED CLASS ---
                    // using the constructor from distanceCalculator.java
                    distanceCalculator dc = new distanceCalculator(origin, dest, aircraft);
                    
                    // using the method to get minutes
                    int durationMinutes = dc.calculateFlightDurationMinutes();

                    // Calculate Arrival Time (HHmm)
                    String arrivalTime = calculateArrivalTime(depTime, durationMinutes);

                    // Swap Origin/Dest and write the new line
                    String newLine = String.join("-", 
                        airline, 
                        aircraft, 
                        dest,   // Swapped
                        origin, // Swapped
                        freq, 
                        arrivalTime, 
                        flightNum
                    );

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

    // Helper method
    private static String calculateArrivalTime(String depTime, int durationMinutes) {
        try {
            int hours = Integer.parseInt(depTime.substring(0, 2));
            int minutes = Integer.parseInt(depTime.substring(2, 4));

            int totalMinutes = minutes + durationMinutes;
            int additionalHours = totalMinutes / 60;
            int finalMinutes = totalMinutes % 60;
            int finalHours = (hours + additionalHours) % 24; 

            return String.format("%02d%02d", finalHours, finalMinutes);
        } catch (NumberFormatException e) {
            return "0000"; 
        }
    }
}