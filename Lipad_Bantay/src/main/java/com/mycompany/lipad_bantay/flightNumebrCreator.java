package Lipad_Bantay.src.main.java.com.mycompany.lipad_bantay;
import com.sun.source.tree.WhileLoopTree;

import java.io.*;
import java.util.*;
import java.time.*;

public class flightNumebrCreator {

    private String fnc_AirlineCode;

    flightNumebrCreator(String fnc_AirlineCode){
        this.fnc_AirlineCode = fnc_AirlineCode;
    }


    public void fnc_CreateflightNumber() throws FileNotFoundException {
        // --- 1. SETUP (Date & Random) ---
        LocalDate today = LocalDate.now();
        long dailySeed = today.toEpochDay();
        // Seed + Airline Hash ensures unique numbers per airline
        Random flightNumberGenerator = new Random(dailySeed + fnc_AirlineCode.hashCode());

        String airlineCodePath = "Database/Airlines/Airlines_Master.txt";
        String timeTableMaster = "Database/Timetable/Departure_Timetable_Master - Copy.txt";

        // --- 2. GET PREFIX FROM MASTER FILE (e.g., PAL -> PR) ---
        File airlineCodeMaster = new File(airlineCodePath);
        String currentPrefix = "";

        try (Scanner masterReader = new Scanner(airlineCodeMaster)) {
            while (masterReader.hasNextLine()) {
                String line = masterReader.nextLine();
                String[] parts = line.split("-");
                // Expected format: PAL-PR (AirlineCode-Prefix)
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(fnc_AirlineCode)) {
                    currentPrefix = parts[1]; // Found "PR"
                    break;
                }
            }
        }

        if (currentPrefix.isEmpty()) {
            System.out.println("Error: Prefix not found for " + fnc_AirlineCode);
            return;
        }

        // --- 3. PROCESS TIMETABLE ---
        File timeTableFile = new File(timeTableMaster);
        ArrayList<String> fileContent = new ArrayList<>();

        if (timeTableFile.exists()) {
            try (Scanner timeTableReader = new Scanner(timeTableFile)) {
                while (timeTableReader.hasNextLine()) {
                    String currentLine = timeTableReader.nextLine();

                    // SPLIT THE LINE
                    String[] parts = currentLine.split("-");

                    // CHECK 1: Does this line belong to the current airline?
                    // If the line starts with "CEB", but we are running "PAL", SKIP IT (save as is).
                    if (!parts[0].equalsIgnoreCase(fnc_AirlineCode)) {
                        fileContent.add(currentLine);
                        continue;
                    }

                    // CHECK 2: Clean up the end of the line
                    // We look at the very last item in the array.
                    String lastPart = parts[parts.length - 1];
                    String baseLine = currentLine;

                    // If the last part starts with a LETTER (e.g., "P" for PR, "5" for 5J, "A" for AirAsia)
                    // It means there is ALREADY a flight number there. We must cut it off.
                    // (Times usually start with digits like 1845, IDs start with letters/codes)
                    boolean hasExistingID = lastPart.matches(".*[A-Za-z].*");

                    if (hasExistingID) {
                        // Find the last dash and remove everything after it
                        int lastDash = currentLine.lastIndexOf("-");
                        if (lastDash != -1) {
                            baseLine = currentLine.substring(0, lastDash);
                        }
                    }

                    // CHECK 3: Generate and Append
                    int flightNum = 1000 + flightNumberGenerator.nextInt(9000);
                    String newFlightCode = currentPrefix + flightNum; // e.g., PR + 1234

                    fileContent.add(baseLine + "-" + newFlightCode);
                }
            }
        }

        // --- 4. WRITE BACK TO FILE ---
        try (PrintWriter timeTableWriter = new PrintWriter(timeTableFile)) {
            for (String line : fileContent) {
                timeTableWriter.println(line);
            }
        }

        System.out.println("Timetable updated for " + fnc_AirlineCode);
    }

    public static boolean isNewDay() throws FileNotFoundException {
        String datePath = "/Database/Date.txt";
        File dateFile = new File(datePath);
        LocalDate today = LocalDate.now();

        if (!dateFile.exists()) {
            try (PrintWriter dateWriter = new PrintWriter(dateFile)) {
                dateWriter.write(today.toString());
            }
            return true;
        }


        LocalDate lastRunDate = null;
        try (Scanner dateReader = new Scanner(dateFile)) {
            if (dateReader.hasNext()) {
                String dateString = dateReader.next();
                lastRunDate = LocalDate.parse(dateString);
            }
        }


        if (lastRunDate != null && !today.equals(lastRunDate)) {

            try (PrintWriter dateWriter = new PrintWriter(dateFile)) {
                dateWriter.write(today.toString());
            }
            return true;
        }



        return false;
    }
    private void updateDateFile(LocalDate today) throws FileNotFoundException {
        File dateFile = new File("Database/Date.txt");
        try (PrintWriter pw = new PrintWriter(dateFile)) {
            pw.write(today.toString());
        }
    }

}
