package com.mycompany.lipadbantayoopdsa;
import java.io.*;
import java.text.BreakIterator;
import java.util.*;


public class flightCreator {
    private String fc_AirlineCode;
    private String fc_AircraftType;
    private String fc_DepartureAirport;
    private String fc_ArrivingAirport;
    private String fc_Day;




    flightCreator(  String fc_AirlineCode,
                    String fc_ArrivingAirport,
                    String fc_DepartureAirport,
                    String fc_Day
                    ) throws   FileNotFoundException {

        this.fc_AirlineCode = fc_AirlineCode;
        this.fc_DepartureAirport = fc_DepartureAirport.toUpperCase();
        this.fc_ArrivingAirport = fc_ArrivingAirport.toUpperCase();
        this.fc_Day = fc_Day.toUpperCase();

            flightNumebrCreator flightNumber = new flightNumebrCreator();
            flightNumber.fnc_InitializeAllFlightNumbers();
            
            ArrivalTimetableGenerator.generate();



    }

    public void blockCreator() throws FileNotFoundException{
        String timeTable_Database =  "C:/Users/Joshua/Documents/NetBeansProjects/FlightManagementSystem/LipadBantayOOPDSA/src/main/java/com/mycompany/lipadbantayoopdsa/Database/Timetable/Departure_Timetable_Master - Copy.txt";
        File scheduleReader = new File(timeTable_Database);
        Scanner scheduleLineReader = new Scanner(scheduleReader);
        boolean isExisting = false;
        while (scheduleLineReader.hasNextLine()) {
            String lineReader = scheduleLineReader.nextLine();
            String timeTableDetails[] = lineReader.split("-");



             if(fc_AirlineCode.equalsIgnoreCase(timeTableDetails[0]) &&  
                timeTableDetails[2].substring(0,4).equals(fc_DepartureAirport) && 
                timeTableDetails[3].substring(0,4).equals(fc_ArrivingAirport) &&
                fc_Day.equalsIgnoreCase("ALL")) {



                System.out.println("Airline: " + timeTableDetails[0]);
                System.out.println("Flight Number: " + "Not implemented");
                System.out.println("Aircraft Type: " + timeTableDetails[1]);
                System.out.println("Departing Airport: " + timeTableDetails[2]);
                System.out.println("Arrival Airport: " + timeTableDetails[3]);
                System.out.println("Time Of departure: " + timeTableDetails[5] + "Z");
                System.out.println("Frequency: " + timeTableDetails[4]);

                AircraftFinder randomLoadGenerator = new AircraftFinder(timeTableDetails[1]);
                randomLoadGenerator.payloadGenerator();

                distanceCalculator flightDistance = new distanceCalculator(timeTableDetails[2].substring(0, 4), timeTableDetails[3].substring(0, 4), timeTableDetails[1]);
                flightDistance.calculateFlightDurationMinutes();

                

                System.out.println();
                isExisting = true;
            }  
            // if (!(isExisting)) {
            //     System.out.println("Flight Does not Exist");
            //     break;
                
            // }
        }
    }
















    //;------------------------------------------------------------------
    // private String flight_DepartingAirport;
    // private String flight_ArrivinalAirport;
    // private String flight_FlightNumber;
    // private String flight_AircraftType;


    // flightCreator(String flightDetails[], String flightAirlineCode){
    //     flight_DepartingAirport = flightDetails[1];
    //     flight_ArrivinalAirport = flightDetails[2];
    //     flight_AircraftType = flightDetails[0];
    //     flight_FlightNumber = flightAirlineCode;
    // }

    // public void displayFlightDetails() throws FileNotFoundException{
    //     Random flightNumberGen = new Random();
    //     int flNumber = flightNumberGen.nextInt(9999);
    //     flight_FlightNumber = flight_FlightNumber + flNumber;

    //     if (flight_AircraftType.contains("F")) {
    //         System.out.println("FREIGHTER");
    //     } else{
    //         System.out.println("AIRLINER");
    //     }
    //     System.out.println("Departing Airport: " + flight_DepartingAirport);
    //     System.out.println("Arrival Airport: " + flight_ArrivinalAirport);
    //     System.out.println("Aircraft type: " + flight_AircraftType);
    //     System.out.println("Flight Number: " + flight_FlightNumber);

    // AircraftFinder payloadInfo = new AircraftFinder(flight_AircraftType);
    // payloadInfo.payloadGenerator();

    

    




}