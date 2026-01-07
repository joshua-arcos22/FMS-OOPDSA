package com.mycompany.lipadbantayoopdsa; 

import java.io.*;
import java.util.Scanner;

public class ArrivalTimetableGenerator {
    
    // Get inputs from the Departure then process it and then generates the Arrival
    public static void generate() {
        try {
            
            
            File checkFile = new File(AdminOperations.Database_TimeTable_Departure_Path);
            if (!checkFile.exists()) {
                return;
            }

            Scanner fileScanner = new Scanner(checkFile);
            FileWriter Arrivalwriter = new FileWriter(AdminOperations.Database_TimeTable_Arrivals_Path);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("-");

                if (parts.length >= 10) {
                    String airline = parts[0];
                    String aircraft = parts[1];
                    String origin = parts[2];
                    String dest = parts[3];
                    String freq = parts[4];
                    String originalETD = parts[5];
                    String flightNum = parts[6];
                    String pax = parts[7];
                    String cargo = parts[8];
                    String status = parts[9];

                    distanceCalculator dc = new distanceCalculator(origin, dest, aircraft);
                    int durationMinutes = dc.calculateFlightDurationMinutes();

                    String initialETA = calculateTimePlusMinutes(originalETD, durationMinutes);
                    String arrivalETD = calculateTimePlusMinutes(initialETA, 10);

                    String newLine =
                            airline + "-" + 
                            aircraft + "-" +
                            dest + "-" +
                            origin + "-" +
                            freq + "-" +
                            arrivalETD + "-" +
                            flightNum + "-" +
                            pax + "-" +
                            cargo + "-" +
                            status;

                    Arrivalwriter.write(newLine + "\n");
                }
            }
            fileScanner.close();
            Arrivalwriter.close();
            System.out.println("Arrival Timetable Generated with switched airports and turnover.");
            
        } catch (IOException e) {
            System.out.println("Error in generating ");;
        }
    }

    
    
    private static String calculateTimePlusMinutes(String startTime, int minutesToAdd) {
        try {
            int hours = Integer.parseInt(startTime.substring(0, 2));
            int minutes = Integer.parseInt(startTime.substring(2, 4));

            int totalMinutes = minutes + minutesToAdd;
            int additionalHours = totalMinutes / 60;
            int finalMinutes = totalMinutes % 60;
            int finalHours = (hours + additionalHours) % 24; 
            
            return String.format("%02d%02d", finalHours, finalMinutes);
        } catch (Exception e) {
            return "0000"; 
        }
    }
}