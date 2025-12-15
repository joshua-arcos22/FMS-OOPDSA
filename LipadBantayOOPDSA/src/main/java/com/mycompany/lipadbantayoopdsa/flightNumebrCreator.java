package com.mycompany.lipadbantayoopdsa;

import java.io.*;
import java.util.*;
import java.time.*;

public class flightNumebrCreator {

    // Defined file paths
    private final String airlineCodePath = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt";
    private final String timeTableMaster = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt";
    private final String datePath = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Date.txt";

    // Constructor no longer needs specific airline code
    public flightNumebrCreator() {
    }

    public void fnc_InitializeAllFlightNumbers() throws FileNotFoundException {
        // --- 1. SETUP (Date) ---
        LocalDate today = LocalDate.now();
        long dailySeed = today.toEpochDay();

        // --- 2. LOAD AIRLINE PREFIXES INTO A MAP ---
        // key = AirlineName (e.g., PAL), value = Prefix (e.g., PR)
        Map<String, String> airlinePrefixMap = new HashMap<>();
        File airlineCodeMaster = new File(airlineCodePath);

        if (airlineCodeMaster.exists()) {
            try (Scanner masterReader = new Scanner(airlineCodeMaster)) {
                while (masterReader.hasNextLine()) {
                    String line = masterReader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split("-");
                        // Expecting format: AIRLINE-PREFIX (e.g., PAL-PR)
                        if (parts.length >= 2) {
                            airlinePrefixMap.put(parts[0].toUpperCase(), parts[1]);
                        }
                    }
                }
            }
        } else {
            System.out.println("Error: Airline Master file not found at " + airlineCodePath);
            return;
        }

        // --- 3. PROCESS TIMETABLE (Batch Update) ---
        File timeTableFile = new File(timeTableMaster);
        ArrayList<String> updatedContent = new ArrayList<>();

        if (timeTableFile.exists()) {
            try (Scanner timeTableReader = new Scanner(timeTableFile)) {
                while (timeTableReader.hasNextLine()) {
                    String currentLine = timeTableReader.nextLine();
                    
                    // Skip empty lines
                    if (currentLine.trim().isEmpty()) {
                        updatedContent.add(currentLine);
                        continue;
                    }

                    // SPLIT THE LINE
                    String[] parts = currentLine.split("-");
                    String airlineCode = parts[0].toUpperCase(); // First part is the airline (e.g., PAL, AIRASIA)

                    // CHECK: Do we have a prefix for this airline?
                    if (airlinePrefixMap.containsKey(airlineCode)) {
                        String prefix = airlinePrefixMap.get(airlineCode);
                        
                        // Clean up the end of the line (Remove existing Flight IDs if present)
                        String lastPart = parts[parts.length - 1];
                        String baseLine = currentLine;

                        // Check if the last part is a Flight ID (contains letters)
                        // This prevents appending a new ID to a line that already has one
                        boolean hasExistingID = lastPart.matches(".*[A-Za-z].*");
                        
                        if (hasExistingID) {
                            // Find the last dash and cut off the old ID
                            int lastDash = currentLine.lastIndexOf("-");
                            if (lastDash != -1) {
                                baseLine = currentLine.substring(0, lastDash);
                            }
                        }

                        // GENERATE UNIQUE ID
                        // We use the dailySeed + the Airline Code's Hash.
                        // This ensures "PAL" gets different random numbers than "CEBU" even on the same day.
                        Random flightNumberGenerator = new Random(dailySeed + airlineCode.hashCode() + baseLine.hashCode()); 
                        // Added baseLine.hashCode() to ensure different lines for the same airline get different numbers
                        
                        int flightNum = 1000 + flightNumberGenerator.nextInt(9000);
                        String newFlightCode = prefix + flightNum; // e.g., Z2 + 4921

                        updatedContent.add(baseLine + "-" + newFlightCode);
                    } else {
                        // Airline code not in master list? Just keep the line as is.
                        updatedContent.add(currentLine);
                    }
                }
            }
        } else {
             System.out.println("Error: Timetable file not found at " + timeTableMaster);
             return;
        }

        // --- 4. WRITE UPDATED CONTENT BACK TO FILE ---
        try (PrintWriter timeTableWriter = new PrintWriter(timeTableFile)) {
            for (String line : updatedContent) {
                timeTableWriter.println(line);
            }
        }

        System.out.println("All flight numbers initialized for all airlines.");
    }

    // --- UTILITY: Check if it's a new day ---
    public boolean isNewDay() throws FileNotFoundException {
        File dateFile = new File(datePath);
        LocalDate today = LocalDate.now();

        if (!dateFile.exists()) {
            updateDateFile(today);
            return true;
        }

        LocalDate lastRunDate = null;
        try (Scanner dateReader = new Scanner(dateFile)) {
            if (dateReader.hasNext()) {
                String dateString = dateReader.next();
                lastRunDate = LocalDate.parse(dateString);
            }
        }

        if (lastRunDate != null && !today.equals(lastRunDate)) {
            updateDateFile(today);
            return true;
        }

        return false;
    }

    private void updateDateFile(LocalDate today) throws FileNotFoundException {
        File dateFile = new File(datePath);
        // Ensure parent directories exist
        dateFile.getParentFile().mkdirs();
        
        try (PrintWriter pw = new PrintWriter(dateFile)) {
            pw.write(today.toString());
        }
    }
}