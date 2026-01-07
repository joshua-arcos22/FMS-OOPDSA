/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.flightBooking;

import com.mycompany.lipadbantayoopdsa.AdminOperations;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import com.mycompany.lipadbantayoopdsa.userAuthentication.UserDashboard;

/**
 *
 * @author Joshua
 */
public class UserBookedFlights extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserBookedFlights.class.getName());
    
    private String username;
    
    
    /**
     * Creates new form UserBookedFlights
     */
    public UserBookedFlights() {
        this("TestUser");
    }

    /**
     * Main constructor used by the application
     */
    public UserBookedFlights(String username) {
        this.username = username;
        initComponents();
        applyColorRenderer(); 
        loadUserBookings(); 
    }
    
    
    
    private String getRealTimeStatus(String flightNum) {
        String[] files = {AdminOperations.Database_TimeTable_Departure_Path, AdminOperations.Database_TimeTable_Arrivals_Path};

        for (String path : files) {
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Line Format: Airline-AcType-Origin-Dest-Freq-Time-FlightNo-Pax-Cargo-Status
                    String[] data = line.split("-");
                
                    if (data.length >= 10 && data[6].trim().equalsIgnoreCase(flightNum.trim())) {
                        return data[9].trim(); 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; 
    }
    
    
    private void applyColorRenderer() {
      
        javax.swing.table.DefaultTableCellRenderer rowRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

               
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

               
                Object statusObj = table.getValueAt(row, 0);
                String status = (statusObj != null) ? statusObj.toString().toUpperCase() : "";

               
                if (!isSelected) {
                    switch (status) {
                        case "SCHEDULED":
                            c.setBackground(new Color(200, 255, 200));
                            c.setForeground(Color.BLACK);
                            break;
                        case "DELAYED":
                            c.setBackground(new Color(255, 213, 128));
                            c.setForeground(Color.BLACK);
                            break;
                        case "CANCELED":
                            c.setBackground(new Color(255, 128, 128));
                            c.setForeground(Color.BLACK);
                            break;
                        default:
                            c.setBackground(java.awt.Color.WHITE);
                            c.setForeground(java.awt.Color.BLACK);
                            break;
                    }
                } else {
                   
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }

                return c;
            }
        };

        
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
    }
    
    
    
    
    
    private void loadUserBookings() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        String userHeader = "(" + this.username + ")";
        File file = new File(AdminOperations.Database_Bookings_Path);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean insideUser = false;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("(") && !trimmed.equals(userHeader)) {
                    insideUser = false;
                }
                if (trimmed.equals(userHeader)) {
                    insideUser = true;
                    continue;
                }

                if (insideUser && trimmed.contains(" - ")) {
                    String[] parts = trimmed.split(" - ");

                    if (parts.length >= 12) {
                        String flightNum = parts[5];

                        String oldStatus = parts[7];
                        String liveStatus = getRealTimeStatus(flightNum);
                        String statusToDisplay = (liveStatus != null) ? liveStatus : oldStatus;

                        String seat = parts[9];
                        String paymentStatus = parts[parts.length - 1];

                        model.addRow(new Object[]{
                            statusToDisplay,
                            parts[0],
                            parts[1],
                            parts[6],
                            parts[4],
                            parts[2],
                            parts[3],
                            parts[5],
                            parts[8],
                            seat,
                            paymentStatus
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    private void searchFlights() {
        String query = SearchField.getText().toLowerCase().trim();
        if (query.equals("search for a flight") || query.isEmpty()) {
            loadUserBookings();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        String userHeader = "(" + this.username + ")";
        try (BufferedReader br = new BufferedReader(new FileReader(AdminOperations.Database_Bookings_Path))) {
            String line;
            boolean insideUserSection = false;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(userHeader)) {
                    insideUserSection = true;
                    continue;
                }
                if (line.trim().startsWith("(") && !line.trim().equals(userHeader)) {
                    insideUserSection = false;
                }

                if (insideUserSection && line.toLowerCase().contains(query)) {
                    String[] parts = line.split(" - ");

                    if (parts.length >= 17) {
                        String flightNum = parts[5];
                        String oldStatus = parts[7];
                        String paymentStatus = parts[parts.length - 1];
                        String liveStatus = getRealTimeStatus(flightNum);
                        String statusToDisplay = (liveStatus != null) ? liveStatus : oldStatus;
                        String seat = parts[9];

                        model.addRow(new Object[]{
                            statusToDisplay,
                            parts[0],
                            parts[1],
                            parts[6],
                            parts[4],
                            parts[2],
                            parts[3],
                            parts[5],
                            parts[8],
                            seat,
                            paymentStatus
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void saveCancellationRequest(String requestData) {
        try {
            File file = new File(AdminOperations.Database_CancelRequests_Path);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(requestData);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error logging request.");
        }
    }
    
    private boolean isCancellationPending(String flightNumber, String seatNumber) {
        File file = new File(AdminOperations.Database_CancelRequests_Path);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                
                String[] parts = line.split(" \\| ");

                if (parts.length >= 6) {
                    boolean userMatch = parts[0].trim().equals(this.username);
                    boolean flightMatch = parts[2].trim().equalsIgnoreCase(flightNumber);
                    boolean statusMatch = line.contains("PENDING");

                    // Check if the seat matches (Index 4 based on your save format)
                    boolean seatMatch = parts[4].trim().equalsIgnoreCase(seatNumber.trim());

                    if (userMatch && flightMatch && seatMatch && statusMatch) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        panel1 = new java.awt.Panel();
        label1 = new java.awt.Label();
        SearchField = new javax.swing.JTextField();
        SearchButton = new javax.swing.JButton();
        button2 = new java.awt.Button();
        button3 = new java.awt.Button();
        label4 = new java.awt.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panel1.setBackground(new java.awt.Color(51, 153, 255));

        label1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("Book Your Journey");

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

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addGap(41, 41, 41))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 28, Short.MAX_VALUE))
        );

        button2.setLabel("Cancel Flight");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button3.setLabel("Back");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        label4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label4.setText("Your Flights");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ft. Status", "Airline", "AcType", "Day", "Time", "Origin", "Destination", "Flight Number", "Date", "Seat", "Pym. Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addGap(18, 18, 18))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(408, 408, 408)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SearchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusGained
        // TODO add your handling code here:

        SearchField.setText("");
        SearchField.setForeground(Color.BLACK);
    }//GEN-LAST:event_SearchFieldFocusGained

    private void SearchFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusLost
        // TODO add your handling code here:

        if (SearchField.getText().trim().isEmpty()) {
            SearchField.setText("Search for a flight");
            SearchField.setForeground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_SearchFieldFocusLost

    private void SearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchFieldActionPerformed
        // TODO add your handling code here:
        searchFlights();
    }//GEN-LAST:event_SearchFieldActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        // TODO add your handling code here:
        searchFlights();
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            return;
        }

        String flightNum = jTable2.getValueAt(selectedRow, 7).toString();
        String seat = jTable2.getValueAt(selectedRow, 9).toString(); 

        
        if (isCancellationPending(flightNum, seat)) {
            JOptionPane.showMessageDialog(this, "A request is already pending for this specific ticket.");
            return;
        }

        String airline = jTable2.getValueAt(selectedRow, 1).toString();
        String day = jTable2.getValueAt(selectedRow, 8).toString();

        String request = String.format("%s | %s | %s | %s | %s | REQUESTED | PENDING",
                this.username, airline, flightNum, day, seat);

        saveCancellationRequest(request);
        JOptionPane.showMessageDialog(this, "Cancellation request sent to Airline.");
    }//GEN-LAST:event_button2ActionPerformed

    
    
    
    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        new UserDashboard(this.username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_button3ActionPerformed

    
    
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
        java.awt.EventQueue.invokeLater(() -> new UserBookedFlights().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SearchButton;
    private javax.swing.JTextField SearchField;
    private java.awt.Button button2;
    private java.awt.Button button3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private java.awt.Label label1;
    private java.awt.Label label4;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
