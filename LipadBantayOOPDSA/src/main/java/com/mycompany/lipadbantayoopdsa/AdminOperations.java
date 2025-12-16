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
    private String Database_Aircarfts_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";;
    private String Database_Airlines_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt";;
    private String Database_Aiports_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airports_Master.txt";;
    //private String Database_Routes_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Routes/Aircraft_Master.txt";;
    private String Database_TimeTable_Destination_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";;
    private String Database_TimeTable_Arrivals_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";;
    private boolean runwayCapable;
    
    AdminOperations(){
        
    }
    
    // methods 
   public void Admin_AddFlight() throws IOException {
        
        
        try (FileWriter addFlight = new FileWriter(Database_TimeTable_Destination_Path, true);
             BufferedWriter addFlightEntry = new BufferedWriter(addFlight)) {
            
            String flightEntry = addFlightFormat("CEB", "B777", "RPLK", "RPLL", "E", "1100", "5J6767");
            
            if (!runwayCapable) {
                System.out.println("Ac not runway capable");
                return;
            }
            
            addFlightEntry.write(flightEntry);
            addFlightEntry.newLine();
            
            System.out.println("Success: written to file.");
            
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    
    
    public String addFlightFormat ( String Airline_Name,
                                    String Ac_Type,
                                    String Origin_Airport,
                                    String Destination_Airport,
                                    String Frequency,
                                    String Time,
                                    String Flight_Number){
        
        String flightFormat = "";
        
        //method constructor
        distanceCalculator findAirportName = new distanceCalculator(Origin_Airport, Destination_Airport);
        
        
        try {
            // findign airport name 
            String aiportName = findAirportName.getAirportNames();
            String aiportNameSeperator[] = aiportName.split("-");
            
            
            //Finding if it s runway capable
            String airprotRunwayLenght = findAirportName.getRunwayLenght().trim();
            String airprotRunwayLenghtSeperator[] = airprotRunwayLenght.split("-");
            
            AircraftFinder ac_airport_validator = new AircraftFinder(Ac_Type);
            runwayCapable = ac_airport_validator.runwayLengthVerfiier(Integer.parseInt(airprotRunwayLenghtSeperator[0]), Integer.parseInt(airprotRunwayLenghtSeperator[1]));
            
            
            
            
            
            
            
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
            
        }
       
        return flightFormat;
        
    }
    
    
    
    
    public static void main (String Args[]) throws IOException{
        AdminOperations createFlight = new AdminOperations();
        createFlight.Admin_AddFlight();
    }
    
    
    
}
