/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa;


import java.io.*;
import java.util.*;


/**
 *
 * @author Joshua
 */
public class AdminOperations {
    
    //PATHSS FOR THE DATA BASE FILES 
    public static String Database_Aircarfts_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
    public static String Database_Airlines_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt";  // lack of time did not use
    public static String Database_Aiports_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt";
    //private String Database_Routes_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Routes/Aircraft_Master.txt"; // lack of time did not use
    public static String Database_TimeTable_Departure_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";
    public static String Database_TimeTable_Arrivals_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";
    private boolean runwayCapable;
    
    
    //VARIABLES 
    private String Airline_Name;
    private String Ac_Type;
    private String Origin_Airport;
    private String Destination_Airport;
    private String Frequency;
    private String Time;
    private String Flight_Number;
        
      // OVERLOAD CONSTURCTOR TO CALL METHODS
    public AdminOperations(){
    }
    
    
    // ASSIGNING ALL OF THE FLIGHT DETAILS 
    public AdminOperations(     String Airline_Name,
                                String Ac_Type,
                                String Origin_Airport,
                                String Destination_Airport,
                                String Frequency,
                                String Time,
                                String Flight_Number){
        this.Airline_Name = Airline_Name;
        this.Ac_Type = Ac_Type;
        this.Origin_Airport = Origin_Airport;
        this.Destination_Airport = Destination_Airport;
        this.Frequency = Frequency;
        this.Time = Time;
        this.Flight_Number = Flight_Number;
    }

    
    // methods 
    //ADD FLIGHT 
   public void Admin_AddFlight() throws IOException {
        
        
       // writes on file 
        try (FileWriter addFlight = new FileWriter(Database_TimeTable_Departure_Path, true);
             BufferedWriter addFlightEntry = new BufferedWriter(addFlight)) {
            
            // Formats the data in the constructor call ino the format same as teh data base depature_time_table
            //  airlinename-actype-origin-destination-frequency-time-flno
            String flightEntry = addFlightFormat(Airline_Name, Ac_Type, Origin_Airport, Destination_Airport, Frequency, Time, Flight_Number);
            
            
            // if aiport runway length < ac runway length capability then it is not possible to land
            if (!runwayCapable) {
                System.out.println("Ac not runway capable");
                return;
            }
            
            //Adds the formatted line to the database
            addFlightEntry.write(flightEntry);
            addFlightEntry.newLine();
            
            // succes print 
            System.out.println("Success: written to file.");
            
        } catch (IOException e) {
            //error print
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    
    //adds flight format 
    public String addFlightFormat ( String Airline_Name,
                                    String Ac_Type,
                                    String Origin_Airport,
                                    String Destination_Airport,
                                    String Frequency,
                                    String Time,
                                    String Flight_Number){
        
        String flightFormat = "";
        
        //Calls distance calculator class in order to determine the distance between 2 points
        //this used the haversine formula
        distanceCalculator findAirportName = new distanceCalculator(Origin_Airport, Destination_Airport, Ac_Type);
        
        
        try {
            // FINDS THE NAME OF ORIGIN AND DESTINATION AIRPORT 
            // findign airport name 
            String aiportName = findAirportName.getAirportNames();
            String aiportNameSeperator[] = aiportName.split("-"); // seperate them
            // airport data base has alos the same format, entries have seperation of "-"
            
            
            //Finding if it s runway capable
            String airprotRunwayLenght = findAirportName.getRunwayLenght().trim();
            String airprotRunwayLenghtSeperator[] = airprotRunwayLenght.split("-");
            
            
            // CALLS THE AC AIRPORT VALIDATOR 
            AircraftFinder ac_airport_validator = new AircraftFinder(Ac_Type);
            runwayCapable = ac_airport_validator.runwayLengthVerfiier(Integer.parseInt(airprotRunwayLenghtSeperator[0]), Integer.parseInt(airprotRunwayLenghtSeperator[1]));
            // determines if aircrft is capable of landing or taking off on aiport
            
            // format for thereturn statement of the flight to be addedo n the master file 
               flightFormat =   Airline_Name + "-" +
                                Ac_Type + "-" + 
                                Origin_Airport.toUpperCase() + "(" +
                                aiportNameSeperator[0] + ")" + "-" +
                                Destination_Airport.toUpperCase()  + "(" +
                                aiportNameSeperator[1] + ")" + "-" +
                                Frequency + "-" +
                                Time + "-" +
                                Flight_Number;

               
               
               
        } catch (FileNotFoundException e) {
            System.out.println("The file does not exist");
        }
        // returns the flight format  
        return flightFormat;
        
    }
    
    
    //editing flight, the key is the flightNumber in order to determine which one is updated or not
    public void Admin_EditFlight(String originalFlightNum) throws IOException {
        //FILE HANDLINGS
        File inputFile = new File(Database_TimeTable_Departure_Path);
        File tempFile = new File("temp_timetable.txt");

        
        // reads and writes the departure timetable 
        try (Scanner reader = new Scanner(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            // Use the new data to format the new string
            String updatedFlight = addFlightFormat(Airline_Name, Ac_Type, Origin_Airport,
                                                   Destination_Airport, Frequency, Time, Flight_Number);
            
            //reads line from the departure 
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                
                // if still old flno then write the updated format
                if (line.contains(originalFlightNum)) {
                    writer.write(updatedFlight);
                } else {
                    //just writes the same line 
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        // Replace original file with temp file
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file.");

        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file.");
        }

        
    }
    
    
    
    
    
    
    public static void main (String Args[]) throws IOException{
        // main methods for creating flights
        AdminOperations createFlight = new AdminOperations();
        createFlight.Admin_AddFlight();
    }
    
    
    
}
