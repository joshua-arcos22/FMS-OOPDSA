/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.popupInterface;
import com.mycompany.lipadbantayoopdsa.MainFlightDisplayAdmin;
import com.mycompany.lipadbantayoopdsa.AdminOperations;
import com.mycompany.lipadbantayoopdsa.distanceCalculator;
import com.mycompany.lipadbantayoopdsa.AircraftFinder;
import com.mycompany.lipadbantayoopdsa.ArrivalTimetableGenerator;
import com.mycompany.lipadbantayoopdsa.Logs.logs;

import java.util.*;
import java.io.*;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.*;


/**
 *
 * @author Joshua
 */
public class editFlightScreen_1 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editFlightScreen_1.class.getName());
    
        private String Airline_Name;
        private String Ac_Type;
        private String Origin_Airport;
        private String Destination_Airport;
        private String Frequency = "";
        private String Time;
        private String Flight_Number;
        private String Original_Flight_Number_Ref;
        private String Pax_Ref;
        private String Cargo_Ref;
        private String Status_Ref;
                
        public editFlightScreen_1(
            String airline,
            String aircraft,
            String origin,
            String destination,
            String frequency,
            String time,
            String flightNumber,
            String pax, // NEW
            String cargo, // NEW
            String status // NEW 
        ) {
            initComponents();

            
            //initializes value of the error labels
            errorAIRLINENAME.setText(" ");
            errorFLTNO.setText(" ");
            errorTIME.setText(" ");
            originRWYLn.setText(" ");
            destRWYLn.setText(" ");
            acRWYLn.setText(" ");
            errorPAX.setText(" ");
            errorCARGO.setText(" ");
            
            this.Original_Flight_Number_Ref = flightNumber;

            this.Original_Flight_Number_Ref = flightNumber;
            this.Airline_Name = airline; // Store for logic
            this.Ac_Type = aircraft;     // Store for logic

            // Set Text Fields
            airlineField.setText(airline);
            flnField.setText(flightNumber);
            timeField.setText(time);
            paxFIELD.setText(pax);     // This puts the "150" into the text box
            cargoFIELD.setText(cargo); // This puts the "5000" into the text box
            status_drpdwn.setSelectedItem(status); // This selects "Delayed" in the dropdown

            // Initialize Dropdowns
            initializeDropdowns(aircraft, origin, destination, status);

            // Set Frequency Checks
            setFrequencyChecks(frequency);
   
            // Run it once initially to set the correct labels for default selections
            updateRunwayStatus();
            
        }
        
        
        // Helper to clean up the constructor (Copy this method too)
        private void initializeDropdowns(String aircraft, String origin, String destination, String status) {
            // 1. Status Dropdown
            status_drpdwn.removeAllItems();
            status_drpdwn.addItem("Scheduled");
            status_drpdwn.addItem("Delayed");
            status_drpdwn.addItem("Canceled");
            status_drpdwn.setSelectedItem(status);

            // 2. Aircraft Dropdown
            ac_drpdwn.removeAllItems();
            try {
                File acFile = new File(AdminOperations.Database_Aircarfts_Path);
                Scanner scanner = new Scanner(acFile);
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split("-");
                    ac_drpdwn.addItem(parts[0]);
                }
                ac_drpdwn.setSelectedItem(aircraft);
            } catch (FileNotFoundException e) {
                System.out.println("AC File not found");
            }

            // 3. Origin Dropdown
            // Extract just the "RPLK" from "RPLK(Daraga)"
            String originCode = (origin.length() >= 4) ? origin.substring(0, 4) : origin;

            origin_drpdwn.removeAllItems();
            try {
                File aprtFile = new File(AdminOperations.Database_Aiports_Path);
                Scanner scanner = new Scanner(aprtFile);
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split("-");
                    // Assuming File format is: ICAO-City-Runway (e.g., RPLK-Daraga-2100)
                    // Dropdown format: ICAO(City) -> RPLK(Daraga)
                    String itemText = parts[1] + "(" + parts[0] + ")";

                    // NOTE: Check your file order. If parts[0] is ICAO, swap the line above to:
                    // String itemText = parts[0] + "(" + parts[1] + ")";
                    // Check based on the Screenshot (RPVU is appearing first, so standard ICAO is likely first)
                    if (parts[0].length() == 4) { // Heuristic check
                        itemText = parts[0] + "(" + parts[1] + ")";
                    } else {
                        itemText = parts[1] + "(" + parts[0] + ")";
                    }

                    origin_drpdwn.addItem(itemText);

                    // Compare just the codes
                    // parts[0] is likely the ICAO code in your file
                    if (parts[0].equalsIgnoreCase(originCode) || parts[1].equalsIgnoreCase(originCode)) {
                        origin_drpdwn.setSelectedItem(itemText);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Airport File not found");
            }

            // 4. Destination Dropdown
            // Extract just the "RPMR" from "RPMR(General...)"
            String destCode = (destination.length() >= 4) ? destination.substring(0, 4) : destination;

            destination_drpdwn.removeAllItems();
            try {
                File aprtFile = new File(AdminOperations.Database_Aiports_Path);
                Scanner scanner = new Scanner(aprtFile);
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split("-");

                    // Build the string to match your UI (ICAO(City))
                    String itemText = "";
                    if (parts[0].length() == 4) {
                        itemText = parts[0] + "(" + parts[1] + ")";
                    } else {
                        itemText = parts[1] + "(" + parts[0] + ")";
                    }

                    destination_drpdwn.addItem(itemText);

                    // Compare code with file data
                    if (parts[0].equalsIgnoreCase(destCode) || parts[1].equalsIgnoreCase(destCode)) {
                        destination_drpdwn.setSelectedItem(itemText);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Airport File not found");
            }
        }
        
        
        
        private void setFrequencyChecks(String freq) {
            if (freq == null) return;

            if (freq.equals("E")) {
                E_check.setSelected(true);
                E_checkMouseClicked(null);
                return;
            }

            String[] parts = freq.split("/");
            for (String p : parts) {
                switch (p) {
                    case "M"  -> M_check.setSelected(true);
                    case "TU" -> TU_check.setSelected(true);
                    case "W"  -> W_check.setSelected(true);
                    case "TH" -> TH_check.setSelected(true);
                    case "F"  -> F_check.setSelected(true);
                    case "ST" -> ST_check.setSelected(true);
                    case "SU" -> SU_check.setSelected(true);
                }
            }
        }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bodyContainer = new javax.swing.JPanel();
        topContainer = new javax.swing.JPanel();
        fucntionTitle = new javax.swing.JLabel();
        ARILINECODE = new javax.swing.JLabel();
        airlineField = new javax.swing.JTextField();
        AIRCRAFT = new javax.swing.JLabel();
        btn_addFlight = new javax.swing.JButton();
        ORIGIN = new javax.swing.JLabel();
        DESTINATION = new javax.swing.JLabel();
        origin_drpdwn = new javax.swing.JComboBox<>();
        destination_drpdwn = new javax.swing.JComboBox<>();
        ac_drpdwn = new javax.swing.JComboBox<>();
        FREQUENCY = new javax.swing.JLabel();
        M_check = new javax.swing.JCheckBox();
        TU_check = new javax.swing.JCheckBox();
        W_check = new javax.swing.JCheckBox();
        TH_check = new javax.swing.JCheckBox();
        F_check = new javax.swing.JCheckBox();
        ST_check = new javax.swing.JCheckBox();
        E_check = new javax.swing.JCheckBox();
        SU_check = new javax.swing.JCheckBox();
        TIME = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        FLNO = new javax.swing.JLabel();
        flnField = new javax.swing.JTextField();
        btn_back = new javax.swing.JButton();
        errorAIRLINENAME = new javax.swing.JLabel();
        errorFLTNO = new javax.swing.JLabel();
        errorTIME = new javax.swing.JLabel();
        originRWYLn = new javax.swing.JLabel();
        destRWYLn = new javax.swing.JLabel();
        acRWYLn = new javax.swing.JLabel();
        FLNO2 = new javax.swing.JLabel();
        cargoFIELD = new javax.swing.JTextField();
        errorCARGO = new javax.swing.JLabel();
        errorPAX = new javax.swing.JLabel();
        paxFIELD = new javax.swing.JTextField();
        FLNO3 = new javax.swing.JLabel();
        ARILINECODE1 = new javax.swing.JLabel();
        status_drpdwn = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bodyContainer.setBackground(new java.awt.Color(255, 255, 255));

        topContainer.setBackground(new java.awt.Color(0, 153, 255));

        fucntionTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        fucntionTitle.setText("EDIT FLIGHT");

        javax.swing.GroupLayout topContainerLayout = new javax.swing.GroupLayout(topContainer);
        topContainer.setLayout(topContainerLayout);
        topContainerLayout.setHorizontalGroup(
            topContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topContainerLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(fucntionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topContainerLayout.setVerticalGroup(
            topContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topContainerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(fucntionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        ARILINECODE.setBackground(new java.awt.Color(102, 102, 102));
        ARILINECODE.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ARILINECODE.setText("AIRLINE NAME");

        airlineField.setBackground(new java.awt.Color(0, 153, 204));
        airlineField.setForeground(new java.awt.Color(255, 255, 255));
        airlineField.setText("AIRLINE CODE");
        airlineField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        airlineField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                airlineFieldActionPerformed(evt);
            }
        });

        AIRCRAFT.setBackground(new java.awt.Color(102, 102, 102));
        AIRCRAFT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        AIRCRAFT.setText("AIRCRAFT");

        btn_addFlight.setBackground(new java.awt.Color(0, 153, 204));
        btn_addFlight.setForeground(new java.awt.Color(255, 255, 255));
        btn_addFlight.setText("EDIT FLIGHT");
        btn_addFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addFlightActionPerformed(evt);
            }
        });

        ORIGIN.setBackground(new java.awt.Color(102, 102, 102));
        ORIGIN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ORIGIN.setText("ORIGIN");

        DESTINATION.setBackground(new java.awt.Color(102, 102, 102));
        DESTINATION.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        DESTINATION.setText("DESTINATION");

        origin_drpdwn.setBackground(new java.awt.Color(0, 153, 204));
        origin_drpdwn.setForeground(new java.awt.Color(255, 255, 255));
        origin_drpdwn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        origin_drpdwn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        destination_drpdwn.setBackground(new java.awt.Color(0, 153, 204));
        destination_drpdwn.setForeground(new java.awt.Color(255, 255, 255));
        destination_drpdwn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        destination_drpdwn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ac_drpdwn.setBackground(new java.awt.Color(0, 153, 204));
        ac_drpdwn.setForeground(new java.awt.Color(255, 255, 255));
        ac_drpdwn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ac_drpdwn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        FREQUENCY.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FREQUENCY.setText("Frequency");

        M_check.setText("MONDAY");
        M_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                M_checkActionPerformed(evt);
            }
        });

        TU_check.setText("TUESDAY");

        W_check.setText("WEDNESDAY");

        TH_check.setText("THURSDAY");

        F_check.setText("FRIDAY");

        ST_check.setText("SATURDAY");

        E_check.setText("EVERYDAY");
        E_check.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                E_checkMouseClicked(evt);
            }
        });

        SU_check.setText("SUNDAY");
        SU_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SU_checkActionPerformed(evt);
            }
        });

        TIME.setBackground(new java.awt.Color(102, 102, 102));
        TIME.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TIME.setText("TIME");

        timeField.setBackground(new java.awt.Color(0, 153, 204));
        timeField.setForeground(new java.awt.Color(255, 255, 255));
        timeField.setText("0000z (Format)");
        timeField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        FLNO.setBackground(new java.awt.Color(102, 102, 102));
        FLNO.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FLNO.setText("FLT NO.");

        flnField.setBackground(new java.awt.Color(0, 153, 204));
        flnField.setForeground(new java.awt.Color(255, 255, 255));
        flnField.setText("6767");
        flnField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btn_back.setText("BACK");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        errorAIRLINENAME.setText("placeholder");

        errorFLTNO.setText("placeholder");

        errorTIME.setText("placeholder");

        originRWYLn.setText("placeholder");

        destRWYLn.setText("placeholder");

        acRWYLn.setText("placeholder");

        FLNO2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FLNO2.setText("CARGO");

        cargoFIELD.setBackground(new java.awt.Color(0, 153, 204));
        cargoFIELD.setForeground(new java.awt.Color(255, 255, 255));
        cargoFIELD.setText("0");
        cargoFIELD.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        errorCARGO.setText("placeholder");

        errorPAX.setText("placeholder");

        paxFIELD.setBackground(new java.awt.Color(0, 153, 204));
        paxFIELD.setForeground(new java.awt.Color(255, 255, 255));
        paxFIELD.setText("0");
        paxFIELD.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        FLNO3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FLNO3.setText("PAX");

        ARILINECODE1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ARILINECODE1.setText("STATUS");

        status_drpdwn.setBackground(new java.awt.Color(0, 153, 204));
        status_drpdwn.setForeground(new java.awt.Color(255, 255, 255));
        status_drpdwn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        status_drpdwn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        status_drpdwn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_drpdwnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyContainerLayout = new javax.swing.GroupLayout(bodyContainer);
        bodyContainer.setLayout(bodyContainerLayout);
        bodyContainerLayout.setHorizontalGroup(
            bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bodyContainerLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FREQUENCY, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addComponent(M_check)
                                .addGap(18, 18, 18)
                                .addComponent(TU_check)
                                .addGap(18, 18, 18)
                                .addComponent(W_check)
                                .addGap(18, 18, 18)
                                .addComponent(TH_check)
                                .addGap(18, 18, 18)
                                .addComponent(F_check)
                                .addGap(18, 18, 18)
                                .addComponent(ST_check)
                                .addGap(18, 18, 18)
                                .addComponent(SU_check))
                            .addComponent(E_check)
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addGap(282, 282, 282)
                                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(btn_addFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ARILINECODE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(airlineField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(origin_drpdwn, javax.swing.GroupLayout.Alignment.TRAILING, 0, 135, Short.MAX_VALUE)
                            .addComponent(originRWYLn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ORIGIN, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(errorAIRLINENAME, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ARILINECODE1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(status_drpdwn, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(bodyContainerLayout.createSequentialGroup()
                                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(destRWYLn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(destination_drpdwn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(DESTINATION, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGap(58, 58, 58))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bodyContainerLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(timeField, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                            .addComponent(errorTIME, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TIME, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(AIRCRAFT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(ac_drpdwn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(bodyContainerLayout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addComponent(acRWYLn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FLNO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(flnField)
                                        .addComponent(errorFLTNO, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(FLNO3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(paxFIELD)
                                    .addComponent(errorPAX, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(FLNO2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cargoFIELD)
                                    .addComponent(errorCARGO, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(82, 82, 82))))
        );

        bodyContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ac_drpdwn, airlineField, destination_drpdwn, flnField, origin_drpdwn, timeField});

        bodyContainerLayout.setVerticalGroup(
            bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyContainerLayout.createSequentialGroup()
                .addComponent(topContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addComponent(FLNO, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flnField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(errorFLTNO))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addComponent(ARILINECODE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(airlineField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(errorAIRLINENAME))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                        .addComponent(TIME, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(errorTIME)))
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addComponent(FLNO3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(paxFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addComponent(ARILINECODE1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(status_drpdwn)))
                        .addGap(13, 13, 13)
                        .addComponent(errorPAX))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(FLNO2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cargoFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(errorCARGO)))
                .addGap(40, 40, 40)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ORIGIN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(origin_drpdwn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(originRWYLn))
                    .addComponent(DESTINATION, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(destination_drpdwn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(destRWYLn))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AIRCRAFT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(ac_drpdwn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(acRWYLn)))))
                .addGap(18, 18, 18)
                .addComponent(FREQUENCY, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(M_check)
                    .addComponent(TU_check)
                    .addComponent(W_check)
                    .addComponent(TH_check)
                    .addComponent(F_check)
                    .addComponent(ST_check)
                    .addComponent(SU_check))
                .addGap(18, 18, 18)
                .addComponent(E_check)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_addFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_back)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bodyContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bodyContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void M_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_M_checkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_M_checkActionPerformed

    private void SU_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SU_checkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SU_checkActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btn_backActionPerformed

    private void airlineFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_airlineFieldActionPerformed
        
    }//GEN-LAST:event_airlineFieldActionPerformed

    
    private void updateRunwayStatus() {
        

        String originChoice = origin_drpdwn.getSelectedItem().toString().substring(0, 4).trim();
        String destChoice = destination_drpdwn.getSelectedItem().toString().substring(0, 4).trim();
        String acTypeChoice = ac_drpdwn.getSelectedItem().toString().trim();

        int NF_choiceOrigin = 0;
        int NF_choiceDestination = 0;
        int NF_acType_Choice = 0;

       
        if (originChoice.equalsIgnoreCase(destChoice)) {
            originRWYLn.setText("Same Airport As Dest");
            originRWYLn.setForeground(Color.red);
            destRWYLn.setText("Same Airport As Origin");
            destRWYLn.setForeground(Color.red);  
        } else {
            originRWYLn.setText("Checking...");
            destRWYLn.setText("Checking...");

            try {
                File aprtOpener = new File(AdminOperations.Database_Aiports_Path);
                Scanner aprtReader = new Scanner(aprtOpener);

               

                while (aprtReader.hasNextLine()) {
                    String line = aprtReader.nextLine();
                    String[] parts = line.split("-");

                    // Find Origin Data
                    if (parts[1].equalsIgnoreCase(originChoice)) {
                        originRWYLn.setForeground(Color.BLACK);
                        originRWYLn.setText(parts[2] + "m");
                        NF_choiceOrigin = Integer.parseInt(parts[2]);
 
                    }

                    // Find Destination Data
                    if (parts[1].equalsIgnoreCase(destChoice)) {
                        destRWYLn.setForeground(Color.BLACK);
                        destRWYLn.setText(parts[2] + "m");
                        NF_choiceDestination = Integer.parseInt(parts[2]);
                        
                    }
                }
                aprtReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Database file not found.");
            }
        }

        
        try {
            File acOpener = new File(AdminOperations.Database_Aircarfts_Path);
            Scanner acRWYReader = new Scanner(acOpener);

            while (acRWYReader.hasNextLine()) {
                String acRwyLineReader = acRWYReader.nextLine();
                String[] acRwyLineReaderArray = acRwyLineReader.split("-");
                if (acRwyLineReaderArray[0].equalsIgnoreCase(acTypeChoice)) {
                    acRWYLn.setForeground(Color.BLACK);
                    acRWYLn.setText(acRwyLineReaderArray[4] + "m");
                    NF_acType_Choice = Integer.parseInt(acRwyLineReaderArray[4]);
                    break;
                }
            }
            acRWYReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Aircraft DB not found");
        }

        
        boolean originShort = (NF_choiceOrigin < NF_acType_Choice);
        boolean destShort = (NF_choiceDestination < NF_acType_Choice);

        if (originShort && destShort) {
            acRWYLn.setForeground(Color.red);
            acRWYLn.setText("OrgDest RWY Short");
        } else if (originShort) {
            acRWYLn.setForeground(Color.red);
            acRWYLn.setText("Origin RWY Short");
        } else if (destShort) {
            acRWYLn.setForeground(Color.red);
            acRWYLn.setText("Dest. RWY Short");
        }
        
    }
    
    
    private String getFrequencyString() {

        // SATURDAY
        if (ST_check.isSelected()) {
            Frequency = "ST"; 
        }
        // SUNDAY
        if (SU_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; } 
            Frequency += "SU";
        }
        // MONDAY
        if (M_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; }
            Frequency += "M";
        }
        // TUESDAY
        if (TU_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; }
            Frequency += "TU";
        }
        // WEDNESDAY
        if (W_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; }
            Frequency += "W";
        }
        //THURSDAY
        if (TH_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; }
            Frequency += "TH";
        }
        //FRIDAY
        if (F_check.isSelected()) {
            if (!Frequency.isEmpty()) {Frequency += "/"; }
            Frequency += "F";
        }
        if (ST_check.isSelected() && SU_check.isSelected() && M_check.isSelected() && 
        TU_check.isSelected() && W_check.isSelected() && TH_check.isSelected() && 
        F_check.isSelected()) {
            return "E"; 
        }
        return Frequency;
    }

    
    private boolean isFlightNumberDuplicate(String flightNumToCheck) {
        try {
            
            File file = new File(AdminOperations.Database_TimeTable_Departure_Path);

            
            if (!file.exists()) {
                return false; 
            }

            Scanner scanner = new Scanner(file);

          
            // checks the data of the time table and the and extracts the flight number 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("-");

               
                if (data.length >= 7) {
                    String existingFlightNo = data[6].trim();

                    if (existingFlightNo.equalsIgnoreCase(flightNumToCheck)) {
                        scanner.close();
                        return true; 
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error checking duplicates");
        }
        return false;
    }
    
    
    private void verifyAndAddRoute(String airlineName, String aircraft, String origin, String destination) {
        String projectRoot = System.getProperty("user.dir");
        String filename = "Routes_" + airlineName.trim().toUpperCase() + ".txt";
        String routePath = java.nio.file.Paths.get(projectRoot,
                "src", "main", "java", "com", "mycompany", "lipadbantayoopdsa",
                "Database", "Routes", filename).toString();

        File file = new File(routePath);

        // Create file if missing
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
        }

        boolean routeExists = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("-");
                if (parts.length >= 3) {
                    if (parts[0].equalsIgnoreCase(aircraft)
                            && parts[1].equalsIgnoreCase(origin)
                            && parts[2].equalsIgnoreCase(destination)) {
                        routeExists = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
        }

        // Append if missing
        if (!routeExists) {
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file, true))) {
                
                // Format: Aircraft-Origin-Destination-0.00
                writer.write(aircraft + "-" + origin + "-" + destination + "-0.00");
                writer.newLine();
            } catch (IOException e) {
            }
        }
    }
    
    private void logFlightEdit(String actor, String flightNumber, String airlineName, String acType,
                               String originFull, String destFull, String time, String frequency,
                               String pax, String cargo, String status) {
     try (PrintWriter logWriter = new PrintWriter(new FileWriter("flight_edits_log.txt", true))) {
         String timestamp = java.time.LocalDateTime.now()
                 .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"));

         String logEntry = String.format(
             "[%s]%n" +
             "EVENT  : FLIGHT_EDIT%n" +
             "ACTION : Flight details updated%n" +
             "ACTOR  : %s%n%n" +
             "DETAILS%n" +
             "--------%n" +
             "Flight Number       : %s%n" +
             "Airline Name        : %s%n" +
             "Aircraft Type       : %s%n" +
             "Origin Airport      : %s%n" +
             "Destination Airport : %s%n" +
             "Time                : %s%n" +
             "Frequency           : %s%n" +
             "PAX Capacity        : %s%n" +
             "Cargo Capacity      : %s%n" +
             "Status              : %s%n%n" +
             "----------------------------------------%n%n",
             timestamp, actor, flightNumber, airlineName, acType, originFull, destFull,
             time, frequency, pax, cargo, status
         );

         logs.writeLog(logEntry);

     } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error logging flight edit: " + e.getMessage(),
                                       "Log Error", JOptionPane.ERROR_MESSAGE);
     }
 }
    
    
    
    private void btn_addFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addFlightActionPerformed
        // 1. GET RAW INPUTS
        String selectedAc = ac_drpdwn.getSelectedItem().toString().trim();
        String airlineNameInput = airlineField.getText().trim();
        String flightNumInput = flnField.getText().trim().toUpperCase();
        String timeInput = timeField.getText().trim();
        String paxInput = paxFIELD.getText().trim();
        String cargoInput = cargoFIELD.getText().trim();
        String statusInput = status_drpdwn.getSelectedItem().toString();

        // 2. INITIALIZE VALIDATION FLAGS
        boolean airlineField_Valid = true;
        boolean Flt_No_Valid = true;
        boolean time_Valid = true;
        boolean runway_Valid = true;
        boolean pax_Valid = true;
        boolean cargo_Valid = true;

        // --- A. AIRLINE NAME VALIDATION (Admin Specific) ---
        if (airlineNameInput.isEmpty()) {
            errorAIRLINENAME.setText("Required");
            errorAIRLINENAME.setForeground(Color.red);
            airlineField_Valid = false;
        } else if (airlineNameInput.matches(".*[0-9].*")) {
            errorAIRLINENAME.setText("Invalid Input");
            errorAIRLINENAME.setForeground(Color.red);
            airlineField_Valid = false;
        } else {
            errorAIRLINENAME.setText(" ");
            this.Airline_Name = airlineNameInput;
        }

        // --- B. FLIGHT NUMBER VALIDATION ---
        if (flightNumInput.isEmpty()) {
            errorFLTNO.setText("Required");
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
        } else if (!flightNumInput.matches("[A-Z0-9]+")) {
            errorFLTNO.setText("Alphanumeric Only");
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
        } else if (flightNumInput.length() != 6) {
            errorFLTNO.setText("Invalid Length");
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
            // Note: No Prefix check for Admin (they control all airlines)
        } else if (!flightNumInput.equalsIgnoreCase(Original_Flight_Number_Ref) && isFlightNumberDuplicate(flightNumInput)) {
            errorFLTNO.setText("Flight Exists");
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
        } else {
            errorFLTNO.setText(" ");
            this.Flight_Number = flightNumInput;
        }

        // --- C. TIME VALIDATION ---
        if (timeInput.length() != 4 || !timeInput.matches("\\d+")) {
            errorTIME.setText("Use HHMM");
            errorTIME.setForeground(Color.red);
            time_Valid = false;
        } else {
            int hrs = Integer.parseInt(timeInput.substring(0, 2));
            int mins = Integer.parseInt(timeInput.substring(2, 4));
            if (hrs > 23 || mins > 59) {
                errorTIME.setText("Invalid Time");
                errorTIME.setForeground(Color.red);
                time_Valid = false;
            } else {
                errorTIME.setText(" ");
                this.Time = timeInput;
            }
        }

        // --- D. AIRCRAFT LIMITS (PAX & CARGO) ---
        try {
            File acFile = new File(AdminOperations.Database_Aircarfts_Path);
            Scanner reader = new Scanner(acFile);
            int maxPax = 0;
            int maxCargo = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("-");
                if (parts[0].equalsIgnoreCase(selectedAc)) {
                    maxPax = Integer.parseInt(parts[2]);
                    maxCargo = Integer.parseInt(parts[4]);
                    break;
                }
            }
            reader.close();

            // Check Pax
            if (!paxInput.matches("\\d+")) {
                errorPAX.setText("Invalid");
                pax_Valid = false;
            } else if (Integer.parseInt(paxInput) > maxPax) {
                errorPAX.setText("Limit: " + maxPax);
                errorPAX.setForeground(Color.red);
                pax_Valid = false;
            } else {
                errorPAX.setText(" ");
            }

            // Check Cargo
            if (!cargoInput.matches("\\d+")) {
                errorCARGO.setText("Invalid");
                cargo_Valid = false;
            } else if (Integer.parseInt(cargoInput) > maxCargo) {
                errorCARGO.setText("Limit: " + maxCargo);
                errorCARGO.setForeground(Color.red);
                cargo_Valid = false;
            } else {
                errorCARGO.setText(" ");
            }

        } catch (Exception e) {
            System.out.println("AC Database Error");
        }

        // --- E. RUNWAY CHECK ---
        if (originRWYLn.getForeground() == Color.red || destRWYLn.getForeground() == Color.red || acRWYLn.getForeground() == Color.red) {
            runway_Valid = false;
        }

        // 3. FINAL EXECUTION
        if (airlineField_Valid && Flt_No_Valid && time_Valid && runway_Valid && pax_Valid && cargo_Valid) {

            this.Ac_Type = selectedAc;

            // --- 1. CAPTURE BOTH SHORT AND FULL NAMES ---
            // Short Code (e.g., "RPVM") for Timetable
            this.Origin_Airport = origin_drpdwn.getSelectedItem().toString().substring(0, 4).trim();
            this.Destination_Airport = destination_drpdwn.getSelectedItem().toString().substring(0, 4).trim();

            // Full Name (e.g., "RPVM(Cebu)") for Route File
            String originFull = origin_drpdwn.getSelectedItem().toString().trim();
            String destFull = destination_drpdwn.getSelectedItem().toString().trim();
            // ---------------------------------------------

            this.Frequency = getFrequencyString();

            // Update Timetable using Short Codes
            AdminOperations ops = new AdminOperations(
                    Airline_Name, Ac_Type, Origin_Airport, Destination_Airport,
                    Frequency, Time, Flight_Number, paxInput, cargoInput, statusInput
            );

            try {
                ops.Admin_EditFlight(this.Original_Flight_Number_Ref);

                // --- 2. UPDATE PRICING USING FULL NAMES ---
                verifyAndAddRoute(
                        Airline_Name, // Passed from text field (Admin can edit this)
                        Ac_Type,
                        originFull,
                        destFull
                );

                // --- 3. LOG THE EDIT ---
                String timestamp = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"));

                String logEntry = String.format(
                    "[%s]%n" +
                    "EVENT  : FLIGHT_EDIT%n" +
                    "ACTION : Flight details updated%n" +
                    "ACTOR  : ADMIN%n%n" +
                    "DETAILS%n" +
                    "--------%n" +
                    "Flight No     : %s%n" +
                    "Airline       : %s%n" +
                    "Aircraft      : %s%n" +
                    "Origin        : %s%n" +
                    "Destination   : %s%n" +
                    "Time          : %s%n" +
                    "Frequency     : %s%n" +
                    "PAX           : %s%n" +
                    "Cargo         : %s%n" +
                    "Status        : %s%n%n" +  // Blank line for spacing between logs
                    "----------------------------------------%n%n",  // Another blank line
                    timestamp, Flight_Number, Airline_Name, Ac_Type, originFull, destFull,
                    Time, Frequency, paxInput, cargoInput, statusInput
                );

                logs.writeLog(logEntry); // Save the log like your other admin actions

                javax.swing.JOptionPane.showMessageDialog(this, "Flight Updated Successfully!");
                ArrivalTimetableGenerator.generate();
                dispose();

            } catch (IOException e) {
                System.out.println("Error Saving Flight");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fix the errors in red.", 
                "Validation Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_addFlightActionPerformed

    
    private void E_checkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_E_checkMouseClicked
        boolean isChecked = E_check.isSelected();
        M_check.setSelected(isChecked);   // Monday
        TU_check.setSelected(isChecked);  // Tuesday
        W_check.setSelected(isChecked);   // Wednesday
        TH_check.setSelected(isChecked);  // Thursday
        F_check.setSelected(isChecked);   // Friday
        ST_check.setSelected(isChecked);  // Saturday
        SU_check.setSelected(isChecked);  // Sunday
    }//GEN-LAST:event_E_checkMouseClicked

    private void status_drpdwnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_drpdwnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_status_drpdwnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new editFlightScreen_1(
        "AIRLINE1",  // airline
        "B737",      // aircraft
        "MNLA",       // origin
        "CEBU",       // destination
        "M/TU/W",    // frequency
        "0930",      // time
        "AB1234",  // flight number
                "0",
                "0",
                "Default"
        ).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AIRCRAFT;
    private javax.swing.JLabel ARILINECODE;
    private javax.swing.JLabel ARILINECODE1;
    private javax.swing.JLabel DESTINATION;
    private javax.swing.JCheckBox E_check;
    private javax.swing.JLabel FLNO;
    private javax.swing.JLabel FLNO2;
    private javax.swing.JLabel FLNO3;
    private javax.swing.JLabel FREQUENCY;
    private javax.swing.JCheckBox F_check;
    private javax.swing.JCheckBox M_check;
    private javax.swing.JLabel ORIGIN;
    private javax.swing.JCheckBox ST_check;
    private javax.swing.JCheckBox SU_check;
    private javax.swing.JCheckBox TH_check;
    private javax.swing.JLabel TIME;
    private javax.swing.JCheckBox TU_check;
    private javax.swing.JCheckBox W_check;
    private javax.swing.JLabel acRWYLn;
    private javax.swing.JComboBox<String> ac_drpdwn;
    private javax.swing.JTextField airlineField;
    private javax.swing.JPanel bodyContainer;
    private javax.swing.JButton btn_addFlight;
    private javax.swing.JButton btn_back;
    private javax.swing.JTextField cargoFIELD;
    private javax.swing.JLabel destRWYLn;
    private javax.swing.JComboBox<String> destination_drpdwn;
    private javax.swing.JLabel errorAIRLINENAME;
    private javax.swing.JLabel errorCARGO;
    private javax.swing.JLabel errorFLTNO;
    private javax.swing.JLabel errorPAX;
    private javax.swing.JLabel errorTIME;
    private javax.swing.JTextField flnField;
    private javax.swing.JLabel fucntionTitle;
    private javax.swing.JLabel originRWYLn;
    private javax.swing.JComboBox<String> origin_drpdwn;
    private javax.swing.JTextField paxFIELD;
    private javax.swing.JComboBox<String> status_drpdwn;
    private javax.swing.JTextField timeField;
    private javax.swing.JPanel topContainer;
    // End of variables declaration//GEN-END:variables
}