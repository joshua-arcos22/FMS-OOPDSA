package com.mycompany.lipadbantayoopdsa;
import java.io.*;
import java.util.*;;

public class AircraftFinder {
    private String aircraft_Type;
    
    // due to the lack of time , unable to implement the number of pax and cargo 
    private int aircraft_MaxPax;
    private int aircraft_MaxCargo;
    
    AircraftFinder(String aircraft_Type){
        this.aircraft_Type = aircraft_Type; // feed the user input of the ac type 
    }

    
    // this determines if the ac is capaable of taking of / landing on airport 
    public boolean runwayLengthVerfiier (int origin_Length, int arrival_length) throws FileNotFoundException{
        
        // opens the databse for the ac 
        // ac format has acname-typical crusing speed kmh-maxpax(notimplemented)-maxcargo(notimplemented)-rwy req
        File file = new File(AdminOperations.Database_Aircarfts_Path);
        Scanner aircraftMasterReader = new Scanner(file);
        
        
         while (aircraftMasterReader.hasNextLine()) {
                String reader = aircraftMasterReader.nextLine();
                String aircraft_Details[] = reader.split("-");

                if (aircraft_Details[0].equals(aircraft_Type)){// finds mathching ac type 
                    if(Integer.parseInt(aircraft_Details[4]) > origin_Length){
                        return false; 
                    } else if(Integer.parseInt(aircraft_Details[4]) > arrival_length){
                        return false;

                    } // return false if both are not fit with the ac's requirements
                } 
            }
        
         return  true; // returns true if ac is capable
        
    }
    
    
    // Unable to implement on the main program due to the lack of time 
    public void payloadGenerator() throws FileNotFoundException{
        String aircraftFopen = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Aircrafts/Aircraft_Master.txt";
        File file = new File(aircraftFopen);
        // Optional: Check if it works
        if (file.exists()) {
        } else {
            System.out.println("Error: File not found at " + file.getAbsolutePath());
        }
        
        Scanner aircraftMasterReader = new Scanner(aircraftFopen);

        while (aircraftMasterReader.hasNextLine()) {
            String reader = aircraftMasterReader.nextLine();
            String aircraft_Details[] = reader.split("-");

            if (aircraft_Type.equals(aircraft_Details[0])) { // finds the ac 
                //assigns the paylaod to the variables
                aircraft_MaxPax = Integer.parseInt(aircraft_Details[2]); 
                aircraft_MaxCargo = Integer.parseInt(aircraft_Details[3]);
                
                break;
            } 
        }
        
        // random paylaod generator for the flight for commercial airlines
        if (aircraft_MaxPax > 0) {
            Random aircraftPayloadGenerator_Commercial = new Random();
            int aircraft_payloadPax = aircraftPayloadGenerator_Commercial.nextInt(aircraft_MaxPax);
            System.out.println("Current Passenger Load : " + aircraft_payloadPax + " PAX");
            System.out.println("Current Cargo Load: " + 100  + " KG");
        }
        
    }


}
