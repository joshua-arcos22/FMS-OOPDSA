package com.mycompany.lipadbantayoopdsa.Database.Timetable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingFunction {

    public static void main(String[] args) {
        sortFile();
    }

    public static void sortFile() {
        
        String filePath = Paths.get(System.getProperty("user.dir"), 
                                   "src", "main", "java", "com", "mycompany", 
                                   "lipadbantayoopdsa", "Database", "Timetable", 
                                   "Departure_timetable_Master.txt").toString();

        File file = new File(filePath);
        

        if (!file.exists()) {
            System.err.println("ERROR: File not found at: " + file.getAbsolutePath());
            return;
        }

        List<String> flightSchedule = new ArrayList<>();

      
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    flightSchedule.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }

     
        Collections.sort(flightSchedule);

       
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String flight : flightSchedule) {
                writer.write(flight);
                writer.newLine();
            }
            System.out.println("Success: Departure_timetable_Master.txt has been sorted.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
}