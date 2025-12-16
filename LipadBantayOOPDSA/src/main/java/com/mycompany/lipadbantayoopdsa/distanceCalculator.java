package com.mycompany.lipadbantayoopdsa; 

import java.io.*;
import java.util.Scanner;

public class distanceCalculator {

    private String departure_airportCode;
    private String arrival_airportCode;
    private String aircraftType;

    private double latitude_1;
    private double longitude_1;
    private double latitude_2;
    private double longitude_2;

    public distanceCalculator(String departure_airportCode, String arrival_airportCode){
        //AirportFinder 
        this.departure_airportCode = departure_airportCode.substring(0,4).toUpperCase();
        this.arrival_airportCode = arrival_airportCode.substring(0,4).toUpperCase();
    }

    
    public distanceCalculator(String departure_airportCode, String arrival_airportCode, String aircraftType){
        // Extract first 4 characters for ICAO code
        this.departure_airportCode = departure_airportCode.substring(0,4).toUpperCase();
        this.arrival_airportCode = arrival_airportCode.substring(0,4).toUpperCase();
        this.aircraftType = aircraftType;
    }

    
    
    public String getRunwayLenght() throws FileNotFoundException{
        String dc_airportMaster = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt"; 
        File aiportFile = new File(dc_airportMaster);
        Scanner airportReader = new Scanner (aiportFile);
        String aiportRunwayDetails = "";
        
        while(airportReader.hasNextLine()){
            String lineReader = airportReader.nextLine();
            String airportDetailsarray[] = lineReader.split("-");
            if (airportDetailsarray[1].equalsIgnoreCase(departure_airportCode)) {
                aiportRunwayDetails = airportDetailsarray[2] + "-";
            }
            
        }
        airportReader = new Scanner (aiportFile);
        while(airportReader.hasNextLine()){
            String lineReader = airportReader.nextLine();
            String airportDetailsarray[] = lineReader.split("-");
            if (airportDetailsarray[1].equalsIgnoreCase(arrival_airportCode)) {
                aiportRunwayDetails = aiportRunwayDetails + airportDetailsarray[2];
            }

        }
        
        return aiportRunwayDetails;
            
    }
    
    
    public String getAirportNames() throws FileNotFoundException{
        String dc_airportMaster = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt"; 
        File aiportFile = new File(dc_airportMaster);
        Scanner airportReader = new Scanner (aiportFile);
        String aiportNameDetails = "";
        
        while(airportReader.hasNextLine()){
            String lineReader = airportReader.nextLine();
            String airportDetailsarray[] = lineReader.split("-");
            if (airportDetailsarray[1].equalsIgnoreCase(departure_airportCode)) {
                aiportNameDetails = airportDetailsarray[0] + "-";
            }
            
        }
        airportReader = new Scanner (aiportFile);
        while(airportReader.hasNextLine()){
            String lineReader = airportReader.nextLine();
            String airportDetailsarray[] = lineReader.split("-");
            if (airportDetailsarray[1].equalsIgnoreCase(arrival_airportCode)) {
                aiportNameDetails = aiportNameDetails + airportDetailsarray[0];
            }

        }
        
        return aiportNameDetails;
            
        
        
    }
    
    
    
    public int calculateFlightDurationMinutes() throws FileNotFoundException {
        // UPDATED: Relative paths starting from the Project Root
        String dc_airportMaster = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt"; 
        String dc_aircraftTypeMaster = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
        
        File dc_airportReader = new File(dc_airportMaster);
        File dc_aircraftReader = new File(dc_aircraftTypeMaster);

        // 1. Get Origin Coordinates
        if (dc_airportReader.exists()) {
            try (Scanner scanner = new Scanner(dc_airportReader)) {
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] parts = line.split("-");
                    if (parts.length > 4 && departure_airportCode.equals(parts[1])){
                        latitude_1 = parseCoordinate(parts[3]);
                        longitude_1 = parseCoordinate(parts[4]);
                        break;
                    }
                }
            }
        } else {
             System.err.println("Error: Airport Database not found at " + dc_airportReader.getAbsolutePath());
        }

        // 2. Get Destination Coordinates
        if (dc_airportReader.exists()) {
            try (Scanner scanner = new Scanner(dc_airportReader)) {
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] parts = line.split("-");
                    if (parts.length > 4 && arrival_airportCode.equals(parts[1])){
                        latitude_2 = parseCoordinate(parts[3]);
                        longitude_2 = parseCoordinate(parts[4]);
                        break;
                    }
                }
            }
        }

        // 3. Calculate Distance (Haversine)
        double dLat = Math.toRadians(latitude_2 - latitude_1);
        double dLon = Math.toRadians(longitude_2 - longitude_1);
        double rLat1 = Math.toRadians(latitude_1);
        double rLat2 = Math.toRadians(latitude_2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(rLat1) *
                        Math.cos(rLat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        double distanceKm = rad * c;

        // 4. Get Speed and Calculate Duration
        int speed = 0;
        if (dc_aircraftReader.exists()) {
            try (Scanner scanner = new Scanner(dc_aircraftReader)) {
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] parts = line.split("-");
                    if (aircraftType.equals(parts[0])){
                        speed = Integer.parseInt(parts[1]); 
                        break;
                    }
                }
            }
        } else {
            System.err.println("Error: Aircraft Database not found at " + dc_aircraftReader.getAbsolutePath());
        }

        if (speed == 0) return 0; 

        double durationHours = distanceKm / speed;
        return (int) Math.round(durationHours * 60); 
    }

    private double parseCoordinate(String raw) {
        String[] parts = raw.split("/");
        double deg = Double.parseDouble(parts[0]);
        double min = Double.parseDouble(parts[1]);
        double sec = Double.parseDouble(parts[2]);
        return deg + (min / 60.0) + (sec / 3600.0);
    }
}