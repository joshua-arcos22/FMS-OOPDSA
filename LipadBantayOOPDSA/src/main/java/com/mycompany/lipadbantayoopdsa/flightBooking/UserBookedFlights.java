/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.flightBooking;

import com.mycompany.lipadbantayoopdsa.AdminOperations;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.mycompany.lipadbantayoopdsa.userAuthentication.UserDashboard;

/**
 *
 * @author Joshua
 */
public class UserBookedFlights extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserBookedFlights.class.getName());

    
    
    private String username;
    private final String bookingsPath = "src/main/java/com/mycompany/lipadbantayoopdsa/flightBooking/bookings.txt";
    private final String cancelledBookings = "src/main/java/com/mycompany/lipadbantayoopdsa/flightBooking/cancellation_requests.txt";
    
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
        applyColorRenderer(); // Apply colors to the Status column
        loadUserBookings(); // Load data
    }
    
    
    private void applyColorRenderer() {
        // Status is at index 0 based on your String[] array
        jTable2.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String status = value.toString().toUpperCase();
                    c.setFont(c.getFont().deriveFont(java.awt.Font.BOLD));

                    switch (status) {
                        case "SCHEDULED":
                            c.setForeground(new java.awt.Color(0, 153, 51)); // Dark Green
                            break;
                        case "DELAYED":
                            c.setForeground(new java.awt.Color(255, 153, 0)); // Orange
                            break;
                        case "CANCELLED":
                        case "CANCELED":
                            c.setForeground(java.awt.Color.RED);
                            break;
                        default:
                            c.setForeground(java.awt.Color.BLACK);
                            break;
                    }
                }
                return c;
            }
        });
    }
    
    
    
    
    
    private void loadUserBookings() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        String userHeader = "(" + this.username + ")";
        File file = new File(bookingsPath);

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

                if (insideUser) {
                    // FORMAT: Airline - Aircraft - Origin - Dest - Time - FNo - Day - Status - Date
                    String[] parts = trimmed.split(" - ");

                    if (parts.length >= 9) {
                        model.addRow(new Object[]{
                            parts[7], // Status (Table Col 0)
                            parts[0], // Airline (Table Col 1)
                            parts[1], // Aircraft (Table Col 2) -> NEW
                            parts[6], // Day (Table Col 3)
                            parts[4], // Time (Table Col 4)
                            parts[2], // Origin (Table Col 5)
                            parts[3], // Destination (Table Col 6)
                            parts[5], // Flight Number (Table Col 7)
                            parts[8] // Date (Table Col 8)
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
        try (BufferedReader br = new BufferedReader(new FileReader(bookingsPath))) {
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
                    if (parts.length >= 8) {
                        model.addRow(new Object[]{
                            parts[6], parts[0], parts[5], parts[3], parts[1], parts[2], parts[4], parts[7]
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
            File file = new File(cancelledBookings);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(requestData);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error logging request.");
        }
    }
    
    private boolean isCancellationPending(String flightNumber) {
        File file = new File(cancelledBookings);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(this.username) && line.contains(flightNumber) && line.contains("PENDING")) {
                    return true;
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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Status", "Airline", "AcType", "Day", "Time", "Origin", "Destination", "Flight Number", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, false, false
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
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

        String flightNum = jTable2.getValueAt(selectedRow, 5).toString();

        if (isCancellationPending(flightNum)) {
            JOptionPane.showMessageDialog(this, "A request is already pending for this flight.");
            return;
        }

        String airline = jTable2.getValueAt(selectedRow, 0).toString();
        String day = jTable2.getValueAt(selectedRow, 1).toString();

        String request = String.format("%s | %s | %s | %s | REQUESTED | PENDING",
                this.username, airline, flightNum, day);

        saveCancellationRequest(request);
        JOptionPane.showMessageDialog(this, "Cancellation request sent to Admin.");
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
