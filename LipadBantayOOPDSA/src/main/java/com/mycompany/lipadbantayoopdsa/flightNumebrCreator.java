//package com.mycompany.lipadbantayoopdsa;
//
//// ---------------------------------
//// TEST PROGRAM DO NO USE 
//// TEST PROGRAM DO NO USE 
//// TEST PROGRAM DO NO USE 
//// TEST PROGRAM DO NO USE 
//// TEST PROGRAM DO NO USE 
//// TEST PROGRAM DO NO USE 
//// ---------------------------------
//
//import java.io.*;
//import java.util.*;
//import java.time.*;
//
//public class flightNumebrCreator {
//
//    // UPDATED: Relative paths starting from the Project Root
//    private final String airlineCodePath = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt";
//    private final String timeTableMaster = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt";
//    private final String datePath = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Date.txt";
//
//    // Constructor 
//    public flightNumebrCreator() {
//    }
//
//    public void fnc_InitializeAllFlightNumbers() throws FileNotFoundException {
//        // --- 1. SETUP (Date) ---
//        LocalDate today = LocalDate.now();
//        long dailySeed = today.toEpochDay();
//
//        // --- 2. LOAD AIRLINE PREFIXES INTO A MAP ---
//        Map<String, String> airlinePrefixMap = new HashMap<>();
//        File airlineCodeMaster = new File(airlineCodePath);
//
//        if (airlineCodeMaster.exists()) {
//            try (Scanner masterReader = new Scanner(airlineCodeMaster)) {
//                while (masterReader.hasNextLine()) {
//                    String line = masterReader.nextLine().trim();
//                    if (!line.isEmpty()) {
//                        String[] parts = line.split("-");
//                        if (parts.length >= 2) {
//                            airlinePrefixMap.put(parts[0].toUpperCase(), parts[1]);
//                        }
//                    }
//                }
//            }
//        } else {
//            System.err.println("Error: Airline Master file not found at " + airlineCodeMaster.getAbsolutePath());
//            return;
//        }
//
//        // --- 3. PROCESS TIMETABLE (Batch Update) ---
//        File timeTableFile = new File(timeTableMaster);
//        ArrayList<String> updatedContent = new ArrayList<>();
//
//        if (timeTableFile.exists()) {
//            try (Scanner timeTableReader = new Scanner(timeTableFile)) {
//                while (timeTableReader.hasNextLine()) {
//                    String currentLine = timeTableReader.nextLine();
//                    
//                    if (currentLine.trim().isEmpty()) {
//                        updatedContent.add(currentLine);
//                        continue;
//                    }
//
//                    String[] parts = currentLine.split("-");
//                    String airlineCode = parts[0].toUpperCase(); 
//
//                    if (airlinePrefixMap.containsKey(airlineCode)) {
//                        String prefix = airlinePrefixMap.get(airlineCode);
//                        
//                        String lastPart = parts[parts.length - 1];
//                        String baseLine = currentLine;
//
//                        // Check if existing ID is present
//                        boolean hasExistingID = lastPart.matches(".*[A-Za-z].*");
//                        
//                        if (hasExistingID) {
//                            int lastDash = currentLine.lastIndexOf("-");
//                            if (lastDash != -1) {
//                                baseLine = currentLine.substring(0, lastDash);
//                            }
//                        }
//
//                        // GENERATE UNIQUE ID
//                        Random flightNumberGenerator = new Random(dailySeed + airlineCode.hashCode() + baseLine.hashCode()); 
//                        
//                        int flightNum = 1000 + flightNumberGenerator.nextInt(9000);
//                        String newFlightCode = prefix + flightNum; 
//
//                        updatedContent.add(baseLine + "-" + newFlightCode);
//                    } else {
//                        updatedContent.add(currentLine);
//                    }
//                }
//            }
//        } else {
//             System.err.println("Error: Timetable file not found at " + timeTableFile.getAbsolutePath());
//             return;
//        }
//
//        // --- 4. WRITE UPDATED CONTENT BACK TO FILE ---
//        try (PrintWriter timeTableWriter = new PrintWriter(timeTableFile)) {
//            for (String line : updatedContent) {
//                timeTableWriter.println(line);
//            }
//        }
//
//        System.out.println("All flight numbers initialized for all airlines.");
//    }
//
//    // --- UTILITY: Check if it's a new day ---
//    public boolean isNewDay() throws FileNotFoundException {
//        File dateFile = new File(datePath);
//        LocalDate today = LocalDate.now();
//
//        if (!dateFile.exists()) {
//            updateDateFile(today);
//            return true;
//        }
//
//        LocalDate lastRunDate = null;
//        try (Scanner dateReader = new Scanner(dateFile)) {
//            if (dateReader.hasNext()) {
//                String dateString = dateReader.next();
//                lastRunDate = LocalDate.parse(dateString);
//            }
//        }
//
//        if (lastRunDate != null && !today.equals(lastRunDate)) {
//            updateDateFile(today);
//            return true;
//        }
//
//        return false;
//    }
//
//    private void updateDateFile(LocalDate today) throws FileNotFoundException {
//        File dateFile = new File(datePath);
//        // Ensure parent directories exist
//        dateFile.getParentFile().mkdirs();
//        
//        try (PrintWriter pw = new PrintWriter(dateFile)) {
//            pw.write(today.toString());
//        }
//    }
//}