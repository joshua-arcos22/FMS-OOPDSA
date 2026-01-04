/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.flightBooking;

import com.mycompany.lipadbantayoopdsa.Aircraftseats.a320;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.a321;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.a330;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.a350;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.atr42;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.atr72;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.b777;
import com.mycompany.lipadbantayoopdsa.Aircraftseats.q400;
import com.mycompany.lipadbantayoopdsa.flightBooking.FlightBooking;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 *
 * @author justine
 */
public class BookingDetailsPopup extends javax.swing.JFrame {
    
    private String airline, aircraft, day, time, origin, dest, flightNum, username, status, bookingDate;
    private Double calculatedPrice;
    private final double baseFare = 2500.00;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BookingDetailsPopup.class.getName());

    /**
     * Creates new form BookingDetailsPopup
     */
    public BookingDetailsPopup(String airline, String aircraft, String day, String time, String origin,
            String dest, String flightNum, String status, String bookingDate, String priceStr, String username) {

        this.airline = airline;
        this.aircraft = aircraft; // NEW
        this.day = day;
        this.time = time;
        this.origin = origin;
        this.dest = dest;
        this.flightNum = flightNum;
        this.status = status;
        this.bookingDate = bookingDate;
        this.username = username;

        // Parse the price passed from table (e.g., "3500.50")
        try {
            this.calculatedPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            this.calculatedPrice = 2500.0; // Fallback default
        }

        initComponents();

        lblSelectedFlight.setText(origin + " to " + dest + " | " + flightNum + " (" + aircraft + ")");

        ChangeListener cl = (ChangeEvent e) -> updateFareSummary();
        spnrAdult.addChangeListener(cl);
        spnrChildren.addChangeListener(cl);
        spnrInfant.addChangeListener(cl);
        spnrLuggage.addChangeListener(cl);

        this.setLocationRelativeTo(null);
        updateFareSummary();
    }
    
    private void updateFareSummary() {
        int adults = (int) spnrAdult.getValue();
        int children = (int) spnrChildren.getValue();
        int infants = (int) spnrInfant.getValue();
        int luggage = (int) spnrLuggage.getValue();

        // Use the DYNAMIC price calculated in FlightBooking
        double adultRate = this.calculatedPrice;
        double childRate = adultRate * 0.75;
        double luggageRate = 100.00;

        double adultSub = adults * adultRate;
        double childSub = children * childRate;
        double luggageSub = luggage * luggageRate;
        double total = adultSub + childSub + luggageSub;

        int totalBooked = adults + children + infants;
        int remaining = 5 - totalBooked;
        lblRemainingSeats.setText("Seats Available: " + remaining);
        lblRemainingSeats.setForeground(remaining < 0 ? java.awt.Color.RED : new java.awt.Color(0, 102, 0));

        String report = String.format(
                "<html><b>Booking Summary:</b><br>"
                + "Adults (%d x ₱%.0f): ₱%.2f<br>"
                + "Children (%d x ₱%.0f): ₱%.2f<br>"
                + "Luggage (%dkg x ₱%.0f): ₱%.2f<br>"
                + "<hr>"
                + "<b>Total Fare: ₱%.2f</b></html>",
                adults, adultRate, adultSub,
                children, childRate, childSub,
                luggage, luggageRate, luggageSub,
                total
        );
        lblSummary.setText(report);
        jButton1.setEnabled(totalBooked > 0 && totalBooked <= 5);
    }
    
    private void saveBookingToTxt(String record) {
        String filePath = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "com", "mycompany",
                "lipadbantayoopdsa", "flightBooking", "bookings.txt").toString();

        List<String> allLines = new ArrayList<>();
        String userHeader = "(" + this.username + ")";
        boolean sectionFound = false;

        try {
            File file = new File(filePath);
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        allLines.add(line);
                    }
                }
            }

            for (int i = 0; i < allLines.size(); i++) {
                if (allLines.get(i).trim().equals(userHeader)) {
                    allLines.add(i + 1, record);
                    sectionFound = true;
                    break;
                }
            }

            if (!sectionFound) {
                allLines.add(userHeader);
                allLines.add(record);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String line : allLines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save Error: " + e.getMessage());
        }
    }

    /**
     * Default constructor for the NetBeans previewer
     */
    public BookingDetailsPopup() {
        initComponents();
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHi = new javax.swing.JLabel();
        lblFlight = new javax.swing.JLabel();
        spnrAdult = new javax.swing.JSpinner();
        spnrChildren = new javax.swing.JSpinner();
        spnrInfant = new javax.swing.JSpinner();
        lblSelectedFlight = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblSummary = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblYears1 = new javax.swing.JLabel();
        lblYears2 = new javax.swing.JLabel();
        spnrLuggage = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        lblRemainingSeats = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblHi.setBackground(java.awt.Color.white);
        lblHi.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblHi.setForeground(new java.awt.Color(0, 153, 204));
        lblHi.setText("     Hi, where would you like to go?");

        lblFlight.setText("Flight:");

        spnrAdult.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));

        spnrChildren.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));

        spnrInfant.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));

        lblSelectedFlight.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSelectedFlight.setForeground(new java.awt.Color(0, 153, 204));
        lblSelectedFlight.setText("Origin, Destination, Flight Number");

        jButton1.setBackground(new java.awt.Color(0, 153, 204));
        jButton1.setForeground(java.awt.Color.white);
        jButton1.setText("Confirm Booking");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblSummary.setText("Total:");

        jLabel2.setForeground(java.awt.Color.gray);
        jLabel2.setText("Adults");

        jLabel3.setForeground(java.awt.Color.gray);
        jLabel3.setText("Children");

        jLabel4.setBackground(java.awt.Color.white);
        jLabel4.setForeground(java.awt.Color.gray);
        jLabel4.setText("Infants");

        lblYears1.setForeground(java.awt.Color.gray);
        lblYears1.setText("12+ years");

        lblYears2.setForeground(java.awt.Color.gray);
        lblYears2.setText("2 - 11 years");

        spnrLuggage.setModel(new javax.swing.SpinnerNumberModel(0, 0, 25, 1));

        jLabel5.setBackground(java.awt.Color.white);
        jLabel5.setForeground(java.awt.Color.gray);
        jLabel5.setText("Luggage (max. of 25kg)");

        lblRemainingSeats.setBackground(java.awt.Color.white);
        lblRemainingSeats.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblRemainingSeats.setForeground(java.awt.Color.red);
        lblRemainingSeats.setText("Seats Available: 5");

        btnReset.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        btnReset.setForeground(java.awt.Color.darkGray);
        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSelectedFlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(71, 71, 71))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(spnrLuggage)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblYears1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(spnrAdult, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(spnrChildren, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                        .addComponent(jLabel3)
                                        .addComponent(lblYears2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(spnrInfant, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnReset)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(48, 48, 48))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(134, 134, 134)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRemainingSeats))
                                .addGap(0, 49, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(lblFlight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSelectedFlight)
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(lblRemainingSeats))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(spnrChildren, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnrInfant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnrAdult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblYears1)
                                    .addComponent(lblYears2))
                                .addGap(0, 73, Short.MAX_VALUE))
                            .addComponent(lblSummary)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnrLuggage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int adults = (int) spnrAdult.getValue();
        int children = (int) spnrChildren.getValue();
        int infants = (int) spnrInfant.getValue();

        int totalPassengers = adults + children + infants;
        int seatsNeeded = adults + children; // Infants don't get seats

        if (totalPassengers == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one passenger.");
            return;
        }

        if (totalPassengers > 5) {
            JOptionPane.showMessageDialog(this, "Transaction limit is 5 passengers.");
            return;
        }

        // 1. Create the PARTIAL Record (Format: ... - Status - Date)
        // We do NOT add the seat here yet.
        String partialRecord = String.format("%s - %s - %s - %s - %s - %s - %s - %s - %s",
                airline, aircraft, origin, dest, time, flightNum, day, status, bookingDate);

        // 2. Pass this string to the aircraft window
        openSeatSelection(seatsNeeded, partialRecord);

        // 3. Close this popup. DO NOT SAVE HERE.
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void openSeatSelection(int seatsNeeded, String partialRecord) {
        String type = this.aircraft.toLowerCase().trim(); // e.g., "q400" or "a320"
        String price = String.valueOf(calculatedPrice);
        try {
            switch (type) {
                case "q400":
                    new q400(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "a320":
                    new a320(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "a321":
                    new a321(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "atr72":
                    new atr72(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "atr42":
                    new atr42(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "a330":
                    new a330(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "a350":
                    new a350(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                case "b777":
                    new b777(flightNum, seatsNeeded, partialRecord, username, price).setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Seat map for " + type + " not found. Booking saved without seat.");
                    // Fallback save if no seat map exists
                    saveBookingToTxt(partialRecord + " - ANY");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error opening seat map: " + e.getMessage());
        }
    }
    
    
    
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        // resets value 
        spnrAdult.setValue(0);
        spnrChildren.setValue(0);
        spnrInfant.setValue(0);
        spnrLuggage.setValue(0);

        updateFareSummary();
        lblRemainingSeats.setForeground(new java.awt.Color(0, 102, 0)); // Back to Green
    }//GEN-LAST:event_btnResetActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new BookingDetailsPopup().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblFlight;
    private javax.swing.JLabel lblHi;
    private javax.swing.JLabel lblRemainingSeats;
    private javax.swing.JLabel lblSelectedFlight;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblYears1;
    private javax.swing.JLabel lblYears2;
    private javax.swing.JSpinner spnrAdult;
    private javax.swing.JSpinner spnrChildren;
    private javax.swing.JSpinner spnrInfant;
    private javax.swing.JSpinner spnrLuggage;
    // End of variables declaration//GEN-END:variables
}
