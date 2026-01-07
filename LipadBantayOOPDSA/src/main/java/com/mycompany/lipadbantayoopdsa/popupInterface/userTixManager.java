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
        loadBookings(); 
    }
    
    public userTixManager(String airlineName, String prefix) {
        this.currentAirlineName = airlineName;
        this.currentAirlinePrefix = prefix;
        initComponents();
        loadBookings();
    }
    
    
    
    private void loadBookings() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

       
        File file = new File(AdminOperations.Database_Bookings_Path);
        if (!file.exists()) {
            System.out.println("Bookings file not found at: " + file.getAbsolutePath());
            return;
        }

        try  {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String currentUserAccount = "Unknown";

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

              
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserAccount = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }

                
                if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");

                    
                    if (parts.length > 0 && parts[0].equalsIgnoreCase(currentAirlineName)) {

                        String flightNo = "";
                        String date = "";
                        String seat = "";
                        String price = "";
                        String mode = "";
                        String status = "";

                     
                        if (parts.length >= 17) {
                            flightNo = parts[5];
                            date = parts[8];
                            seat = parts[9];
                           
                            price = parts[11];
                            mode = parts[12];
                            status = parts[parts.length - 1];
                        }
                        else if (parts.length >= 16) {
                            flightNo = parts[5];
                            date = parts[8];
                            seat = parts[9];
                            price = parts[10]; 
                            mode = parts[11];
                            status = parts[parts.length - 1];
                        } else {
                            continue;
                        }

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
        } catch (  IOException e) {
            System.out.println("Error in reading File");
        }
    }
    
    private void updateTicketStatus(String newStatus) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a ticket to update.");
            return;
        }

        // Get valeus from the selectrd row on the table
        String targetUser = jTable1.getValueAt(selectedRow, 0).toString();   
        String targetFlight = jTable1.getValueAt(selectedRow, 1).toString(); 
        String targetSeat = jTable1.getValueAt(selectedRow, 3).toString();   

        File file = new   File(AdminOperations.Database_Bookings_Path);
        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updateSuccess = false;
            
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                
                String line;
                String currentUserContext = "";

                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();

                    
                    if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                        currentUserContext = trimmed.substring(1, trimmed.length() - 1);
                        allLines.add(line);
                        continue;
                    }

                   
                    if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                        String[] parts = trimmed.split(" - ");

                      
                        if (currentUserContext.equals(targetUser) &&
                            parts.length >= 16 &&
                            parts[5].equals(targetFlight) &&
                            parts[9].equals(targetSeat)) {

                           
                            parts[parts.length - 1] = newStatus;

                            
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

                            logs.writeLog(logEntry); 

                        } else {
                            allLines.add(line);
                        }
                    } else {
                        allLines.add(line);
                    }
                }
            } catch (IOException e ){
                System.out.println("Error in updating the Status");
            }

            
            if (updateSuccess) {
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                                        for (String s : allLines) {
                        bw.write(s);
                        bw.newLine();
                    }
                } catch (IOException e){
                    System.out.println("Error in Writing the file");
                }
                javax.swing.JOptionPane.showMessageDialog(this, "Ticket Status Updated to: " + newStatus);
                loadBookings(); 
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No matching ticket found to update.");
            }

} 
    
    
    
    
    
    private void searchFlights() {
        String query = SearchField.getText().toLowerCase().trim();
        
      
        if (query.isEmpty() || query.equals("search for a flight")) {
            loadBookings();
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); 

        File file = new File(AdminOperations.Database_Bookings_Path);
        if (!file.exists()) return;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentUserAccount = "Unknown"; 

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                
              
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserAccount = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }

                
                if (!trimmed.isEmpty() && trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");
                    
                 
                    if (parts.length >= 16 && parts[0].equalsIgnoreCase(currentAirlineName)) {
                        
                       
                        boolean match = false;
                        
                       
                        if (currentUserAccount.toLowerCase().contains(query)) match = true;
                        
                       
                        for (String part : parts) {
                            if (part.toLowerCase().contains(query)) {
                                match = true;
                                break;
                            }
                        }

                        if (match) {
                            
                            model.addRow(new Object[]{
                                currentUserAccount,       
                                parts[5],                 
                                parts[8],                 
                                parts[9],                 
                                parts[11],                
                                parts[12],                
                                parts[parts.length - 1]   
                            });
                        }
                    }
                }
            }
        } catch (  IOException e) {
            System.out.println("Error in Searching Flights");
        }
    }
    
    private boolean updateMasterFile(String flightNum, int paxToAdd, int luggageToAdd) {
          File file = new File(AdminOperations.Database_TimeTable_Departure_Path);
        if (!file.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Master Timetable file not found.");
            return false;
        }

        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updated = false;

        try(BufferedReader br = new BufferedReader(new FileReader(file)))  {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");

              
                if (parts.length >= 10 && parts[6].trim().equalsIgnoreCase(flightNum)) {
                    try {
                        int currentPax = Integer.parseInt(parts[7].trim());
                        int currentCargo = Integer.parseInt(parts[8].trim());

                       
                        int newPax = currentPax + paxToAdd;
                        int newLuggage = currentCargo + luggageToAdd;

                       
                        if (newPax < 0) {
                            newPax = 0;
                        }
                        if (newLuggage < 0) {
                            newLuggage = 0;
                        }

                        parts[7] = String.valueOf(newPax);
                        parts[8] = String.valueOf(newLuggage);

                        allLines.add(String.join("-", parts));
                        updated = true;

                    } catch (NumberFormatException e) {
                        allLines.add(line);
                    }
                } else {
                    allLines.add(line);
                }
            }
        } catch (  IOException e) {
            System.out.println("Error in Updating the File");
            return false;
        }

        if (updated) {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                
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
    
    
    
    private void processCapacityChange(String targetUser, String targetFlight, String targetSeat, boolean isAdding) {
          File bookingFile = new File(AdminOperations.Database_Bookings_Path);
        int paxCount = 0;
        int luggageCount = 0;
        boolean found = false;

    
        if (targetSeat != null && !targetSeat.isEmpty()) {
            paxCount = targetSeat.split(",").length;
        }

       
        try(BufferedReader br = new BufferedReader(new FileReader(bookingFile))) {
            
            String line;
            String currentUserContext = "";
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
                    currentUserContext = trimmed.substring(1, trimmed.length() - 1);
                    continue;
                }
                
                if (currentUserContext.equals(targetUser) && 
                    trimmed.contains(" - " + targetFlight + " - ") &&
                    trimmed.contains(" - " + targetSeat + " - ")) {
                    
                    String[] parts = trimmed.split(" - ");
                    if (parts.length >= 11) {
                        try {
                            luggageCount = Integer.parseInt(parts[10].trim());
                            found = true;
                            break;
                        } catch (NumberFormatException e) {
                            luggageCount = 0; 
                        }
                    }
                }
            }
        } catch (Exception e) { 
            System.out.println("Error in updating The PAX"); 
        }

      
        int finalPax = isAdding ? paxCount : -paxCount;
        int finalLuggage = isAdding ? luggageCount : -luggageCount;
        
        updateMasterFile(targetFlight, finalPax, finalLuggage);
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
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a ticket.");
            return;
        }

        String currentStatus = jTable1.getValueAt(selectedRow, 6).toString();

       
        if (currentStatus.equalsIgnoreCase("ACCEPTED")) {
            javax.swing.JOptionPane.showMessageDialog(this, "This ticket is already accepted.");
            return;
        }

        String user = jTable1.getValueAt(selectedRow, 0).toString();
        String flight = jTable1.getValueAt(selectedRow, 1).toString();
        String seat = jTable1.getValueAt(selectedRow, 3).toString();

        
        processCapacityChange(user, flight, seat, true);

        updateTicketStatus("ACCEPTED");
    }//GEN-LAST:event_acceptActionPerformed

    private void pendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a ticket.");
            return;
        }

        String currentStatus = jTable1.getValueAt(selectedRow, 6).toString();
        String user = jTable1.getValueAt(selectedRow, 0).toString();
        String flight = jTable1.getValueAt(selectedRow, 1).toString();
        String seat = jTable1.getValueAt(selectedRow, 3).toString();

       
        if (currentStatus.equalsIgnoreCase("ACCEPTED")) {
            processCapacityChange(user, flight, seat, false); 
        }

        updateTicketStatus("PENDING");
    }//GEN-LAST:event_pendingActionPerformed

    private void declineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a ticket.");
            return;
        }

        String currentStatus = jTable1.getValueAt(selectedRow, 6).toString();
        String user = jTable1.getValueAt(selectedRow, 0).toString();
        String flight = jTable1.getValueAt(selectedRow, 1).toString();
        String seat = jTable1.getValueAt(selectedRow, 3).toString();

       
        if (currentStatus.equalsIgnoreCase("ACCEPTED")) {
            processCapacityChange(user, flight, seat, false); 
        }

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
