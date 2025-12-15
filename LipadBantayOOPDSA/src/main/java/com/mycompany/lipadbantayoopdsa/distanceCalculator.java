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

    // Matches your provided constructor
    public distanceCalculator(String departure_airportCode, String arrival_airportCode, String aircraftType){
        // Extract first 4 characters for ICAO code (e.g. "RPVM" from "RPVM(Mactan)")
        this.departure_airportCode = departure_airportCode.substring(0,4).toUpperCase();
        this.arrival_airportCode = arrival_airportCode.substring(0,4).toUpperCase();
        this.aircraftType = aircraftType;
    }

    // MODIFIED: Returns duration in minutes (int) so the other class can use it.
    // Also fixed the math division logic to ensure precision.
    public int calculateFlightDurationMinutes() throws FileNotFoundException {
        // Adjusted paths to match typical flat file execution, or update to "Database/Airports/..." if you prefer
        String dc_airportMaster = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt"; 
        String dc_aircraftTypeMaster = "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
        
        File dc_airportReader = new File(dc_airportMaster);
        File dc_aircraftReader = new File(dc_aircraftTypeMaster);

        // 1. Get Origin Coordinates
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

        // 2. Get Destination Coordinates
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
        try (Scanner scanner = new Scanner(dc_aircraftReader)) {
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split("-");
                if (aircraftType.equals(parts[0])){
                    speed = Integer.parseInt(parts[1]); // Aircraft speed
                    break;
                }
            }
        }

        if (speed == 0) return 0; // Avoid division by zero if aircraft not found

        double durationHours = distanceKm / speed;
        return (int) Math.round(durationHours * 60); // Return total minutes
    }

    // Helper to parse "12/18/39/N" format correctly
    private double parseCoordinate(String raw) {
        String[] parts = raw.split("/");
        double deg = Double.parseDouble(parts[0]);
        double min = Double.parseDouble(parts[1]);
        double sec = Double.parseDouble(parts[2]);
        // Fixed: Use 60.0 and 3600.0 to force double division
        return deg + (min / 60.0) + (sec / 3600.0);
    }
}