package com.mycompany.lipadbantayoopdsa; 

import java.io.*;
import java.util.Scanner;

public class ArrivalTimetableGenerator {

    // UPDATED: Relative paths starting from the Project Root
    // This looks inside: LipadBantayOOPDSA -> src -> main -> java -> ...
    private static final String INPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt";
    private static final String OUTPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";

    public static void generate() {
        System.out.println("--- Starting Arrival Timetable Generation ---");
        try {
            // Check if input file exists using the relative path
            File checkFile = new File(INPUT_PATH);
            
            // (Removed the specific "C:/Users/Joshua..." fallback here so it works for everyone)
            
            if (!checkFile.exists()) {
                // Helpful error message telling the user exactly where it looked
                System.err.println("Error: Departure Timetable file not found at: " + checkFile.getAbsolutePath());
                return;
            }

            Scanner fileScanner = new Scanner(checkFile);
            FileWriter writer = new FileWriter(OUTPUT_PATH);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

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
                    distanceCalculator dc = new distanceCalculator(origin, dest, aircraft);
                    
                    int durationMinutes = dc.calculateFlightDurationMinutes();
                    String arrivalTime = calculateArrivalTime(depTime, durationMinutes);

                    String newLine = String.join("-", 
                        airline, 
                        aircraft, 
                        dest,   
                        origin, 
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