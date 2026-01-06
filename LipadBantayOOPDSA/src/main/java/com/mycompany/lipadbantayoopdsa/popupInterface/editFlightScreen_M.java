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
public class editFlightScreen_M extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editFlightScreen_M.class.getName());
    
        private String Airline_Name;
        private String Ac_Type;
        private String Origin_Airport;
        private String Destination_Airport;
        private String Frequency = "";
        private String Time;
        private String Flight_Number;
        private String Original_Flight_Number_Ref;
        public String FLNO_Prefix;
        
        
      
        
        public editFlightScreen_M(
            String airline,
            String aircraft,
            String origin,
            String destination,
            String frequency,
            String time,
            String flightNumber,
            String FL_Prefix
        ) {
            initComponents();

            this.Original_Flight_Number_Ref = flightNumber;
            this.Airline_Name = airline;
            flnField.setText(flightNumber);
            timeField.setText(time);
            FLNO_Prefix = FL_Prefix;
             //initializes value
            errorAIRLINENAME.setText(airline);
            errorFLTNO.setText(" ");
            errorTIME.setText(" ");
            originRWYLn.setText(" ");
            destRWYLn.setText(" ");
            acRWYLn.setText(" ");
            errorCARGO.setText(" ");
            errorPAX.setText(" ");
            
            
            

            // handle frequency checkboxes
            setFrequencyChecks(frequency);
            
            ac_drpdwn.removeAllItems();
            
            try {
                File acOpenMasyer = new File(AdminOperations.Database_Aircarfts_Path);
                Scanner acReader = new Scanner(acOpenMasyer);
                while(acReader.hasNextLine()){
                    String acLineReader = acReader.nextLine();
                    String acLineReaderArray[] = acLineReader.split("-");
                    ac_drpdwn.addItem(acLineReaderArray[0]);
                      
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found ");
            }
            ac_drpdwn.setSelectedItem(aircraft);
            
            

            
            //initialize origin airport dropdown
            origin_drpdwn.removeAllItems();
            try {
                File acOpenMasyer = new File(AdminOperations.Database_Aiports_Path);
                Scanner aprtReader = new Scanner(acOpenMasyer);
                while(aprtReader.hasNextLine()){
                    String aprtLineReader = aprtReader.nextLine();
                    String aprtLineReaderArray[] = aprtLineReader.split("-");
                    origin_drpdwn.addItem(aprtLineReaderArray[1] + "(" +aprtLineReaderArray[0] + ")" );
                    
                }
                aprtReader.close();
             } catch (FileNotFoundException e) {
                System.out.println("File not found ");
            }
            int i;
            for (i = 0; i < origin_drpdwn.getItemCount(); i++) {
                String orgndrpAp = origin_drpdwn.getItemAt(i).toString().substring(0, 4);
                if (orgndrpAp.equals(origin.substring(0,4))) {
                    break;
                }
            }
            origin_drpdwn.setSelectedIndex(i);
            
            
            



            //initializes desitantion aiport drop down
            destination_drpdwn.removeAllItems();
            try {
                File acOpenMasyer = new File(AdminOperations.Database_Aiports_Path);
                Scanner aprtReader = new Scanner(acOpenMasyer);
                while(aprtReader.hasNextLine()){
                    String aprtLineReader = aprtReader.nextLine();
                    String aprtLineReaderArray[] = aprtLineReader.split("-");
                    destination_drpdwn.addItem(aprtLineReaderArray[1] + "(" +aprtLineReaderArray[0] + ")" );
                    
                } 
                aprtReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found ");
            }
            int y;
            for (y = 0; y < destination_drpdwn.getItemCount(); y++) {
                String destdrpAp = destination_drpdwn.getItemAt(y).toString().substring(0, 4);
                if (destdrpAp.equals(destination.substring(0,4))) {
                    break;
                }
            }
            destination_drpdwn.setSelectedIndex(y);
            
            
//            System.out.println(origin);
//            System.out.println(destination);

            //updates the field if there are changes 
            java.awt.event.ActionListener updateAction = new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    updateRunwayStatus();
                }
            };

            origin_drpdwn.addActionListener(updateAction);
            destination_drpdwn.addActionListener(updateAction);
            ac_drpdwn.addActionListener(updateAction);

            // Run it once initially to set the correct labels for default selections
            updateRunwayStatus();
            
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

        origin_drpdwn2 = new javax.swing.JComboBox<>();
        bodyContainer = new javax.swing.JPanel();
        topContainer = new javax.swing.JPanel();
        fucntionTitle = new javax.swing.JLabel();
        ARILINECODE = new javax.swing.JLabel();
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
        paxFIELD = new javax.swing.JTextField();
        FLNO3 = new javax.swing.JLabel();
        ARILINECODE1 = new javax.swing.JLabel();
        FLNO2 = new javax.swing.JLabel();
        cargoFIELD = new javax.swing.JTextField();
        errorCARGO = new javax.swing.JLabel();
        errorPAX = new javax.swing.JLabel();
        status_drpdwn = new javax.swing.JComboBox<>();

        origin_drpdwn2.setBackground(new java.awt.Color(0, 153, 204));
        origin_drpdwn2.setForeground(new java.awt.Color(255, 255, 255));
        origin_drpdwn2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        origin_drpdwn2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
        flnField.setText("FL1234");
        flnField.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btn_back.setText("BACK");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        errorAIRLINENAME.setForeground(new java.awt.Color(51, 51, 51));
        errorAIRLINENAME.setText("placeholder");

        errorFLTNO.setText("placeholder");

        errorTIME.setText("placeholder");

        originRWYLn.setText("placeholder");

        destRWYLn.setText("placeholder");

        acRWYLn.setText("placeholder");

        paxFIELD.setBackground(new java.awt.Color(0, 153, 204));
        paxFIELD.setForeground(new java.awt.Color(255, 255, 255));
        paxFIELD.setText("0");
        paxFIELD.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        paxFIELD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paxFIELDActionPerformed(evt);
            }
        });

        FLNO3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FLNO3.setText("PAX");

        ARILINECODE1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ARILINECODE1.setText("STATUS");

        FLNO2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        FLNO2.setText("CARGO");

        cargoFIELD.setBackground(new java.awt.Color(0, 153, 204));
        cargoFIELD.setForeground(new java.awt.Color(255, 255, 255));
        cargoFIELD.setText("0");
        cargoFIELD.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        errorCARGO.setText("placeholder");

        errorPAX.setText("placeholder");

        status_drpdwn.setBackground(new java.awt.Color(0, 153, 204));
        status_drpdwn.setForeground(new java.awt.Color(255, 255, 255));
        status_drpdwn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        status_drpdwn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout bodyContainerLayout = new javax.swing.GroupLayout(bodyContainer);
        bodyContainer.setLayout(bodyContainerLayout);
        bodyContainerLayout.setHorizontalGroup(
            bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bodyContainerLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ARILINECODE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(origin_drpdwn, 0, 135, Short.MAX_VALUE)
                            .addComponent(originRWYLn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ORIGIN, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(errorAIRLINENAME, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ARILINECODE1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(status_drpdwn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(destRWYLn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(destination_drpdwn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(bodyContainerLayout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(DESTINATION, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(timeField, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(errorTIME, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TIME, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(FLNO3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(paxFIELD)
                                .addComponent(errorPAX, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(55, 55, 55)
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(FLNO2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cargoFIELD)
                                .addComponent(errorCARGO, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(82, 82, 82))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(btn_addFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        bodyContainerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ac_drpdwn, destination_drpdwn, flnField, origin_drpdwn, timeField});

        bodyContainerLayout.setVerticalGroup(
            bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyContainerLayout.createSequentialGroup()
                .addComponent(topContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(errorAIRLINENAME))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                        .addComponent(TIME, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(errorTIME)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyContainerLayout.createSequentialGroup()
                        .addGroup(bodyContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addComponent(FLNO3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(paxFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bodyContainerLayout.createSequentialGroup()
                                .addComponent(ARILINECODE1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(status_drpdwn)))
                        .addGap(13, 13, 13)
                        .addComponent(errorPAX)
                        .addGap(37, 37, 37))
                    .addGroup(bodyContainerLayout.createSequentialGroup()
                        .addComponent(FLNO2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cargoFIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(errorCARGO)
                        .addGap(18, 18, Short.MAX_VALUE)))
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

          
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

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
    
    private void verifyAndAddRoute(String aircraft, String origin, String destination) {
        String projectRoot = System.getProperty("user.dir");
        // Use the class variable 'this.Airline_Name' which is set in the constructor
        String filename = "Routes_" + this.Airline_Name.trim().toUpperCase() + ".txt";

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
    
    
    
    
    
    private void btn_addFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addFlightActionPerformed
       
        // 1. GET RAW INPUTS
        String selectedAc = ac_drpdwn.getSelectedItem().toString().trim();
        String flightNumInput = flnField.getText().trim().toUpperCase();
        String timeInput = timeField.getText().trim();
        String paxInput = paxFIELD.getText().trim();
        String cargoInput = cargoFIELD.getText().trim();
        String statusInput = status_drpdwn.getSelectedItem().toString();

        // 2. VALIDATION FLAGS
        boolean Flt_No_Valid = true;
        boolean time_Valid = true;
        boolean runway_Valid = true;
        boolean pax_Valid = true;
        boolean cargo_Valid = true;

        // --- A. FLIGHT NUMBER VALIDATION ---
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
        } else if (!flightNumInput.substring(0, 2).equalsIgnoreCase(FLNO_Prefix)) {
            errorFLTNO.setText("Must Start w/ " + FLNO_Prefix);
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
        } else if (!flightNumInput.equalsIgnoreCase(Original_Flight_Number_Ref) && isFlightNumberDuplicate(flightNumInput)) {
            errorFLTNO.setText("Flight Exists");
            errorFLTNO.setForeground(Color.red);
            Flt_No_Valid = false;
        } else {
            errorFLTNO.setText(" ");
            this.Flight_Number = flightNumInput;
        }

        // --- B. TIME VALIDATION ---
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

        // --- C. AIRCRAFT LIMITS ---
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

        // --- D. RUNWAY CHECK ---
        if (originRWYLn.getForeground() == Color.red || destRWYLn.getForeground() == Color.red || acRWYLn.getForeground() == Color.red) {
            runway_Valid = false;
        }

        // 3. FINAL EXECUTION
        if (Flt_No_Valid && time_Valid && runway_Valid && pax_Valid && cargo_Valid) {

            this.Ac_Type = selectedAc;

            // --- CHANGE 1: Capture BOTH Short and Long names ---
            // Short Code (e.g., "RPVM") for the Timetable
            this.Origin_Airport = origin_drpdwn.getSelectedItem().toString().substring(0, 4).trim();
            this.Destination_Airport = destination_drpdwn.getSelectedItem().toString().substring(0, 4).trim();

            // Full Name (e.g., "RPVM(Cebu)") for the Route File
            String originFull = origin_drpdwn.getSelectedItem().toString().trim();
            String destFull = destination_drpdwn.getSelectedItem().toString().trim();
            // ----------------------------------------------------

            this.Frequency = getFrequencyString();

            // Use Short Codes for Timetable (Standard)
            AdminOperations ops = new AdminOperations(
                    Airline_Name, Ac_Type, Origin_Airport, Destination_Airport,
                    Frequency, Time, Flight_Number, paxInput, cargoInput, statusInput
            );

            try {
                ops.Admin_EditFlight(this.Original_Flight_Number_Ref);

                // --- CHANGE 2: Pass the FULL names to the verify method ---
                verifyAndAddRoute(Ac_Type, originFull, destFull);
                // ----------------------------------------------------------

                javax.swing.JOptionPane.showMessageDialog(this, "Flight Edited Successfully!");
                ArrivalTimetableGenerator.generate();
                dispose();
            } catch (IOException e) {
                System.out.println("Error Saving Flight");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fix the errors in red.", "Validation Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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

    private void paxFIELDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paxFIELDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paxFIELDActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new editFlightScreen_M(
        "AIRLINE1",  // airline
        "B737",      // aircraft
        "MNLA",       // origin
        "CEBU",       // destination
        "M/TU/W",    // frequency
        "0930",      // time
        "AB1234",     // flight number
        "TS"
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
    private javax.swing.JComboBox<String> origin_drpdwn2;
    private javax.swing.JTextField paxFIELD;
    private javax.swing.JComboBox<String> status_drpdwn;
    private javax.swing.JTextField timeField;
    private javax.swing.JPanel topContainer;
    // End of variables declaration//GEN-END:variables
}