//package com.mycompany.lipadbantayoopdsa;
//import java.io.*;
//import java.util.*;
//
// do not use
//
//public class Routefinder {
//    public static void main(String[] args) throws FileNotFoundException{
//
//        
//        Scanner userInput = new Scanner(System.in);
//
//
//        System.out.println("Enter the airline code: ");
//        String airlineCode = userInput.nextLine().toUpperCase();
//        System.out.println("Enter the Departure Airport code: ");
//        String airportCode_Departure = userInput.nextLine();
//        System.out.println("Enter the Arrival Airport Code: ");
//        String airportCode_Arrival = userInput.nextLine();
//        System.out.println("Enter the day(ST-Saturday - SU-Sunday - M-Monday - TU-Tuesday - W-Wednesday - TH-Thrusday - F-Friday - E-Everyday): ");
//        String day = userInput.nextLine();
//
//    
//        flightCreator createBlock = new flightCreator(airlineCode, airportCode_Arrival, airportCode_Departure, day);
//        createBlock.blockCreator();
//        userInput.close();
//
//        
//    }
//}