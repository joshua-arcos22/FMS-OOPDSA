package com.mycompany.lipadbantayoopdsa;

import java.io.*;

public class distanceCalculator {

    // Inputs
    private String departure_airportCode;
    private String arrival_airportCode;
    private String aircraftType;

    // Data extracted from file (Cached in fields)
    private double latitude_1 = 0;
    private double longitude_1 = 0;
    private double latitude_2 = 0;
    private double longitude_2 = 0;
    
    // RESTORED FIELDS FOR YOUR CODE
    private String originName = "Unknown";
    private String destName = "Unknown";
    private String originRunway = "0";
    private String destRunway = "0";
    
    // Constants
    private final String AIRPORT_DB_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt";
    private final String AIRCRAFT_DB_PATH = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";

    // Constructor 1: Used by your addFlight logic (Origin, Dest)
    public distanceCalculator(String departure_airportCode, String arrival_airportCode) {
        this(departure_airportCode, arrival_airportCode, "");
    }

    // Constructor 2: Used by MainFlightDisplay (Origin, Dest, Aircraft)
    public distanceCalculator(String departure_airportCode, String arrival_airportCode, String aircraftType) {
        if (departure_airportCode != null && departure_airportCode.length() >= 4)
            this.departure_airportCode = departure_airportCode.substring(0, 4).toUpperCase();
        else 
            this.departure_airportCode = "";
            
        if (arrival_airportCode != null && arrival_airportCode.length() >= 4)
            this.arrival_airportCode = arrival_airportCode.substring(0, 4).toUpperCase();
        else 
            this.arrival_airportCode = "";

        this.aircraftType = aircraftType;

        // Load all data (Coords, Names, Runway) in one go
        loadAirportData();
    }

    // --- 1. OPTIMIZED FILE READER ---
    private void loadAirportData() {
        File file = new File(AIRPORT_DB_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean foundOrigin = false;
            boolean foundDest = false;

            while ((line = br.readLine()) != null) {
                if (foundOrigin && foundDest) break; // Stop if we found both

                // Fast skip
                if (!line.contains(this.departure_airportCode) && !line.contains(this.arrival_airportCode)) {
                    continue;
                }

                String[] parts = line.split("-");
                if (parts.length < 5) continue;

                // FILE FORMAT: NAME-CODE-RUNWAY-LAT-LON
                // Index:       0    1    2      3   4

                // Capture Origin Data
                if (!foundOrigin && parts[1].equalsIgnoreCase(this.departure_airportCode)) {
                    this.originName = parts[0];
                    this.originRunway = parts[2];
                    this.latitude_1 = parseCoordinate(parts[3]);
                    this.longitude_1 = parseCoordinate(parts[4]);
                    foundOrigin = true;
                }
                // Capture Destination Data
                else if (!foundDest && parts[1].equalsIgnoreCase(this.arrival_airportCode)) {
                    this.destName = parts[0];
                    this.destRunway = parts[2];
                    this.latitude_2 = parseCoordinate(parts[3]);
                    this.longitude_2 = parseCoordinate(parts[4]);
                    foundDest = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading airports: " + e.getMessage());
        }
    }

    // --- 2. RESTORED GETTERS (For your AddFlight Logic) ---
    
    public String getAirportNames() {
        // Returns "Ninoy Aquino-Mactan Cebu"
        return this.originName + "-" + this.destName;
    }

    public String getRunwayLenght() {
        // Returns "3737-3300"
        return this.originRunway + "-" + this.destRunway;
    }

    // --- 3. CALCULATE DISTANCE ---
    public double calculateDistanceKm() {
        if (latitude_1 == 0 || latitude_2 == 0) return 0.0;

        double dLat = Math.toRadians(latitude_2 - latitude_1);
        double dLon = Math.toRadians(longitude_2 - longitude_1);
        double rLat1 = Math.toRadians(latitude_1);
        double rLat2 = Math.toRadians(latitude_2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.pow(Math.sin(dLon / 2), 2) * Math.cos(rLat1) * Math.cos(rLat2);
        
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6371 * c; 
    }

    // --- 4. CALCULATE DURATION ---
    public int calculateFlightDurationMinutes() {
        double distance = calculateDistanceKm();
        if (distance == 0) return 0;

        int speed = getAircraftSpeed();
        if (speed == 0) speed = 800; 

        double durationHours = distance / speed;
        return (int) Math.round(durationHours * 60);
    }

    private int getAircraftSpeed() {
        File file = new File(AIRCRAFT_DB_PATH);
        if (!file.exists()) return 800;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(this.aircraftType)) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception e) { return 800; }
        return 800;
    }

    private double parseCoordinate(String raw) {
        try {
            String[] parts = raw.split("/");
            double deg = Double.parseDouble(parts[0]);
            double min = Double.parseDouble(parts[1]);
            double sec = Double.parseDouble(parts[2]);
            return deg + (min / 60.0) + (sec / 3600.0);
        } catch (Exception e) { return 0.0; }
    }
}