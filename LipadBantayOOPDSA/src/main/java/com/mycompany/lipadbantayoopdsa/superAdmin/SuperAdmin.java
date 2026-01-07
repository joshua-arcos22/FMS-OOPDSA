package com.mycompany.lipadbantayoopdsa.superAdmin;

import com.mycompany.lipadbantayoopdsa.userAuthentication.LoginForm;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author jedrich
 */
public class SuperAdmin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SuperAdmin.class.getName());

    /**
     * Creates new form SuperAdmin
     */
    public SuperAdmin() {
        initComponents();
        loadUserData();
    }
        
    private void loadUserData() {
    DefaultTableModel model = (DefaultTableModel) UserTable.getModel();
    model.setRowCount(0); 

   
    String filePath = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_credentials.txt"; 
    String separator = "----------------------------";

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        String fullName = "", username = "", role = "", status = "", action = "";

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals(separator)) {
                if (!username.isEmpty()) {
                    
                    if (status.equalsIgnoreCase("PENDING")) action = "[Validate]";
                    else if (status.equalsIgnoreCase("ACTIVE")) action = "[Disable]";
                    else if (status.equalsIgnoreCase("DISABLED")) action = "[Enable]";

                    model.addRow(new Object[]{fullName, username, role, status, action});
                }
               
                fullName = ""; username = ""; role = ""; status = ""; 
            } else if (line.contains(": ")) {
                String[] parts = line.split(": ", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.equals("userFULLNAME") || key.equals("FULLNAME") || key.equals("MANAGER")) fullName = value;
                else if (key.equals("USERNAME")) username = value;
                else if (key.equals("ROLE")) role = value;
                else if (key.equals("STATUS")) status = value;
            }
        }
    } catch (IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
    private void updateAccountStatus(String targetUsername, String newStatus) {
    String filePath = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_credentials.txt";
    String tempPath = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_credentials_temp.txt";
    
    java.io.File inputFile = new java.io.File(filePath);
    java.io.File tempFile = new java.io.File(tempPath);

    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(inputFile));
         java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(tempFile))) {

        String line;
        boolean isTargetBlock = false;
        boolean statusUpdatedInBlock = false;
        
        
        StringBuilder currentBlock = new StringBuilder();

        while ((line = br.readLine()) != null) {
            currentBlock.append(line).append("\n");

           
            if (line.trim().equals("USERNAME: " + targetUsername)) {
                isTargetBlock = true;
            }

            
            if (line.trim().equals("----------------------------")) {
                String blockText = currentBlock.toString();
                
                if (isTargetBlock) {
                    
                    if (blockText.contains("STATUS:")) {
                        
                        blockText = blockText.replaceAll("STATUS: .*", "STATUS: " + newStatus);
                    } else {
                       
                        blockText = blockText.replace("----------------------------", 
                                                      "STATUS: " + newStatus + "\n----------------------------");
                    }
                }
                
                bw.write(blockText);
             
                currentBlock.setLength(0);
                isTargetBlock = false;
            }
        }
    } catch (java.io.IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error processing file: " + e.getMessage());
        return;
    }

   
    if (inputFile.delete()) {
        if (!tempFile.renameTo(inputFile)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Could not rename temporary file.");
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Could not delete original file. Check if it is open elsewhere.");
    }
    }
    private void performSearch() {
    DefaultTableModel model = (DefaultTableModel) UserTable.getModel();
    model.setRowCount(0); 

    String searchText = searchField.getText().toLowerCase().trim();
    String filterType = searchFilter.getSelectedItem().toString();
    
    String filePath = "src/main/java/com/mycompany/lipadbantayoopdsa/userAuthentication/user_credentials.txt";
    String separator = "----------------------------";

    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
        String line;
        String fullName = "", username = "", role = "", status = "", action = "";

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals(separator)) {
                if (!username.isEmpty()) {
                    
                    boolean matches = false;
                    if (filterType.equals("Full Name") && fullName.toLowerCase().contains(searchText)) matches = true;
                    else if (filterType.equals("Username") && username.toLowerCase().contains(searchText)) matches = true;
                    else if (filterType.equals("Role") && role.toLowerCase().contains(searchText)) matches = true;
                    
                    
                    if (searchText.isEmpty() || matches) {
                        if (status.equalsIgnoreCase("PENDING")) action = "[Validate]";
                        else if (status.equalsIgnoreCase("ACTIVE")) action = "[Disable]";
                        else if (status.equalsIgnoreCase("DISABLED")) action = "[Enable]";

                        model.addRow(new Object[]{fullName, username, role, status, action});
                    }
                }
                fullName = ""; username = ""; role = ""; status = ""; 
            } else if (line.contains(": ")) {
                String[] parts = line.split(": ", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.equals("userFULLNAME") || key.equals("FULLNAME") || key.equals("MANAGER")) fullName = value;
                else if (key.equals("USERNAME")) username = value;
                else if (key.equals("ROLE")) role = value;
                else if (key.equals("STATUS")) status = value;
            }
        }
    } catch (java.io.IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        UserTable = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        searchFilter = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Super Admin Dashboard");

        jButton2.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jButton2.setText("Accounts Requests");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jButton3.setText("Add Admin Account");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jButton4.setLabel("Log Out");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButton2)
                .addGap(27, 27, 27)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(21, 21, 21))
        );

        UserTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Full Name", "Username", "Role", "Status", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        UserTable.setCellSelectionEnabled(true);
        UserTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UserTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(UserTable);
        UserTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (UserTable.getColumnModel().getColumnCount() > 0) {
            UserTable.getColumnModel().getColumn(2).setResizable(false);
            UserTable.getColumnModel().getColumn(3).setResizable(false);
        }

        searchField.addActionListener(this::searchFieldActionPerformed);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        searchFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full Name", "Username", "Role" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:// 1. Close the current SuperAdmin dashboard to free up resources
    this.dispose(); 
    
    
    new AccountApproval().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
    String checkKey = javax.swing.JOptionPane.showInputDialog(this, "Enter Master Key to authorize registration:");
    
   
    if ("ken".equals(checkKey)) {
       
        this.dispose(); 
        new AddSuperadmin().setVisible(true); 
    } else if (checkKey != null) {
        
        javax.swing.JOptionPane.showMessageDialog(this, "Unauthorized Access!", "Security Alert", javax.swing.JOptionPane.ERROR_MESSAGE);
    }        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void UserTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserTableMouseClicked
    int row = UserTable.getSelectedRow();
    int col = UserTable.getSelectedColumn();

    if (col == 4 && row != -1) {
        String action = UserTable.getValueAt(row, 4).toString();
        String username = UserTable.getValueAt(row, 1).toString();

        String key = javax.swing.JOptionPane.showInputDialog(this, "Enter Master Key:");
        
        
        // CHNAGE THE MASTER KEYYYY
        if ("ken".equals(key)) {
            String newStatus = "";
            if (action.equals("[Validate]") || action.equals("[Enable]")) newStatus = "ACTIVE";
            else if (action.equals("[Disable]")) newStatus = "DISABLED";

            updateAccountStatus(username, newStatus);
            javax.swing.JOptionPane.showMessageDialog(this, "Account " + username + " is now " + newStatus);
            loadUserData(); // Refresh table
        } else if (key != null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid Master Key!", "Security Alert", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_UserTableMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose(); 
        new LoginForm().setVisible(true); 
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        performSearch();        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldKeyReleased

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new SuperAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable UserTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> searchFilter;
    // End of variables declaration//GEN-END:variables
}
