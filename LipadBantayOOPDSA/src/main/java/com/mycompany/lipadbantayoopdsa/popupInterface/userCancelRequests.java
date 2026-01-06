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
public class userCancelRequests extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(userCancelRequests.class.getName());
    
    
    private String currentAirlineName = "TEST";
    private String currentAirlinePrefix = "TS";
    /**
     * Creates new form userTixManager
     */
    public userCancelRequests() {
        initComponents();
        loadRequests(); // <--- Add this
    }
    
    public userCancelRequests(String airlineName, String prefix) {
        this.currentAirlineName = airlineName;
        this.currentAirlinePrefix = prefix;
        initComponents();
        loadRequests(); // <--- Add this
    }

    
    
    private void loadRequests() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        java.io.File file = new java.io.File(AdminOperations.Database_CancelRequests_Path);
        if (!file.exists()) {
            return;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                // TRY SPLITTING BY PIPE FIRST (Your current code's expectation)
                String[] parts = trimmed.split(" \\| ");

                // IF PIPE FAILS, TRY DASH (Your other file's format)
                if (parts.length < 2) {
                    parts = trimmed.split(" - ");
                }

                // NOW CHECK LENGTH
                if (parts.length >= 5) {
                    // Extract Airline (Adjust index based on your specific file format)
                    // If format: User | Airline | FlightNo | Date | ... | Status
                    String airline = parts[1];

                    if (airline.trim().equalsIgnoreCase(currentAirlineName)) {
                        model.addRow(new Object[]{
                            parts[0], // Name
                            parts[1], // Airline
                            parts[2], // Flight No
                            parts[3], // Date
                            parts[parts.length - 1] // Status (Last element is safest)
                        });
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void approveCancellation() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a request to cancel.");
            return;
        }

        // 1. Get Identifiers from Table
        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();
        String targetFlight = jTable1.getValueAt(selectedRow, 2).toString();

        // 2. Confirmation Dialog
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to CANCEL this booking? \nThis will permanently delete the record and free up seats.", 
            "Confirm Cancellation", javax.swing.JOptionPane.YES_NO_OPTION);
            
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        // 3. RETRIEVE INFO (Pax/Luggage) from bookings.txt before deleting
        java.io.File bookingFile = new java.io.File(AdminOperations.Database_Bookings_Path);
        int paxToRemove = 0;
        int luggageToRemove = 0;
        boolean foundInfo = false;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(bookingFile))) {
            String line;
            String currentUserContext = "";
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                
                // Handle User Header (e.g., (joshua_123))
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserContext = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }
                
                // Find the specific flight line for this user
                if (currentUserContext.equals(targetUser) && trimmed.contains(" - " + targetFlight + " - ")) {
                    String[] parts = trimmed.split(" - ");

                    // NEW FORMAT: Check Index 10 for Luggage
                    if (parts.length >= 11) {
                        try {
                            // Pax is NOT at index 9 anymore? 
                            // Wait, your format is: Date(8) - Seats(9) - Luggage(10)
                            // We need Pax count. Pax count is derived from Seats(9).

                            String seatString = parts[9].trim();
                            paxToRemove = seatString.split(",").length; // Count seats

                            luggageToRemove = Integer.parseInt(parts[10].trim()); // Luggage

                            foundInfo = true;
                            break;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (java.io.IOException e) { e.printStackTrace(); }

        // 4. Update Master File (Subtract) if info was found
        if (foundInfo) {
            updateMasterFile(targetFlight, paxToRemove, luggageToRemove);
        } else {
            // Optional warning if you want to know if logic failed
            System.out.println("Warning: Could not find booking details to update capacity.");
        }

        // 5. DELETE RECORDS (Existing Logic)
        boolean bookingDeleted = deleteRecordFromBookings(targetUser, targetFlight);
        boolean requestDeleted = deleteRecordFromRequests(targetUser, targetFlight);

        if (bookingDeleted || requestDeleted) {
            javax.swing.JOptionPane.showMessageDialog(this, "Booking Cancelled. Seats/Luggage returned to inventory.");
            loadRequests(); // Refresh Table
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Could not find records to delete.");
        }
    }

    // Helper: Deletes specific flight line under specific user in bookings.txt
    private boolean deleteRecordFromBookings(String targetUser, String targetFlight) {
        java.io.File file = new java.io.File(AdminOperations.Database_Bookings_Path);
        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean found = false;
        String currentUserSection = "";

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

                // Check which user section we are in
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserSection = trimmed.substring(1, trimmed.length() - 1);
                    allLines.add(line);
                    continue;
                }

                // Check if this line matches the Flight AND we are in the correct User Section
                // Format: Airline - ... - FlightNo - ...
                if (currentUserSection.equals(targetUser) && trimmed.contains(" - " + targetFlight + " - ")) {
                    found = true; 
                    // SKIP adding this line (effectively deleting it)
                } else {
                    allLines.add(line);
                }
            }
        } catch (java.io.IOException e) { e.printStackTrace(); return false; }

        // Write Back
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
            for (String s : allLines) {
                bw.write(s);
                bw.newLine();
            }
        } catch (java.io.IOException e) { e.printStackTrace(); return false; }
        
        return found;
    }

    // Helper: Deletes the line from cancellation_requests.txt
    private boolean deleteRecordFromRequests(String targetUser, String targetFlight) {
        java.io.File file = new java.io.File(AdminOperations.Database_CancelRequests_Path);
        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean found = false;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: User | Airline | FlightNo ...
                String[] parts = line.split(" \\| ");
                
                if (parts.length >= 3 && parts[0].equals(targetUser) && parts[2].equals(targetFlight)) {
                    found = true; 
                    // SKIP adding this line
                } else {
                    allLines.add(line);
                }
            }
        } catch (java.io.IOException e) { e.printStackTrace(); return false; }

        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
            for (String s : allLines) {
                bw.write(s);
                bw.newLine();
            }
        } catch (java.io.IOException e) { e.printStackTrace(); return false; }
        
        return found;
    }
    
    
    
    private void declineCancellation() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a request to decline.");
            return;
        }

        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();
        String targetFlight = jTable1.getValueAt(selectedRow, 2).toString();

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
            "Decline this cancellation request?", "Confirm Decline", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        java.io.File file = new java.io.File(AdminOperations.Database_CancelRequests_Path);
        java.util.List<String> allLines = new java.util.ArrayList<>();

        try {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" \\| ");

                    // Match and Update Status to DECLINED
                    if (parts.length >= 6 && parts[0].equals(targetUser) && parts[2].equals(targetFlight)) {
                        parts[5] = "DECLINED"; 
                        allLines.add(String.join(" | ", parts));
                    } else {
                        allLines.add(line);
                    }
                }
            }

            // Write back updated file
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
                for (String s : allLines) {
                    bw.write(s);
                    bw.newLine();
                }
            }

            // --- LOGGING ---
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"));

            String logEntry = String.format(
                "[%s]%n" +
                "EVENT  : CANCELLATION_REQUEST%n" +
                "ACTION : Declined%n" +
                "ACTOR  : ADMIN%n%n" +
                "DETAILS%n" +
                "--------%n" +
                "User       : %s%n" +
                "Flight No  : %s%n" +
                "Status     : DECLINED%n%n" +
                "----------------------------------------%n%n",
                timestamp, targetUser, targetFlight
            );

            logs.writeLog(logEntry);

            javax.swing.JOptionPane.showMessageDialog(this, "Request Declined.");
            loadRequests(); 

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void searchFlights() {
        String query = SearchField1.getText().toLowerCase().trim();

        if (query.isEmpty() || query.equals("search for a flight")) {
            loadRequests();
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        java.io.File file = new java.io.File(AdminOperations.Database_CancelRequests_Path);
        if (!file.exists()) {
            return;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: User | Airline | FlightNo | Day | Origin to Dest | Status
                String[] parts = line.split(" \\| ");

                if (parts.length >= 6) {
                    String airline = parts[1];

                    // 1. Must belong to CURRENT Airline
                    if (airline.equalsIgnoreCase(currentAirlineName)) {

                        // 2. Check if ANY field contains the query
                        boolean match = false;
                        for (String part : parts) {
                            if (part.toLowerCase().contains(query)) {
                                match = true;
                                break;
                            }
                        }

                        if (match) {
                            model.addRow(new Object[]{
                                parts[0], // Name
                                parts[1], // Airline
                                parts[2], // Flight No.
                                parts[3], // Date
                                parts[5] // Status
                            });
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private boolean updateMasterFile(String flightNum, int paxToSubtract, int luggageToSubtract) {
        java.io.File file = new java.io.File(AdminOperations.Database_TimeTable_Departure_Path);
        if (!file.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Master Timetable not found.");
            return false;
        }

        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updated = false;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                
                // Check Flight Number (Index 6)
                if (parts.length >= 10 && parts[6].trim().equalsIgnoreCase(flightNum)) {
                    try {
                        int currentPax = Integer.parseInt(parts[7].trim());
                        int currentCargo = Integer.parseInt(parts[8].trim());

                        // --- SUBTRACT VALUES ---
                        int newPax = currentPax - paxToSubtract;
                        int newLuggage = currentCargo - luggageToSubtract;

                        // --- SAFETY MEASURE: PREVENT NEGATIVES ---
                        if (newPax < 0) {
                            newPax = 0; 
                        }
                        if (newLuggage < 0) {
                            newLuggage = 0;
                        }
                        // ------------------------------------------

                        // Update array
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
        } catch (java.io.IOException e) { 
            e.printStackTrace(); 
            return false; 
        }

        // Write changes back
        if (updated) {
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
                for (String s : allLines) {
                    bw.write(s);
                    bw.newLine();
                }
                return true;
            } catch (java.io.IOException e) { 
                e.printStackTrace(); 
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
