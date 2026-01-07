package com.mycompany.lipadbantayoopdsa;

import java.io.*;
import java.util.*;

public class AdminOperations {
    
    // VARIABLES ---------------------------------------------------------------------------------------
    // PATHS FOR THE DATA BASE FILES 
    public static final String Database_Aircarfts_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
    public static final String Database_Airlines_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt"; 
    public static final String Database_Aiports_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt";
    public static final String Database_TimeTable_Departure_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";
    public static final String Database_TimeTable_Arrivals_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";
    public static final String Database_TimeTable_Archive_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Archive_Timetable.txt";
    public static final String Database_Bookings_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/flightBooking/bookings.txt";
    public static final String Database_CancelRequests_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/flightBooking/cancellation_requests.txt";
    public static final String Database_logs_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Logs/logs.txt";
    public static final String Database_UserCredentials_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_credentials.txt";
    public static final String Database_UserProfiles_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_profiles.txt";
            
    private boolean runwayCapable;

    // VARIABLES 
    private String Airline_Name;
    private String Ac_Type;
    private String Origin_Airport;
    private String Destination_Airport;
    private String Frequency;
    private String Time;
    private String Flight_Number;
    private String Flight_Pax;
    private String Flight_Cargo;
    private String Flight_Status;
    
    //  --------------------------------------------------------------------------------------------------       
            
    
    
    // CONSTRUCTORS ---------------------------------------------------------------------------------------  
    
    // Empty Constructor 
    public AdminOperations() {
    }

    
    // Overload Constructor for Setting up the values 
    public AdminOperations(String Airline_Name, String Ac_Type, String Origin_Airport,
                           String Destination_Airport, String Frequency, String Time,
                           String Flight_Number, String Flight_Pax, String Flight_Cargo,
                           String Flight_Status) {
        this.Airline_Name = Airline_Name;
        this.Ac_Type = Ac_Type;
        this.Origin_Airport = Origin_Airport;
        this.Destination_Airport = Destination_Airport;
        this.Frequency = Frequency;
        this.Time = Time;
        this.Flight_Number = Flight_Number;
        this.Flight_Pax = Flight_Pax;
        this.Flight_Cargo = Flight_Cargo;
        this.Flight_Status = Flight_Status;
    }
    
    //  --------------------------------------------------------------------------------------------------
    
    
   
    // FORMAT---------------------------------------------------------------------------------------------
    // SAMPLE FORMAT (AEL-A320-RPLL(Manila)-RPLK(Legazpi)-W/TH-1230-AL1123-3-0-Scheduled) ---------------
    //  --------------------------------------------------------------------------------------------------
    public String addFlightFormat(String Airline_Name, String Ac_Type, String Origin_Airport,
                                   String Destination_Airport, String Frequency, String Time,
                                   String Flight_Number, String Flight_Pax, String Flight_Cargo,
                                   String Flight_Status) {
        
        distanceCalculator calc = new distanceCalculator(Origin_Airport, Destination_Airport, Ac_Type);
        
        try {
            String airportName = calc.getAirportNames();
            String[] names = airportName.split("-"); 
            
            String runway = calc.getRunwayLenght().trim();
            String[] runways = runway.split("-");
            
            AircraftFinder acFinder = new AircraftFinder(Ac_Type);
            runwayCapable = acFinder.runwayLengthVerfiier(
                Integer.parseInt(runways[0]), 
                Integer.parseInt(runways[1])
            );
            
            // Format: Airline-Aircraft-Origin(City)-Dest(City)-Freq-Time-FlightNo-Pax-Cargo-Status
            return Airline_Name + "-" + 
                   Ac_Type + "-" +
                   Origin_Airport.toUpperCase() + 
                    "(" + names[0] + ")-" +
                   Destination_Airport.toUpperCase() + 
                    "(" + names[1] + ")-" +
                   Frequency + "-" + 
                   Time + "-" + 
                   Flight_Number + "-" +
                   Flight_Pax + "-" + 
                   Flight_Cargo + "-" + 
                   Flight_Status;

        } catch (FileNotFoundException e) {
            System.out.println("Database Error: " + e.getMessage());
            return "";
        }
    }
    
    //  --------------------------------------------------------------------------------------------------
    
    
    
    // Adds the entry on the Departure Timetable ---------------------------------------------------------
    public void Admin_AddFlight() throws IOException {
        String flightEntry = addFlightFormat(   Airline_Name, 
                                                Ac_Type, 
                                                Origin_Airport, 
                                                Destination_Airport, 
                                                Frequency, 
                                                Time, 
                                                Flight_Number, 
                                                Flight_Pax, 
                                                Flight_Cargo, 
                                                Flight_Status   );
        
        if (!runwayCapable) {
            System.out.println("Error: Aircraft not runway capable for these airports.");
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Database_TimeTable_Departure_Path, true))) {
            writer.write(flightEntry);
            writer.newLine();
            System.out.println("Success: Flight " + Flight_Number + " added.");
        }
    }

    //  --------------------------------------------------------------------------------------------------
    

    // Edits the entry on the Departure Timetable --------------------------------------------------------
    public void Admin_EditFlight(String originalFlightNum) throws IOException {
        File inputFile = new File(Database_TimeTable_Departure_Path);
        File tempFile = new File("src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/temp_timetable.txt");

        String updatedEntry = addFlightFormat(  Airline_Name, 
                                                Ac_Type, 
                                                Origin_Airport, 
                                                Destination_Airport,
                                                Frequency, 
                                                Time, 
                                                Flight_Number, 
                                                Flight_Pax, 
                                                Flight_Cargo, 
                                                Flight_Status );

        try (Scanner reader = new Scanner(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split("-");
                
                if (parts.length > 6 && parts[6].equalsIgnoreCase(originalFlightNum)) {
                    writer.write(updatedEntry);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        if (inputFile.delete()) {
            if (tempFile.renameTo(inputFile)) {
                System.out.println("Edit Success: File updated.");
                // After editing departure, refresh the arrival timetable
                ArrivalTimetableGenerator.generate();
            }
        }
    }
    //  --------------------------------------------------------------------------------------------------
    
    
}

