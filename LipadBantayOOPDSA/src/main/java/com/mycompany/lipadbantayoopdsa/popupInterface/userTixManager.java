/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.popupInterface;

import com.mycompany.lipadbantayoopdsa.AdminOperations;
import com.mycompany.lipadbantayoopdsa.userAuthentication.AirlineManagerDashboard;
import com.mycompany.lipadbantayoopdsa.Logs.logs;
import java.awt.Color;

/**
 *
 * @author Joshua
 */
public class userTixManager extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(userTixManager.class.getName());
    
    
    private String currentAirlineName = "TEST";
    private String currentAirlinePrefix = "TS";
    /**
     * Creates new form userTixManager
     */
    public userTixManager() {
        initComponents();
        loadBookings(); // <--- Add this line
    }
    
    public userTixManager(String airlineName, String prefix) {
        this.currentAirlineName = airlineName;
        initComponents();
        loadBookings();
    }
    
    
    
    private void loadBookings() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        java.io.File file = new java.io.File(AdminOperations.Database_Bookings_Path);
        if (!file.exists()) {
            return;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            String currentUserAccount = "Unknown"; // Stores the name found in (parentheses)

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

                // 1. Detect User Header: (joshua_123)
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    // Remove parens to get raw username
                    currentUserAccount = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }

                // 2. Parse Booking Line
                if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");

                    // Check if it belongs to THIS airline and has enough data
                    if (parts.length >= 16 && parts[0].equalsIgnoreCase(currentAirlineName)) {

                        // Map parts based on your provided format:
                        // [5] FlightNo, [8] Date, [9] Seat, [10] Price, [11] Mode, [Last] Status
                        String flightNo = parts[5];
                        String date = parts[8];
                        String seat = parts[9];
                        String price = parts[10];
                        String mode = parts[11];
                        String status = parts[parts.length - 1];

                        // Add to Table (Name col gets the User Account)
                        model.addRow(new Object[]{
                            currentUserAccount,
                            flightNo,
                            date,
                            seat,
                            price,
                            mode,
                            status
                        });
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void updateTicketStatus(String newStatus) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a ticket to update.");
            return;
        }

        // Get identifying info from the selected row
        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();   // Name/User
        String targetFlight = jTable1.getValueAt(selectedRow, 1).toString(); // Flight No
        String targetSeat = jTable1.getValueAt(selectedRow, 3).toString();   // Seat

        java.io.File file = new java.io.File(AdminOperations.Database_Bookings_Path);
        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updateSuccess = false;

        try {
            // Read all lines
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                String currentUserContext = "";

                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();

                    // Track which user section we are in
                    if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                        currentUserContext = trimmed.substring(1, trimmed.length() - 1);
                        allLines.add(line);
                        continue;
                    }

                    // Check if this is the target line
                    if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                        String[] parts = trimmed.split(" - ");

                        // Match User + Flight + Seat
                        if (currentUserContext.equals(targetUser) &&
                            parts.length >= 16 &&
                            parts[5].equals(targetFlight) &&
                            parts[9].equals(targetSeat)) {

                            // UPDATE STATUS (The last element)
                            parts[parts.length - 1] = newStatus;

                            // Rebuild the line
                            String newLine = String.join(" - ", parts);
                            allLines.add(newLine);
                            updateSuccess = true;

                            // -----------------------
                            // LOG THE STATUS CHANGE
                            // -----------------------
                            String timestamp = java.time.LocalDateTime.now()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"));

                            String logEntry = String.format(
                                "[%s]%n" +
                                "EVENT  : TICKET_STATUS_UPDATE%n" +
                                "ACTION : Ticket status changed%n" +
                                "ACTOR  : ADMIN%n%n" +
                                "DETAILS%n" +
                                "--------%n" +
                                "User          : %s%n" +
                                "Flight No     : %s%n" +
                                "Seat          : %s%n" +
                                "New Status    : %s%n%n" +
                                "----------------------------------------%n%n",
                                timestamp, targetUser, targetFlight, targetSeat, newStatus
                            );

                            logs.writeLog(logEntry); // Assuming 'logs' is your logging instance

                        } else {
                            allLines.add(line); // Not the target, keep as is
                        }
                    } else {
                        allLines.add(line); // Empty lines
                    }
                }
            }

            // Write back to file
            if (updateSuccess) {
                try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
                    for (String s : allLines) {
                        bw.write(s);
                        bw.newLine();
                    }
                }
                javax.swing.JOptionPane.showMessageDialog(this, "Ticket Status Updated to: " + newStatus);
                loadBookings(); // Refresh table
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No matching ticket found to update.");
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void searchFlights() {
        String query = SearchField.getText().toLowerCase().trim();
        
        // Reload all if search is empty
        if (query.isEmpty() || query.equals("search for a flight")) {
            loadBookings();
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        java.io.File file = new java.io.File("src/main/java/com/mycompany/lipadbantayoopdsa/flightBooking/bookings.txt");
        if (!file.exists()) return;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            String currentUserAccount = "Unknown"; // To track the header (username)

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                
                // 1. Detect User Header (e.g., "(joshua_123)")
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserAccount = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }

                // 2. Parse Booking Line
                if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");
                    
                    // Check if it belongs to THIS airline
                    if (parts.length >= 16 && parts[0].equalsIgnoreCase(currentAirlineName)) {
                        
                        // 3. SEARCH CHECK: Does the query match Name, Flight, Date, or Status?
                        boolean match = false;
                        
                        // Check Username
                        if (currentUserAccount.toLowerCase().contains(query)) match = true;
                        
                        // Check other fields in the line
                        for (String part : parts) {
                            if (part.toLowerCase().contains(query)) {
                                match = true;
                                break;
                            }
                        }

                        if (match) {
                            // Add to Table
                            model.addRow(new Object[]{
                                currentUserAccount,       // Name
                                parts[5],                 // Flight No
                                parts[8],                 // Date
                                parts[9],                 // Seat
                                parts[10],                // Price
                                parts[11],                // Mode
                                parts[parts.length - 1]   // Status
                            });
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        TopContainer = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        SearchContainer1 = new javax.swing.JPanel();
        SearchField = new javax.swing.JTextField();
        SearchButton = new javax.swing.JButton();
        BottomContainer = new javax.swing.JPanel();
        back = new javax.swing.JToggleButton();
        accept = new javax.swing.JToggleButton();
        pending = new javax.swing.JToggleButton();
        decline = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TopContainer.setBackground(new java.awt.Color(51, 153, 255));

        Title.setFont(new java.awt.Font("Santana-Black", 0, 36)); // NOI18N
        Title.setForeground(new java.awt.Color(255, 255, 255));
        Title.setText("TICKET MANAGER");

        SearchContainer1.setBackground(new java.awt.Color(255, 255, 255));

        SearchField.setText("Search for a flight");
        SearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchFieldFocusLost(evt);
            }
        });
        SearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchFieldActionPerformed(evt);
            }
        });

        SearchButton.setBackground(new java.awt.Color(0, 102, 255));
        SearchButton.setText("Search");
        SearchButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SearchContainer1Layout = new javax.swing.GroupLayout(SearchContainer1);
        SearchContainer1.setLayout(SearchContainer1Layout);
        SearchContainer1Layout.setHorizontalGroup(
            SearchContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
        );
        SearchContainer1Layout.setVerticalGroup(
            SearchContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainer1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(SearchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout TopContainerLayout = new javax.swing.GroupLayout(TopContainer);
        TopContainer.setLayout(TopContainerLayout);
        TopContainerLayout.setHorizontalGroup(
            TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopContainerLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        TopContainerLayout.setVerticalGroup(
            TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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

        accept.setBackground(new java.awt.Color(153, 255, 153));
        accept.setText("ACCEPT");
        accept.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptActionPerformed(evt);
            }
        });

        pending.setBackground(new java.awt.Color(255, 204, 153));
        pending.setText("PENDING");
        pending.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingActionPerformed(evt);
            }
        });

        decline.setBackground(new java.awt.Color(255, 102, 102));
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
                .addGap(253, 253, 253)
                .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pending, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BottomContainerLayout.setVerticalGroup(
            BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BottomContainerLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accept, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pending, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decline, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Flight No.", "Date", "Seat", "Price", "Mode", "Status "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        updateTicketStatus("ACCEPTED");
    }//GEN-LAST:event_acceptActionPerformed

    private void pendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingActionPerformed
        updateTicketStatus("PENDING");
    }//GEN-LAST:event_pendingActionPerformed

    private void declineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineActionPerformed
        updateTicketStatus("DECLINED");
    }//GEN-LAST:event_declineActionPerformed

    private void SearchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusGained
        SearchField.setText("");
        SearchField.setForeground(Color.BLACK);
    }//GEN-LAST:event_SearchFieldFocusGained

    private void SearchFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusLost
        if (SearchField.getText().trim().isEmpty()) {
            SearchField.setText("Search for a flight");
            SearchField.setForeground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_SearchFieldFocusLost

    private void SearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchFieldActionPerformed
        searchFlights();
    }//GEN-LAST:event_SearchFieldActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        searchFlights();
    }//GEN-LAST:event_SearchButtonActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new userTixManager().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomContainer;
    private javax.swing.JButton SearchButton;
    private javax.swing.JPanel SearchContainer1;
    private javax.swing.JTextField SearchField;
    private javax.swing.JLabel Title;
    private javax.swing.JPanel TopContainer;
    private javax.swing.JToggleButton accept;
    private javax.swing.JToggleButton back;
    private javax.swing.JToggleButton decline;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton pending;
    // End of variables declaration//GEN-END:variables
}
