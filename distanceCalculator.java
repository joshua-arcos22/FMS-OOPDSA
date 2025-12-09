import java.io.*;
import java.util.Scanner;


public class distanceCalculator {

    private String departure_airportCode;
    private String arrival_airportCode;
    private String aircraftType;

    private double latitude_1;
    private double longitude_1;
    private double latitude_2;
    private double longitude_2;


    distanceCalculator(String departure_airportCode, String arrival_airportCode, String aircraftType){
        this.departure_airportCode = departure_airportCode.substring(0,4).toUpperCase();
        this.arrival_airportCode = arrival_airportCode.substring(0,4).toUpperCase();
        this.aircraftType = aircraftType;
    }

    public void getDuration_Distance() throws FileNotFoundException {
        String dc_airportMaster = "Database/Airports/Airport_Master.txt";
        String dc_aircraftTypeMaster = "Database/Aircrafts/Aircraft_Master.txt";
        File dc_aircraftReader = new File(dc_aircraftTypeMaster);
        File dc_airportReader = new File(dc_airportMaster);
        Scanner dc_airport_reader = new Scanner(dc_airportReader);
        Scanner dc_aircraft_reader = new Scanner(dc_aircraftReader);


        while(dc_airport_reader.hasNextLine()){
            String airportDetailReader = dc_airport_reader.nextLine();
            String airportDetailReaderArray[] = airportDetailReader.split("-");

            if (departure_airportCode.equals(airportDetailReaderArray[1])){
                String latitudeFormating = airportDetailReaderArray[3]  ;
                String latitudeFormatingArray[] = latitudeFormating.split("/");
                double la_degrees =  Integer.parseInt(latitudeFormatingArray[0]);
                double la_minutes = Integer.parseInt(latitudeFormatingArray[1]);
                double la_seconds = Integer.parseInt(latitudeFormatingArray[2]);
                latitude_1 = la_degrees + (double)(la_minutes / 60) + (double)(la_seconds / 3600);

                String longitudeFormatting = airportDetailReaderArray[4];
                String longitudeFormattingArray[] = longitudeFormatting.split("/");
                double lo_degrees =  Integer.parseInt(longitudeFormattingArray[0]);
                double lo_minutes = Integer.parseInt(longitudeFormattingArray[1]);
                double lo_seconds = Integer.parseInt(longitudeFormattingArray[2]);
                longitude_1 = lo_degrees + (double)(lo_minutes / 60) + (double)(lo_seconds / 3600);
                break;
            }

        }

        dc_airport_reader.close();
        dc_airport_reader = new Scanner(new File(dc_airportMaster));

        while(dc_airport_reader.hasNextLine()){
            String airportDetailReader = dc_airport_reader.nextLine();
            String airportDetailReaderArray[] = airportDetailReader.split("-");

            if (arrival_airportCode.equals(airportDetailReaderArray[1])){
                String latitudeFormating = airportDetailReaderArray[3];
                String latitudeFormatingArray[] = latitudeFormating.split("/");
                double la_degrees =  Integer.parseInt(latitudeFormatingArray[0]);
                double la_minutes = Integer.parseInt(latitudeFormatingArray[1]);
                double la_seconds = Integer.parseInt(latitudeFormatingArray[2]);
                latitude_2 = la_degrees + (double)(la_minutes / 60) + (double)(la_seconds / 3600);

                String longitudeFormatting = airportDetailReaderArray[4];
                String longitudeFormattingArray[] = longitudeFormatting.split("/");
                double lo_degrees =  Integer.parseInt(longitudeFormattingArray[0]);
                double lo_minutes = Integer.parseInt(longitudeFormattingArray[1]);
                double lo_seconds = Integer.parseInt(longitudeFormattingArray[2]);
                longitude_2 = lo_degrees + (double)(lo_minutes / 60) + (double)(lo_seconds / 3600);
                break;
            }

        }


        double dLat = Math.toRadians(latitude_2 - latitude_1);
        double dLon = Math.toRadians(longitude_2 - longitude_1);

        // convert to radians
        latitude_1 = Math.toRadians(latitude_1);
        latitude_2 = Math.toRadians(latitude_2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(latitude_1) *
                        Math.cos(latitude_2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));

        System.out.println("Distance of Flight: " + (int)(rad * c) + " km");

        while(dc_aircraft_reader.hasNextLine()){
            String aricraftTypeReader = dc_aircraft_reader.nextLine();
            String aricraftTypeReaderArray[] = aricraftTypeReader.split("-");

            if (aircraftType.equals(aricraftTypeReaderArray[0])){

                double durationFlight = (rad * c) / (Integer.parseInt(aricraftTypeReaderArray[1]));

                int hours = (int) durationFlight;
                int minutes = (int) ((durationFlight - hours) * 60);

                System.out.printf("Duration of Flight is: %d:%02d\n", hours, minutes);;
            }
        }




    }




}
