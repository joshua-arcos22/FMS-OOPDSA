package com.mycompany.lipadbantayoopdsa; 

import java.io.*;
import java.util.Scanner;

public class ArrivalTimetableGenerator {

    private static final String INPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";
    private static final String OUTPUT_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";
    public static void generate() {
        try {
            File checkFile = new File(INPUT_PATH);
            if (!checkFile.exists()) {
                return;
            }

            Scanner fileScanner = new Scanner(checkFile);
            FileWriter writer = new FileWriter(OUTPUT_PATH);

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

                    // 1. Calculate landing time at destination
                    String initialETA = calculateTimePlusMinutes(originalETD, durationMinutes);

                    // 2. Add 10 minutes turnover (New ETD for the return leg)
                    String arrivalETD = calculateTimePlusMinutes(initialETA, 10);

                    // Note: The newLine below now uses 'dest' as the origin 
                    // and 'origin' as the destination to represent the return leg.
                    String newLine = String.format("%s-%s-%s-%s-%s-%s-%s-%s-%s-%s",
                            airline,
                            aircraft,
                            dest, // Switched: Now the starting point
                            origin, // Switched: Now the destination
                            freq,
                            arrivalETD,
                            flightNum,
                            pax,
                            cargo,
                            status);

                    writer.write(newLine + "\n");
                }
            }
            fileScanner.close();
            writer.close();
            System.out.println("Arrival Timetable Generated with switched airports and turnover.");
        } catch (IOException e) {
            e.printStackTrace();
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