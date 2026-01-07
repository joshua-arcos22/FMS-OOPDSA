/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.lipadbantayoopdsa.popupInterface;

import com.mycompany.lipadbantayoopdsa.AdminOperations;
import com.mycompany.lipadbantayoopdsa.distanceCalculator;
import com.mycompany.lipadbantayoopdsa.userAuthentication.AirlineManagerDashboard;
import java.awt.Color;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Joshua
 */
public class fareEditor_M extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(fareEditor_M.class.getName());

    
    private String currentAirlineName = "";
    private String currentAirlinePrefix = ""; 
    private String airlineRoutesPath = "";
    private double currentRatePerKm;
    /**
     * Creates new form fareEditor_M
     */
    public fareEditor_M() {
        initComponents();
    }
    
    
    
    public fareEditor_M(String currentAirlineName, String currentAirlinePrefix) {
        initComponents();
        this.currentAirlineName = currentAirlineName;
        this.currentAirlinePrefix = currentAirlinePrefix;

        Title.setText("FARE EDITOR (" + currentAirlineName.toUpperCase() + ")");

        
        loadAirlineDetails(); 
        loadPath();           
       
        System.out.println(airlineRoutesPath);

        loadArchiveData();    
        intializeComboBoxes();
    }

    public void intializeComboBoxes() {

       
        //Airline
        airlineName.setText(currentAirlineName);

        //Aircraft
        Combo_Aircraft.removeAllItems();
        Combo_Aircraft.addItem("N/A");
        try {
            File aircraftMaster = new File(AdminOperations.Database_Aircarfts_Path);
            Scanner aircraftReader = new Scanner(aircraftMaster);
            // adds options for the combobox, reads from the ac database
            while (aircraftReader.hasNextLine()) {
                String aircraftStringLine = aircraftReader.nextLine();
                String aircraftStringLineArray[] = aircraftStringLine.split("-");
                Combo_Aircraft.addItem(aircraftStringLineArray[0]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found ");
        }

        //Origin
        Combo_Origin.removeAllItems();
        Combo_Origin.addItem("N/A");
        try {
            File originMaster = new File(AdminOperations.Database_Aiports_Path);
            Scanner originReader = new Scanner(originMaster);
            // adds options for the combobox, reads from the ac database
            while (originReader.hasNextLine()) {
                String originStringLine = originReader.nextLine();
                String originStringLineArray[] = originStringLine.split("-");
                Combo_Origin.addItem(originStringLineArray[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found ");
        }

        //Destination 
        Combo_Destination.removeAllItems();
        Combo_Destination.addItem("N/A");
        try {
            File destinationMaster = new File(AdminOperations.Database_Aiports_Path);
            Scanner destiantionReader = new Scanner(destinationMaster);
            // adds options for the combobox, reads from the ac database
            while (destiantionReader.hasNextLine()) {
                String destinationStringLine = destiantionReader.nextLine();
                String destinationStringLineArray[] = destinationStringLine.split("-");
                Combo_Destination.addItem(destinationStringLineArray[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found ");
        }

    }
    
    
    
    public void loadPath() {
        String parentFilePath = "src/main/java/com/mycompany/lipadbantayoopdsa/Database/Routes/";
        this.airlineRoutesPath = parentFilePath + "Routes_" + currentAirlineName.toUpperCase() + ".txt";
    }
    
    public void loadAirlineDetails() {
        
        File file = new File(AdminOperations.Database_Airlines_Path); 
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
            while ((line = br.readLine()) != null) {
                // Format: AIRASIA-Z2-2.50
                String[] data = line.split("-");
                
            
                if (data.length >= 3 && data[0].equalsIgnoreCase(currentAirlineName)) {
                    try {
                        this.currentRatePerKm = Double.parseDouble(data[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing rate: " + data[2]);
                        this.currentRatePerKm = 0.0;
                    }
                    break; 
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading airline details");
        }
    }
    
    
    public void loadArchiveData() {
        DefaultTableModel model = (DefaultTableModel) FlightTable.getModel();
        model.setRowCount(0);

   
        File file = new File(airlineRoutesPath);

        if (!file.exists()) {
            return; 
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Format: A320-RPLC(Clark)-RPVE(Caticlan)-1500
                    String[] rowData = line.split("-");

                   
                    if (rowData.length >= 4) {
                        processAndAddSearchRow(model, rowData);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading routes");
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
        Container = new javax.swing.JPanel();
        TopContainer = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        SearchContainer = new javax.swing.JPanel();
        SearchField = new javax.swing.JTextField();
        SearchButton = new javax.swing.JButton();
        BottomContainer = new javax.swing.JPanel();
        editFlight = new javax.swing.JToggleButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        farekmeditor = new javax.swing.JToggleButton();
        Combo_Aircraft = new javax.swing.JComboBox<>();
        Combo_Origin = new javax.swing.JComboBox<>();
        Combo_Destination = new javax.swing.JComboBox<>();
        applyFilters = new javax.swing.JButton();
        resetFilters = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        FlightTable = new javax.swing.JTable();
        airlineName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Container.setBackground(new java.awt.Color(255, 255, 255));

        TopContainer.setBackground(new java.awt.Color(51, 153, 255));

        Title.setFont(new java.awt.Font("Santana-Black", 0, 36)); // NOI18N
        Title.setForeground(new java.awt.Color(255, 255, 255));
        Title.setText("FARE EDITOR");

        SearchContainer.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout SearchContainerLayout = new javax.swing.GroupLayout(SearchContainer);
        SearchContainer.setLayout(SearchContainerLayout);
        SearchContainerLayout.setHorizontalGroup(
            SearchContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
        );
        SearchContainerLayout.setVerticalGroup(
            SearchContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchContainerLayout.createSequentialGroup()
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
                .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SearchContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        TopContainerLayout.setVerticalGroup(
            TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(TopContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SearchContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Title))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        BottomContainer.setBackground(new java.awt.Color(255, 255, 255));

        editFlight.setBackground(new java.awt.Color(255, 153, 102));
        editFlight.setText("EDIT");
        editFlight.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFlightActionPerformed(evt);
            }
        });

        jToggleButton1.setText("BACK");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        farekmeditor.setBackground(new java.awt.Color(255, 153, 102));
        farekmeditor.setText("EDIT FARE/KM");
        farekmeditor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        farekmeditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                farekmeditorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BottomContainerLayout = new javax.swing.GroupLayout(BottomContainer);
        BottomContainer.setLayout(BottomContainerLayout);
        BottomContainerLayout.setHorizontalGroup(
            BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomContainerLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(farekmeditor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
        BottomContainerLayout.setVerticalGroup(
            BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BottomContainerLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(BottomContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(farekmeditor, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
        );

        Combo_Aircraft.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        Combo_Origin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        Combo_Destination.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        applyFilters.setText("Apply");
        applyFilters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyFiltersActionPerformed(evt);
            }
        });

        resetFilters.setText("Reset Filters");
        resetFilters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFiltersActionPerformed(evt);
            }
        });

        FlightTable.setBackground(new java.awt.Color(255, 255, 255));
        FlightTable.setForeground(new java.awt.Color(0, 0, 0));
        FlightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Airline", "Aircraft", "Origin", "Destination", "Distance", "Base Fare", "Rate/Km", "Total Fare"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(FlightTable);

        airlineName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout ContainerLayout = new javax.swing.GroupLayout(Container);
        Container.setLayout(ContainerLayout);
        ContainerLayout.setHorizontalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TopContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(ContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContainerLayout.createSequentialGroup()
                        .addComponent(BottomContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(ContainerLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(airlineName, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(Combo_Aircraft, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Combo_Origin, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(Combo_Destination, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applyFilters, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resetFilters, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ContainerLayout.setVerticalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerLayout.createSequentialGroup()
                .addComponent(TopContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(airlineName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Combo_Origin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Combo_Aircraft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Combo_Destination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetFilters)
                        .addComponent(applyFilters)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BottomContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    
    public void searchFlights() {
        String searchTerm = SearchField.getText().trim().toLowerCase();
        String placeholderText = "search for a flight".toLowerCase();

       
        if (searchTerm.equals(placeholderText) || searchTerm.isEmpty()) {
            loadArchiveData();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) FlightTable.getModel();
        model.setRowCount(0);

 
        File file = new File(airlineRoutesPath);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] rowData = line.split("-");

                boolean match = false;
                if (line.toLowerCase().contains(searchTerm)) {
                    match = true;
                }

                if (match && rowData.length >= 4) {
                    processAndAddSearchRow(model, rowData);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
    
    //----------------------------------------------------------------------
    public void applyFilters() {
        File file = new File(airlineRoutesPath);

     
        String selAircraft = Combo_Aircraft.getSelectedItem().toString();
        String selOrigin = Combo_Origin.getSelectedItem().toString();
        String selDest = Combo_Destination.getSelectedItem().toString();

        DefaultTableModel model = (DefaultTableModel) FlightTable.getModel();
        model.setRowCount(0);

        if (!file.exists()) {
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] rowData = line.split("-");
                if (rowData.length < 4) {
                    continue;
                }

                
                boolean matchesAircraft = selAircraft.equals("N/A")
                        || selAircraft.isEmpty()
                        || rowData[0].equalsIgnoreCase(selAircraft);

                boolean matchesOrigin = selOrigin.equals("N/A")
                        || selOrigin.isEmpty()
                        || rowData[1].contains(selOrigin);

                boolean matchesDest = selDest.equals("N/A")
                        || selDest.isEmpty()
                        || rowData[2].contains(selDest);

                if (matchesAircraft && matchesOrigin && matchesDest) {
                    processAndAddSearchRow(model, rowData);
                }
            }
        } catch (IOException e) {
            System.out.println("Error in Filtering");
        }
    }
    
    
    
    private void updateRouteFile(String targetAircraft, String targetOrigin, String targetDest, String newPrice) {
        File inputFile = new File(airlineRoutesPath);
        File tempFile = new File(inputFile.getParent(), "temp_" + inputFile.getName());

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile)); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("-");

         
                if (parts.length >= 4
                        && parts[0].equals(targetAircraft)
                        && parts[1].equals(targetOrigin)
                        && parts[2].equals(targetDest)) {

                    // Format sline: Aircraft-Org-Dest-NEWPRICE
                    writer.write(parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + newPrice);
                } else {
                    
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error updating route file");
            return;
        }

      
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving changes to database.");
        }
    }
    
    
    
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

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        AirlineManagerDashboard display = new AirlineManagerDashboard(currentAirlineName, currentAirlinePrefix);
        display.setVisible(true);
        dispose();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void applyFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyFiltersActionPerformed
        applyFilters();
    }//GEN-LAST:event_applyFiltersActionPerformed

    private void resetFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFiltersActionPerformed
        Combo_Aircraft.setSelectedIndex(0);
        Combo_Destination.setSelectedIndex(0);
        Combo_Origin.setSelectedIndex(0);
        applyFilters();
    }//GEN-LAST:event_resetFiltersActionPerformed

    private void editFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFlightActionPerformed
        int selectedRow = FlightTable.getSelectedRow();

    
        if (selectedRow == -1 || FlightTable.getValueAt(selectedRow, 0).toString().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a valid route to edit.");
            return;
        }

        // READ CURRENT VALUES
        String aircraft = FlightTable.getValueAt(selectedRow, 1).toString();
        String origin = FlightTable.getValueAt(selectedRow, 2).toString();
        String destination = FlightTable.getValueAt(selectedRow, 3).toString();
        String currentBaseFare = FlightTable.getValueAt(selectedRow, 5).toString();

        // Input dialog for new Base Fare
        String newBaseFare = javax.swing.JOptionPane.showInputDialog(this,
                "Edit Base Fare for " + origin + " to " + destination,
                currentBaseFare);

        if (newBaseFare != null && !newBaseFare.isEmpty()) {
            try {
                Double.parseDouble(newBaseFare); // Validate number

                // UPDATE FILE
                updateRouteFile(aircraft, origin, destination, newBaseFare);

                // ================= MASTER LOG (EDIT) =================
                java.time.format.DateTimeFormatter dtf =
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
                String timestamp = java.time.LocalDateTime.now().format(dtf);

                StringBuilder logEntry = new StringBuilder();

                logEntry.append("[").append(timestamp).append("]\n");
                logEntry.append("EVENT  : ROUTE_EDIT\n");
                logEntry.append("ACTION : Base fare updated\n");
                logEntry.append("ACTOR  : SYSTEM\n\n");

                logEntry.append("DETAILS\n");
                logEntry.append("--------\n");
                logEntry.append("Aircraft Type       : ").append(aircraft).append("\n");
                logEntry.append("Origin Airport      : ").append(origin).append("\n");
                logEntry.append("Destination Airport : ").append(destination).append("\n\n");

                logEntry.append("CHANGES\n");
                logEntry.append("--------\n");
                logEntry.append("Base Fare (Before)  : ").append(currentBaseFare).append("\n");
                logEntry.append("Base Fare (After)   : ").append(newBaseFare).append("\n\n");

                logEntry.append("----------------------------------------\n\n");

                com.mycompany.lipadbantayoopdsa.Logs.logs.writeLog(logEntry.toString());
                // =====================================================

                loadArchiveData(); // Refresh table

            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid Fare Amount");
            }
        }
    }//GEN-LAST:event_editFlightActionPerformed

    
    
    private void farekmeditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_farekmeditorActionPerformed
  
        String newRateStr = javax.swing.JOptionPane.showInputDialog(this,
                "Enter new Rate per Km for " + currentAirlineName + ":",
                String.valueOf(currentRatePerKm));

       
        if (newRateStr == null || newRateStr.trim().isEmpty()) {
            return;
        }

        try {

            double newRate = Double.parseDouble(newRateStr);

            if (newRate < 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Rate cannot be negative.");
                return;
            }

        
            double oldRate = currentRatePerKm;

            
            boolean success = updateAirlineRateInDatabase(newRate);

            if (success) {
             
                this.currentRatePerKm = newRate;

                // ================= MASTER LOG (RATE EDIT) =================
                java.time.format.DateTimeFormatter dtf =
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
                String timestamp = java.time.LocalDateTime.now().format(dtf);

                StringBuilder logEntry = new StringBuilder();

                logEntry.append("[").append(timestamp).append("]\n");
                logEntry.append("EVENT  : AIRLINE_RATE_EDIT\n");
                logEntry.append("ACTION : Rate per Km updated\n");
                logEntry.append("ACTOR  : SYSTEM\n\n");

                logEntry.append("DETAILS\n");
                logEntry.append("--------\n");
                logEntry.append("Airline Name        : ").append(currentAirlineName).append("\n\n");

                logEntry.append("CHANGES\n");
                logEntry.append("--------\n");
                logEntry.append("Rate per Km (Before): PHP ").append(oldRate).append("\n");
                logEntry.append("Rate per Km (After) : PHP ").append(newRate).append("\n\n");

                logEntry.append("----------------------------------------\n\n");

                com.mycompany.lipadbantayoopdsa.Logs.logs.writeLog(logEntry.toString());
                // ==========================================================

                
                loadArchiveData();

                javax.swing.JOptionPane.showMessageDialog(this,
                        "Rate updated successfully to PHP " + newRate);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error updating database file.");
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Invalid number format. Please enter a valid decimal (e.g., 2.50).");
        }
    }//GEN-LAST:event_farekmeditorActionPerformed

    
    private boolean updateAirlineRateInDatabase(double newRate) {
        File inputFile = new File(AdminOperations.Database_Airlines_Path);
        
        File tempFile = new File(inputFile.getParent(), "temp_" + inputFile.getName());

        try( BufferedReader reader = new BufferedReader(new FileReader(inputFile)); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
           

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("-");

                // File Format: AIRLINE-PREFIX-RATE

                if (parts.length >= 2 && parts[0].equalsIgnoreCase(currentAirlineName)) {

                 
                    writer.write(parts[0] + "-" + parts[1] + "-" + String.format("%.2f", newRate));

                } else {
                   
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error updating airline rate");
            return false;
        }

        
        if (inputFile.delete()) {
            return tempFile.renameTo(inputFile);
        } else {
            return false;
        }
    }
    
    
    
    private void processAndAddSearchRow(DefaultTableModel model, String[] rowData) {
        try {
            String aircraft = rowData[0];
            String origin = rowData[1];
            String destination = rowData[2];
            String baseFareStr = rowData[3];

            double baseFare = Double.parseDouble(baseFareStr);

         
            distanceCalculator calc = new distanceCalculator(origin, destination, aircraft);
            double distance = calc.calculateDistanceKm();

          
            double totalFare = (distance * currentRatePerKm) + baseFare;

       
            String distDisplay = String.format("%.0f km", distance);
            String baseDisplay = String.format("%.2f", baseFare);
            String rateDisplay = String.format("%.2f", currentRatePerKm);
            String totalDisplay = String.format("PHP %.2f", totalFare);

        
            model.addRow(new Object[]{
                currentAirlineName,
                aircraft,
                origin,
                destination,
                distDisplay,
                baseDisplay,
                rateDisplay,
                totalDisplay
            });

        } catch (Exception e) {
            System.out.println("Error in Calculating");
        }
    }
    
   
    
    
    
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
        java.awt.EventQueue.invokeLater(() -> new fareEditor_M().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomContainer;
    private javax.swing.JComboBox<String> Combo_Aircraft;
    private javax.swing.JComboBox<String> Combo_Destination;
    private javax.swing.JComboBox<String> Combo_Origin;
    private javax.swing.JPanel Container;
    private javax.swing.JTable FlightTable;
    private javax.swing.JButton SearchButton;
    private javax.swing.JPanel SearchContainer;
    private javax.swing.JTextField SearchField;
    private javax.swing.JLabel Title;
    private javax.swing.JPanel TopContainer;
    private javax.swing.JLabel airlineName;
    private javax.swing.JButton applyFilters;
    private javax.swing.JToggleButton editFlight;
    private javax.swing.JToggleButton farekmeditor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton resetFilters;
    // End of variables declaration//GEN-END:variables
}
