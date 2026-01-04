/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.Logs;

import java.io.*;
import java.nio.file.Paths;
import javax.swing.JOptionPane;

/**
 *
 * @author alken
 */
public class logs {
    
    private static final String logFilePath = Paths.get(System.getProperty("user.dir"),
            "src", "main", "java", "com", "mycompany",
            "lipadbantayoopdsa", "Logs", "logs.txt").toString();

    public static void writeLog(String entry) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.print(entry);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to log file: " + e.getMessage());
        }
    }
}
