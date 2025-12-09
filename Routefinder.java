

import java.io.*;
import java.util.*;



public class Routefinder {
    public static void main(String[] args) throws FileNotFoundException{

        
        Scanner userInput = new Scanner(System.in);


        System.out.println("Enter the airline code: ");
        String airlineCode = userInput.nextLine().toUpperCase();
        System.out.println("Enter the Departure Airport code: ");
        String airportCode_Departure = userInput.nextLine();
        System.out.println("Enter the Arrival Airport Code: ");
        String airportCode_Arrival = userInput.nextLine();
        System.out.println("Enter the day(ST-Saturday - SU-Sunday - M-Monday - TU-Tuesday - W-Wednesday - TH-Thrusday - F-Friday - E-Everyday): ");
        String day = userInput.nextLine();


        flightCreator createBlock = new flightCreator(airlineCode, airportCode_Arrival, airportCode_Departure, day);
        createBlock.blockCreator();
        userInput.close();







//;--------------------------------------------------------------------------------------
        // String route_Database = "C:/Users/Joshua/Desktop/Database/Timetable";
        // String route_UserSelectAirline; 
        //Random route Generator
        // int route_FlightCreator = 0;
        // Scanner airlineRouteReader = new Scanner(System.in);


        // System.out.println("Please Enter the Airline in the database: ");
        // route_UserSelectAirline = airlineRouteReader.nextLine();
        // route_Database = route_Database + "/Routes_" + route_UserSelectAirline + ".txt";
        // System.out.println("Please Enter the how many flights to generate: ");
        // route_FlightCreator = airlineRouteReader.nextInt();



        // File openAirlineDatabase = new File(route_Database);
        // Scanner airlineRouteReader_Selected_Counter =  new Scanner(openAirlineDatabase);
        // String airlineCode =  airlineRouteReader_Selected_Counter.nextLine();

        // int airlineFlightCounter = 0;
        // while (airlineRouteReader_Selected_Counter.hasNextLine()) {
        //     airlineFlightCounter++;
        //     airlineRouteReader_Selected_Counter.nextLine();
        // }


        // //RANDOM NUMBER GENERATOR 
        // Random randFlightSelector = new Random();
        // int rFS = randFlightSelector.nextInt(airlineFlightCounter);

        // Scanner airlineRouteReader_Selected =  new Scanner(openAirlineDatabase);
        // airlineRouteReader_Selected.nextLine();
        // for (int i = 0; i < rFS-1; i++) {
        //     airlineRouteReader_Selected.nextLine();
        // }  
        // String randomAirlineRoute = airlineRouteReader_Selected.nextLine();
        // String randomAirlineRouteArray[] = randomAirlineRoute.split("-");

        // for (int i = 0; i < route_FlightCreator; i++) {
        //     flightCreator Fcreator = new flightCreator(randomAirlineRouteArray, airlineCode);
        //     Fcreator.displayFlightDetails();
        //     System.out.println();
        // }
        


        



        
    }
}