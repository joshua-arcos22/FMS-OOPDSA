package com.mycompany.lipadbantayoopdsa;
import java.io.*;
import java.util.*;;

public class AircraftFinder {
    private String aircraft_Type;
    private int aircraft_MaxPax;
    private int aircraft_MaxCargo;
    
    AircraftFinder(String aircraft_Type){
        this.aircraft_Type = aircraft_Type;
    }


    //Hello
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

            if (aircraft_Type.equals(aircraft_Details[0])) { 
                aircraft_MaxPax = Integer.parseInt(aircraft_Details[2]);
                aircraft_MaxCargo = Integer.parseInt(aircraft_Details[3]);
                
                break;
            } 
        }
        //added a comment 

        

//        if (aircraft_MaxPax == 0){
//            Random aircraftPayloadGenerator_Freighter = new Random();
//            int aircraft_payloadCargo = aircraftPayloadGenerator_Freighter.nextInt(aircraft_MaxCargo);
//            System.out.println("Current Passenger Load : " + 0 + " PAX");
//            System.out.println("Current Cargo Load: " + aircraft_payloadCargo  + " KG");
//        } 
        
        if (aircraft_MaxPax > 0) {
            Random aircraftPayloadGenerator_Commercial = new Random();
            int aircraft_payloadPax = aircraftPayloadGenerator_Commercial.nextInt(aircraft_MaxPax);


//            int payloadMax_ForPax = aircraft_MaxCargo - (aircraft_payloadPax * 100);
//            int aircraft_payloadCargo = aircraftPayloadGenerator_Commercial.nextInt(payloadMax_ForPax);

            

            System.out.println("Current Passenger Load : " + aircraft_payloadPax + " PAX");
            System.out.println("Current Cargo Load: " + 100  + " KG");
        }
        
    }


}
