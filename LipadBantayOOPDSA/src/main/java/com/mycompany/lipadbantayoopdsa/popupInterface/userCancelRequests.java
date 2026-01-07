/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.popupInterface;

import com.mycompany.lipadbantayoopdsa.AdminOperations;
import com.mycompany.lipadbantayoopdsa.userAuthentication.AirlineManagerDashboard;
import com.mycompany.lipadbantayoopdsa.Logs.logs;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.*;
import javax.swing.*;


/**
 *
 * @author Joshua
 */
public class userCancelRequests extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(userCancelRequests.class.getName());
    
    
    private String currentAirlineName = "TEST";
    private String currentAirlinePrefix = "TS";
    /**
     * Creates new form userTixManager
     */
    public userCancelRequests() {
        initComponents();
        loadRequests(); 
    }
    
    public userCancelRequests(String airlineName, String prefix) {
        this.currentAirlineName = airlineName;
        this.currentAirlinePrefix = prefix;
        initComponents();
        loadRequests(); 
    }

    
    
    private void loadRequests() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        File file = new File(AdminOperations.Database_CancelRequests_Path);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: User | Airline | Flight | Date | Seat | Status1 | Status2
                String[] parts = line.split(" \\| ");

                if (parts.length >= 7) {
                    String airline = parts[1];
                    if (airline.trim().equalsIgnoreCase(currentAirlineName)) {
                        model.addRow(new Object[]{
                            parts[0],
                            parts[1], 
                            parts[2], 
                            parts[3], 
                            parts[4], 
                            parts[6] 
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading requests file");
        }
    }
    
    
    private void approveCancellation() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a request to cancel.");
            return;
        }

        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();
        String targetFlight = jTable1.getValueAt(selectedRow, 2).toString();
        String targetSeat = jTable1.getValueAt(selectedRow, 4).toString();

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Confirm cancellation for " + targetUser + " on flight " + targetFlight + " (Seat: " + targetSeat + ")?",
                "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        
        int seatsToCancelCount = targetSeat.split(",").length;

        
        updateMasterFile(targetFlight, seatsToCancelCount, 0);

        boolean bookingDeleted = deleteRecordFromBookings(targetUser, targetFlight, targetSeat);
        boolean requestDeleted = deleteRecordFromRequests(targetUser, targetFlight, targetSeat);

        if (bookingDeleted || requestDeleted) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cancellation Processed.");
            loadRequests();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Could not find matching records.");
        }
    }
    
    

    private boolean deleteRecordFromBookings(String targetUser, String targetFlight, String targetSeat) {
        File file = new File(AdminOperations.Database_Bookings_Path);
        List<String> allLines = new ArrayList<>();
        boolean found = false;
        String currentUserSection = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserSection = trimmed.substring(1, trimmed.length() - 1);
                    allLines.add(line);
                    continue;
                }

                if (trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");

                    boolean matchUser = currentUserSection.trim().equalsIgnoreCase(targetUser.trim());
                   
                    boolean matchFlight = parts.length >= 6 && parts[5].trim().equalsIgnoreCase(targetFlight.trim());

                    if (matchUser && matchFlight) {
                        
                        if (targetSeat.equalsIgnoreCase("N/A")) {
                            found = true;
                            continue;
                        }

                     
                        if (parts.length >= 10) {
                            String currentSeats = parts[9];
                            String[] bookingSeats = currentSeats.split(",");
                            List<String> seatsToKeep = new ArrayList<>();
                            String[] seatsToRemove = targetSeat.split(",");

                            boolean modified = false;

                            for (String s : bookingSeats) {
                                boolean isTarget = false;
                                for (String remove : seatsToRemove) {
                                    if (s.trim().equalsIgnoreCase(remove.trim())) {
                                        isTarget = true;
                                        found = true;
                                        modified = true;
                                        break;
                                    }
                                }
                                if (!isTarget) {
                                    seatsToKeep.add(s.trim());
                                }
                            }

                            if (modified) {
                                if (seatsToKeep.isEmpty()) {
                                   
                                    continue;
                                } else {
                                    
                                    parts[9] = String.join(",", seatsToKeep);
                                    allLines.add(String.join(" - ", parts));
                                }
                            } else {
                              
                                allLines.add(line);
                            }
                        } else {
                            allLines.add(line);
                        }
                    } else {
                        allLines.add(line);
                    }
                } else {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String s : allLines) {
                    bw.write(s);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
    
    

    private boolean deleteRecordFromRequests(String targetUser, String targetFlight, String targetSeat) {
        File file = new File(AdminOperations.Database_CancelRequests_Path);
        List<String> allLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // User | Airline | Flight | Date | Seat | Status1 | Status2
                String[] parts = line.split(" \\| ");

                boolean matchUser = false;
                boolean matchFlight = false;
                boolean matchSeat = false;

                if (parts.length >= 3) {
                    matchUser = parts[0].trim().equalsIgnoreCase(targetUser.trim());
                    matchFlight = parts[2].trim().equalsIgnoreCase(targetFlight.trim());

                    if (parts.length >= 5) {
                       
                        matchSeat = parts[4].trim().equalsIgnoreCase(targetSeat.trim());
                    } else {
                        matchSeat = targetSeat.equalsIgnoreCase("N/A");
                    }
                }

                if (matchUser && matchFlight && matchSeat) {
                    found = true;
                } else {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String s : allLines) {
                    bw.write(s);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
    
    
    
    
    private void declineCancellation() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();
        String targetFlight = jTable1.getValueAt(selectedRow, 2).toString();
        String targetSeat = jTable1.getValueAt(selectedRow, 4).toString();

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Decline cancellation? This removes the request but KEEPS the booking.",
                "Confirm Decline", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        deleteRecordFromRequests(targetUser, targetFlight, targetSeat);

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"));

        String logEntry = String.format(
                "[%s]%n"
                + "EVENT  : CANCELLATION_REQUEST%n"
                + "ACTION : Declined%n"
                + "ACTOR  : ADMIN%n%n"
                + "DETAILS%n"
                + "--------%n"
                + "User        : %s%n"
                + "Flight No   : %s%n"
                + "Seat        : %s%n"
                + "Status      : DECLINED%n%n"
                + "----------------------------------------%n%n",
                timestamp, targetUser, targetFlight, targetSeat
        );

        logs.writeLog(logEntry);

        javax.swing.JOptionPane.showMessageDialog(this, "Request Declined.");
        loadRequests();
    }
    
    
    
    private void searchFlights() {
        String query = SearchField1.getText().toLowerCase().trim();

        if (query.isEmpty() || query.equals("search for a flight")) {
            loadRequests();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        File file = new File(AdminOperations.Database_CancelRequests_Path);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" \\| ");

                if (parts.length >= 7) {
                    String airline = parts[1];

                    if (airline.equalsIgnoreCase(currentAirlineName)) {

                        boolean match = false;
                        for (String part : parts) {
                            if (part.toLowerCase().contains(query)) {
                                match = true;
                                break;
                            }
                        }

                        if (match) {
                            model.addRow(new Object[]{
                                parts[0],
                                parts[1],
                                parts[2],
                                parts[3],
                                parts[5],
                                parts[6]
                            });
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in Reading File");
        }
    }
    
    
    private boolean updateMasterFile(String flightNum, int paxToSubtract, int luggageToSubtract) {
         File file = new  File(AdminOperations.Database_TimeTable_Departure_Path);
        if (!file.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Master Timetable not found.");
            return false;
        }

        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updated = false;

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
                        String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                
                
                if (parts.length >= 10 && parts[6].trim().equalsIgnoreCase(flightNum)) {
                    try {
                        int currentPax = Integer.parseInt(parts[7].trim());
                        int currentCargo = Integer.parseInt(parts[8].trim());

                       
                        int newPax = currentPax - paxToSubtract;
                        int newLuggage = currentCargo - luggageToSubtract;

                        
                        if (newPax < 0) {
                            newPax = 0; 
                        }
                        if (newLuggage < 0) {
                            newLuggage = 0;
                        }
                       
                       
                        parts[7] = String.valueOf(newPax);
                        parts[8] = String.valueOf(newLuggage);
                        
                        String newLine = String.join("-", parts);
                        allLines.add(newLine);
                        updated = true;

                    } catch (NumberFormatException e) {
                        allLines.add(line);
                    }
                } else {
                    allLines.add(line);
                }
            }
        } catch ( IOException e) { 
            System.out.println("Error in Reading File");
            return false; 
        }

      
        if (updated) {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                
                for (String s : allLines) {
                    bw.write(s);
                    bw.newLine();
                }
                return true;
            } catch ( IOException e) { 
                System.out.println("Error in Writing File");
 
                return false; 
            }
        }
        return false;
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        TopContainer = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        SearchContainer1 = new javax.swing.JPanel();
        SearchField1 = new javax.swing.JTextField();
        SearchButton1 = new javax.swing.JButton();
        BottomContainer = new javax.swing.JPanel();
        back = new javax.swing.JToggleButton();
        accept = new javax.swing.JToggleButton();
        decline = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TopContainer.setBackground(new java.awt.Color(51, 153, 255));

        Title.setFont(new java.awt.Font("Santana-Black", 0, 36)); // NOI18N
        Title.setForeground(new java.awt.Color(255, 255, 255));
        Title.setText("CANCEL REQUESTS");

        SearchContainer1.setBackground(new java.awt.Color(255, 255, 255));

        SearchField1.setText("Search for a flight");
        SearchField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchField1FocusLost(evt);
            }
        });
        SearchField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchField1ActionPerformed(evt);
            }
        });

        SearchButton1.setBackground(new java.awt.Color(0, 102, 255));
        SearchButton1.setText("Search");
        SearchButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SearchButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SearchContainer1Layout = new javax.swing.GroupLayout(SearchContainer1);
        SearchContainer1.setLayout(SearchContainer1Layout);
        SearchContainer1Layout.setHorizontalGroup(
            SearchContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchField1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
        );
        SearchContainer1Layout.setVerticalGroup(
            SearchContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(SearchButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout TopContainerLayout = new javax.swing.GroupLayout(TopContainer);
        TopContainer.setLayout(TopContainerLayout);
        TopContainerLayout.setHorizontalGroup(
            TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopContainerLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, 919, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        TopContainerLayout.setVerticalGroup(
            TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SearchContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Title))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        BottomContainer.setBackground(new java.awt.Color(255, 255, 255));

        back.setText("BACK");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        accept.setBackground(new java.awt.Color(0, 102, 204));
        accept.setText("CANCEL");
        accept.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptActionPerformed(evt);
            }
        });

        decline.setBackground(new java.awt.Color(0, 102, 204));
        decline.setText("DECLINE");
        decline.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        decline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BottomContainerLayout = new javax.swing.GroupLayout(BottomContainer);
        BottomContainer.setLayout(BottomContainerLayout);
        BottomContainerLayout.setHorizontalGroup(
            BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomContainerLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(274, 274, 274))
        );
        BottomContainerLayout.setVerticalGroup(
            BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BottomContainerLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Airline", "Flight No.", "Date", "Status "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TopContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BottomContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(TopContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BottomContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        AirlineManagerDashboard display = new AirlineManagerDashboard(currentAirlineName, currentAirlinePrefix);
        display.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void acceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptActionPerformed
        approveCancellation();
    }//GEN-LAST:event_acceptActionPerformed

    private void declineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineActionPerformed
        declineCancellation();
    }//GEN-LAST:event_declineActionPerformed

    private void SearchField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchField1FocusGained
        // FIX: Use SearchField1 instead of SearchField
        if (SearchField1.getText().equals("Search for a flight")) {
            SearchField1.setText("");
            SearchField1.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_SearchField1FocusGained

    private void SearchField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchField1FocusLost
        if (SearchField1.getText().trim().isEmpty()) {
            SearchField1.setText("Search for a flight");
            SearchField1.setForeground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_SearchField1FocusLost

    private void SearchField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchField1ActionPerformed
        searchFlights();
    }//GEN-LAST:event_SearchField1ActionPerformed

    private void SearchButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButton1ActionPerformed
        searchFlights();
    }//GEN-LAST:event_SearchButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new userCancelRequests().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomContainer;
    private javax.swing.JButton SearchButton;
    private javax.swing.JButton SearchButton1;
    private javax.swing.JButton SearchButton2;
    private javax.swing.JButton SearchButton3;
    private javax.swing.JPanel SearchContainer;
    private javax.swing.JPanel SearchContainer1;
    private javax.swing.JPanel SearchContainer2;
    private javax.swing.JPanel SearchContainer3;
    private javax.swing.JTextField SearchField;
    private javax.swing.JTextField SearchField1;
    private javax.swing.JTextField SearchField2;
    private javax.swing.JTextField SearchField3;
    private javax.swing.JLabel Title;
    private javax.swing.JPanel TopContainer;
    private javax.swing.JToggleButton accept;
    private javax.swing.JToggleButton back;
    private javax.swing.JToggleButton decline;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
