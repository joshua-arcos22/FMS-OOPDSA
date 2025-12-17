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
    public static String Database_Aircarfts_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
    public static String Database_Airlines_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airlines/Airlines_Master.txt";
    public static String Database_Aiports_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Airports/Airport_Master.txt";
    //private String Database_Routes_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Routes/Aircraft_Master.txt";;
    public static String Database_TimeTable_Departure_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master.txt";
    public static String Database_TimeTable_Arrivals_Path = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Arrival_Timetable_Master.txt";
    private boolean runwayCapable;
    
    
    
    private String Airline_Name;
    private String Ac_Type;
    private String Origin_Airport;
    private String Destination_Airport;
    private String Frequency;
    private String Time;
    private String Flight_Number;
        
        
    public AdminOperations(){
    }
    
    public AdminOperations(    String Airline_Name,
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
   public void Admin_AddFlight() throws IOException {
        
        
        try (FileWriter addFlight = new FileWriter(Database_TimeTable_Departure_Path, true);
             BufferedWriter addFlightEntry = new BufferedWriter(addFlight)) {
            
            String flightEntry = addFlightFormat(Airline_Name, Ac_Type, Origin_Airport, Destination_Airport, Frequency, Time, Flight_Number);
            
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
        distanceCalculator findAirportName = new distanceCalculator(Origin_Airport, Destination_Airport, Ac_Type);
        
        
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
            System.out.println("The file does not exist");
        }
       
        return flightFormat;
        
    }
    
    
    public void Admin_EditFlight(String originalFlightNum) throws IOException {
        File inputFile = new File(Database_TimeTable_Departure_Path);
        File tempFile = new File("temp_timetable.txt");

        try (Scanner reader = new Scanner(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            // Use the NEW data (stored in class variables) to format the new string
            String updatedFlight = addFlightFormat(Airline_Name, Ac_Type, Origin_Airport,
                                                   Destination_Airport, Frequency, Time, Flight_Number);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.contains(originalFlightNum)) {
                    writer.write(updatedFlight);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        // Replace original file with temp file
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file.");
            
            System.gc();
            inputFile.delete();
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file.");
        }

        
    }
    
    
    
    
    
    
    public static void main (String Args[]) throws IOException{
        AdminOperations createFlight = new AdminOperations();
        createFlight.Admin_AddFlight();
    }
    
    
    
}
