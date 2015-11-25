/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliveryoptimiser;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 *
 * @author Highway62
 */
public class DeliveryOptimiserMain extends javax.swing.JFrame {
    
    CardLayout mainCL;
    CardLayout screenCL;
    public static LinkedList<Item> itemNav;     // Top navigation display values
    public static int currentItem;    // Top navigation display cursor
    String mapString;
    ImageIcon map1;
    URL mapURL;
    HashMap<Integer, String> cargoMap;
    String[] cargoArray;
    int cWeight, cVolume;
    Double capWeight, capVolume;
    
    /**
     * Creates new form DeliveryOptimiserMain
     */
    public DeliveryOptimiserMain() {
        initComponents();
        this.getContentPane().setBackground(new Color(107, 106, 104)); 
        mainCL = (CardLayout) MainPanel.getLayout();
        screenCL = (CardLayout) ScreenViews.getLayout();
        
        
        // Test Customers
        Customer c1 = new Customer("Mr J Jones", "126 Ochilview Av", "Dundee", "D57 6FD", "07765463723", 1);
        Customer c2 = new Customer("Mrs B Smith", "13 Hillside St", "Dundee", "D34 6GH", "07689768543", 2);
        Customer c3 = new Customer("Mr C McDonald", "45 Braefield St", "Stirling", "ST5 9NT", "07453684692", 3);
        Customer c4 = new Customer("Mr B Dylan", "29 BigPink Rd", "Stirling", "ST34 2FV", "07845390677", 4);
        Customer c5 = new Customer("Mr C McArthur", "87 Wallace Dr", "Falkirk", "FK1 4NX", "07717345634", 5);
        Customer c6 = new Customer("Mrs C Connarty", "154 Graemes Rd", "Falkirk", "FK1 8HJ", "07723356774", 6);
        Customer c7 = new Customer("Mr A Devlin", "37 Newmarket St", "Edinburgh", "EH45 8DS", "07965744552", 7);
        Customer c8 = new Customer("Mr M Kelly", "24 Claremont Rd", "Edinburgh", "EH34 9WK", "07734423773", 8);
        
        // Test Items
        Item item1 = new Item("Fridge", 2, 2, 6, 200, "Pickup", c1, c5);
        Item item2 = new Item("Cabinet", 1, 5, 2, 160, "Pickup", c2, c6);
        Item item3 = new Item("Chair", 1, 1, 4, 50, "Pickup", c3, c7);
        Item item4 = new Item("Table", 6, 3, 3, 120, "Pickup", c4, c8);
        Item item5 = new Item("Fridge", 2, 2, 6, 200, "Delivery", c1, c5);
        Item item6 = new Item("Cabinet", 1, 5, 2, 160, "Delivery", c2, c6);
        Item item7 = new Item("Chair", 1, 1, 4, 50, "Delivery", c3, c7);
        Item item8 = new Item("Table", 6, 3, 3, 120, "Delivery", c4, c8);
        
        itemNav = new LinkedList<Item>();
        itemNav.add(item1); itemNav.add(item2); itemNav.add(item3); itemNav.add(item4);
        itemNav.add(item5); itemNav.add(item6); itemNav.add(item7); itemNav.add(item8);
        currentItem = 0;
        
        NavItem.setHorizontalAlignment(SwingConstants.CENTER);
        NavAddr.setHorizontalAlignment(SwingConstants.CENTER);
        NavPCode.setHorizontalAlignment(SwingConstants.CENTER);
        
        cargoMap = new HashMap<Integer,String>();
        
        // Initialise cargo weight and volume
        cWeight = 0; cVolume = 0;
        capWeight = 2000.0d; capVolume = 15.0d * 5.0d * 5.0d;
        cargoCapacity.setText("Capacity: " + capWeight.intValue() + "Kg, " + capVolume.intValue() + "ft³");
        
        // Initially disable Navigation left
        NavLeft.setEnabled(false);
        loadItemInfo();
        
        // Initialise Cargo Bars
        cargoWeightBar.setStringPainted(true);
        cargoVolumeBar.setStringPainted(true);
        
    }
    
    private void loadItemInfo(){
        Customer c;
        
        if(itemNav.isEmpty()){
            // Navigation panel
            NavItem.setText("All");
            NavAddr.setText("Items");
            NavPCode.setText("Delivered");
            NavRight.setEnabled(false);
            NavLeft.setEnabled(false);
            
            // Set dynamically chosen mock up map
            mapString = "/Images/Map0.png";
            mapURL = getClass().getResource(mapString);
            map1 = new ImageIcon(mapURL);
            Map.setIcon(map1);              
            
            itemConfirmBtn.setText("Confirm");
            itemCancelBtn.setText("Cancel");
            itemConfirmBtn.setEnabled(false);
            itemCancelBtn.setEnabled(false);
            
            // Item info screen
            itemName.setText("Item: ");
            itemWeight.setText("Weight: ");
            itemDimensions.setText("Dimensions: ");
            itemType.setText("Type: ");
            itemText.setText("");
            
            // Cargo Screen
            cargoList.setListData(new String[0]);
            cargoWeight.setText("Weight Used: 0%");
            cargoVolume.setText("Volume Used: 0%");
            cargoWeightBar.setValue(0);
            cargoVolumeBar.setValue(0);
        } else {
            
            if(itemNav.get(currentItem).getType().equals("Pickup")){
                c = itemNav.get(currentItem).getFrom();
                if(!itemCancelBtn.isEnabled()){
                    itemCancelBtn.setEnabled(true);
                }
            }else{
                c = itemNav.get(currentItem).getTo();
                itemCancelBtn.setEnabled(false);
            }            
            
            // Navigation panel
            NavItem.setText(itemNav.get(currentItem).getType() + " - " + itemNav.get(currentItem).getName());
            NavAddr.setText(c.getAddr() + 
                    ", " + c.getCity());
            NavPCode.setText(c.getPostCode());

            if(currentItem == itemNav.size() -1){
                NavRight.setEnabled(false);
            }

            if(currentItem == 0){
                NavLeft.setEnabled(false);
            }        

            // Set dynamically chosen mock up map
            mapString = "/Images/Map" + (currentItem + 1) + ".png";
            mapURL = getClass().getResource(mapString);
            map1 = new ImageIcon(mapURL);
            Map.setIcon(map1);        

            // Item info screen
            itemName.setText("Item: " + itemNav.get(currentItem).getName());
            itemWeight.setText("Weight: " + itemNav.get(currentItem).getWeight() + "Kg");
            itemDimensions.setText("Dimensions: " + itemNav.get(currentItem).getLength() +
                    "' x " + itemNav.get(currentItem).getWidth() +
                    "' x " + itemNav.get(currentItem).getHeight() + 
                    "'");
            itemType.setText("Type: " + itemNav.get(currentItem).getType());
            itemText.setText(c.getName() + "\n" +
                    c.getAddr() + "\n" +
                    c.getCity() + "\n" +
                    c.getPostCode() + "\n" +
                    c.getTel());
            
            // Set button text
            itemConfirmBtn.setText("Confirm " + itemNav.get(currentItem).getType());
            itemCancelBtn.setText("Cancel " + itemNav.get(currentItem).getType());
            
            // Set Cargo List
            cargoArray = cargoMap.values().toArray(new String[0]);
            cargoList.setListData(cargoArray);
            
            // Work out weight/volume percentages
            Double cWeightPerct = (100.0d/capWeight) * cWeight;
            cargoWeight.setText("Weight Used:");
            Double cVolPerct = (100.0d/capVolume) * cVolume;
            cargoVolume.setText("Volume Used:");
            
            // Set Cargo Bars
            cargoWeightBar.setValue(cWeightPerct.intValue());
            cargoVolumeBar.setValue(cVolPerct.intValue());
            
            
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

        MainPanel = new javax.swing.JPanel();
        LoginScreen = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        loginUsername = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        loginBtn = new javax.swing.JButton();
        loginRegisterBtn = new javax.swing.JButton();
        loginPassword = new javax.swing.JPasswordField();
        AppScreen = new javax.swing.JPanel();
        NavPanel = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        NavLeft = new javax.swing.JLabel();
        NavRight = new javax.swing.JLabel();
        NavItem = new javax.swing.JLabel();
        NavAddr = new javax.swing.JLabel();
        NavPCode = new javax.swing.JLabel();
        ScreenViews = new javax.swing.JPanel();
        MapScreen = new javax.swing.JPanel();
        Map = new javax.swing.JLabel();
        ItemScreen = new javax.swing.JPanel();
        itemConfirmBtn = new javax.swing.JButton();
        itemName = new javax.swing.JLabel();
        itemWeight = new javax.swing.JLabel();
        itemDimensions = new javax.swing.JLabel();
        itemType = new javax.swing.JLabel();
        itemCancelBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemText = new javax.swing.JTextArea();
        CargoScreen = new javax.swing.JPanel();
        cargoListScroll = new javax.swing.JScrollPane();
        cargoList = new javax.swing.JList<>();
        cargoCapacity = new javax.swing.JLabel();
        cargoWeight = new javax.swing.JLabel();
        cargoVolume = new javax.swing.JLabel();
        cargoWeightBar = new javax.swing.JProgressBar();
        cargoVolumeBar = new javax.swing.JProgressBar();
        BtnPanel = new javax.swing.JPanel();
        MapBtn = new javax.swing.JButton();
        ItemInfoBtn = new javax.swing.JButton();
        CargoBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));
        setResizable(false);

        MainPanel.setBackground(new java.awt.Color(153, 153, 153));
        MainPanel.setLayout(new java.awt.CardLayout());

        LoginScreen.setBackground(new java.awt.Color(102, 255, 102));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo2.png"))); // NOI18N

        jLabel2.setText("Username");

        loginUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginUsernameActionPerformed(evt);
            }
        });

        jLabel3.setText("Password");

        loginBtn.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        loginRegisterBtn.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        loginRegisterBtn.setText("Register Online");

        javax.swing.GroupLayout LoginScreenLayout = new javax.swing.GroupLayout(LoginScreen);
        LoginScreen.setLayout(LoginScreenLayout);
        LoginScreenLayout.setHorizontalGroup(
            LoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginScreenLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(LoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginRegisterBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(loginUsername)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        LoginScreenLayout.setVerticalGroup(
            LoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(loginRegisterBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MainPanel.add(LoginScreen, "LoginScreenView");

        AppScreen.setBackground(new java.awt.Color(102, 255, 102));

        NavPanel.setBackground(new java.awt.Color(102, 255, 102));

        NavLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/NavLeft.png"))); // NOI18N
        NavLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NavLeftMouseClicked(evt);
            }
        });

        NavRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/NavRight.png"))); // NOI18N
        NavRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NavRightMouseClicked(evt);
            }
        });

        NavItem.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        NavItem.setText("Pickup");
        NavItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        NavAddr.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        NavAddr.setText("23 Dundas Crescent, Dundee");

        NavPCode.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        NavPCode.setText("D45 7DF");

        javax.swing.GroupLayout NavPanelLayout = new javax.swing.GroupLayout(NavPanel);
        NavPanel.setLayout(NavPanelLayout);
        NavPanelLayout.setHorizontalGroup(
            NavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(NavPanelLayout.createSequentialGroup()
                .addComponent(NavLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(NavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(NavItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NavAddr, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(NavPCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addComponent(NavRight)
                .addGap(6, 6, 6))
        );
        NavPanelLayout.setVerticalGroup(
            NavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavPanelLayout.createSequentialGroup()
                .addGroup(NavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(NavItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NavAddr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NavPCode))
                    .addComponent(NavLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NavRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ScreenViews.setBackground(new java.awt.Color(102, 255, 102));
        ScreenViews.setLayout(new java.awt.CardLayout());

        MapScreen.setBackground(new java.awt.Color(102, 255, 102));

        Map.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Map1.png"))); // NOI18N

        javax.swing.GroupLayout MapScreenLayout = new javax.swing.GroupLayout(MapScreen);
        MapScreen.setLayout(MapScreenLayout);
        MapScreenLayout.setHorizontalGroup(
            MapScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Map, javax.swing.GroupLayout.PREFERRED_SIZE, 318, Short.MAX_VALUE)
        );
        MapScreenLayout.setVerticalGroup(
            MapScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Map, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        ScreenViews.add(MapScreen, "mapScreen");

        ItemScreen.setBackground(new java.awt.Color(204, 204, 255));

        itemConfirmBtn.setBackground(new java.awt.Color(102, 255, 102));
        itemConfirmBtn.setText("Confirm Item Pickup/Delivery");
        itemConfirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemConfirmBtnActionPerformed(evt);
            }
        });

        itemName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        itemName.setText("Item:");

        itemWeight.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        itemWeight.setText("Weight: ");

        itemDimensions.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        itemDimensions.setText("Dimension: ");

        itemType.setText("Type: ");

        itemCancelBtn.setBackground(new java.awt.Color(255, 51, 51));
        itemCancelBtn.setText("Cancel Pickup");
        itemCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCancelBtnActionPerformed(evt);
            }
        });

        itemText.setColumns(20);
        itemText.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        itemText.setRows(5);
        jScrollPane1.setViewportView(itemText);

        javax.swing.GroupLayout ItemScreenLayout = new javax.swing.GroupLayout(ItemScreen);
        ItemScreen.setLayout(ItemScreenLayout);
        ItemScreenLayout.setHorizontalGroup(
            ItemScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ItemScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(itemConfirmBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ItemScreenLayout.createSequentialGroup()
                        .addGroup(ItemScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemDimensions)
                            .addComponent(itemName)
                            .addComponent(itemWeight)
                            .addComponent(itemType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(itemCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                .addContainerGap())
        );
        ItemScreenLayout.setVerticalGroup(
            ItemScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ItemScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ItemScreenLayout.createSequentialGroup()
                        .addComponent(itemName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemWeight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemDimensions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemType)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(itemCancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        ScreenViews.add(ItemScreen, "imageScreen");

        CargoScreen.setBackground(new java.awt.Color(204, 204, 255));

        cargoList.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        cargoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        cargoListScroll.setViewportView(cargoList);

        cargoCapacity.setText("Capacity: 41,000Kg, 30' x 10' x 10' ");

        cargoWeight.setText("Weight Used:");

        cargoVolume.setText("Volume Used:");

        javax.swing.GroupLayout CargoScreenLayout = new javax.swing.GroupLayout(CargoScreen);
        CargoScreen.setLayout(CargoScreenLayout);
        CargoScreenLayout.setHorizontalGroup(
            CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CargoScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cargoListScroll)
                    .addGroup(CargoScreenLayout.createSequentialGroup()
                        .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cargoCapacity)
                            .addGroup(CargoScreenLayout.createSequentialGroup()
                                .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cargoWeight)
                                    .addComponent(cargoVolume))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cargoWeightBar, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                    .addComponent(cargoVolumeBar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
        CargoScreenLayout.setVerticalGroup(
            CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CargoScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CargoScreenLayout.createSequentialGroup()
                        .addComponent(cargoListScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cargoCapacity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cargoWeight))
                    .addComponent(cargoWeightBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CargoScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CargoScreenLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cargoVolume)
                        .addContainerGap(37, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CargoScreenLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cargoVolumeBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );

        ScreenViews.add(CargoScreen, "cargoScreen");

        BtnPanel.setBackground(new java.awt.Color(102, 255, 102));

        MapBtn.setBackground(new java.awt.Color(204, 204, 255));
        MapBtn.setFont(new java.awt.Font("Arial Unicode MS", 0, 18)); // NOI18N
        MapBtn.setText("Map");
        MapBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MapBtnActionPerformed(evt);
            }
        });

        ItemInfoBtn.setBackground(new java.awt.Color(204, 204, 255));
        ItemInfoBtn.setFont(new java.awt.Font("Arial Unicode MS", 0, 18)); // NOI18N
        ItemInfoBtn.setText("Item Info");
        ItemInfoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemInfoBtnActionPerformed(evt);
            }
        });

        CargoBtn.setBackground(new java.awt.Color(204, 204, 255));
        CargoBtn.setFont(new java.awt.Font("Arial Unicode MS", 0, 18)); // NOI18N
        CargoBtn.setText("Cargo");
        CargoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BtnPanelLayout = new javax.swing.GroupLayout(BtnPanel);
        BtnPanel.setLayout(BtnPanelLayout);
        BtnPanelLayout.setHorizontalGroup(
            BtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BtnPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(BtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BtnPanelLayout.createSequentialGroup()
                        .addComponent(MapBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ItemInfoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CargoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        BtnPanelLayout.setVerticalGroup(
            BtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BtnPanelLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MapBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ItemInfoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CargoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout AppScreenLayout = new javax.swing.GroupLayout(AppScreen);
        AppScreen.setLayout(AppScreenLayout);
        AppScreenLayout.setHorizontalGroup(
            AppScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ScreenViews, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(BtnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        );
        AppScreenLayout.setVerticalGroup(
            AppScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppScreenLayout.createSequentialGroup()
                .addComponent(NavPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ScreenViews, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        MainPanel.add(AppScreen, "MapScreenView");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 306, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 503, Short.MAX_VALUE)
        );

        setBounds(0, 0, 322, 541);
    }// </editor-fold>//GEN-END:initComponents

    private void loginUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginUsernameActionPerformed

    /**
     * Login button event handler
     * @param evt 
     */
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        
        mainCL.show(MainPanel, "MapScreenView");
    }//GEN-LAST:event_loginBtnActionPerformed

    /**
     * Confirm pickup button event handler
     * @param evt 
     */
    private void itemConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemConfirmBtnActionPerformed
       
        Customer c = itemNav.get(currentItem).getFrom();
        
        // Set cargo info
        if(!itemNav.isEmpty()){
                    
            if(itemNav.get(currentItem).getType().equals("Pickup")){
                cargoMap.put(c.getCustNo(),itemNav.get(currentItem).getName() +
                        " - " + itemNav.get(currentItem).getWeight() +
                        "Kg : " + (itemNav.get(currentItem).getLength() * 
                                itemNav.get(currentItem).getWidth() *
                                itemNav.get(currentItem).getHeight()) +
                        "ft³");
                
                // Add weight/volume
                cWeight = cWeight + itemNav.get(currentItem).getWeight();
                int cVolumeL = itemNav.get(currentItem).getLength();
                int cVolumeW = itemNav.get(currentItem).getWidth();
                int cVolumeH = itemNav.get(currentItem).getHeight();
                
                cVolume = cVolume + (cVolumeL * cVolumeW + cVolumeH);
            }
            
            if(itemNav.get(currentItem).getType().equals("Delivery")){
                cargoMap.remove(c.getCustNo());
                
                // Minus weight/volume
                cWeight = cWeight - itemNav.get(currentItem).getWeight();
                int volL = itemNav.get(currentItem).getLength();
                int volW = itemNav.get(currentItem).getWidth();
                int volH = itemNav.get(currentItem).getHeight();
                
                cVolume = cVolume - (volL * volW * volH);
            }
            
            
        }
        
        if(itemNav.isEmpty()){
            NavRight.setEnabled(false);
            NavLeft.setEnabled(false);
        } else if(itemNav.size() == 1){
            NavRight.setEnabled(false);
            itemNav.removeFirst();
        } else {
            itemNav.removeFirst();
            if(currentItem > 0){
                currentItem--;
            }
        }
        
        loadItemInfo();
        
    }//GEN-LAST:event_itemConfirmBtnActionPerformed

    private void NavRightMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NavRightMouseClicked
        
        
        if(NavRight.isEnabled()){
            
            if(!NavLeft.isEnabled()){
                NavLeft.setEnabled(true);
            }

            if(currentItem == (itemNav.size() - 1)){
                NavRight.setEnabled(false);
                return;
            }
            
            currentItem++;
            loadItemInfo();
        }
        
        if(currentItem > 0){
            itemConfirmBtn.setEnabled(false);
        }
        
    }//GEN-LAST:event_NavRightMouseClicked

    private void NavLeftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NavLeftMouseClicked
        
        if(NavLeft.isEnabled()){
            if(!NavRight.isEnabled()){
                NavRight.setEnabled(true);
            }

            if(currentItem == 0){
                NavLeft.setEnabled(false);
                return;
            }
            
            currentItem--;
            loadItemInfo();
        }
        
        if(currentItem == 0){
            itemConfirmBtn.setEnabled(true);
        }
    }//GEN-LAST:event_NavLeftMouseClicked

    private void MapBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MapBtnActionPerformed
        screenCL.show(ScreenViews, "mapScreen");
    }//GEN-LAST:event_MapBtnActionPerformed

    private void ItemInfoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemInfoBtnActionPerformed
        screenCL.show(ScreenViews, "imageScreen");
    }//GEN-LAST:event_ItemInfoBtnActionPerformed

    private void CargoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargoBtnActionPerformed
        screenCL.show(ScreenViews, "cargoScreen");
    }//GEN-LAST:event_CargoBtnActionPerformed

    private void itemCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCancelBtnActionPerformed
        
        if(itemNav.isEmpty()){
            NavRight.setEnabled(false);
            NavLeft.setEnabled(false);
        } else if(itemNav.size() == 1){
            NavRight.setEnabled(false);
            itemNav.remove(currentItem);
        } else {
            
            Item it = itemNav.remove(currentItem);
            if(currentItem > 0){
                currentItem--;
            }
  
            // If pickup cancelled, cancel related delivery
            if(it.getType().equals("Pickup")){
                int j = 0;
                int rmIndex = 0;
                boolean rmSet = false;
                for(Item i : itemNav){
                    if(i.getType().equals("Delivery")){
                        if(i.getTo() == it.getTo()){
                            rmIndex = j;
                            rmSet = true;
                        }
                    }
                    j++;
                }
                if(rmSet = true){
                    itemNav.remove(rmIndex);
                }
            }
        }
        
        loadItemInfo();        
    }//GEN-LAST:event_itemCancelBtnActionPerformed

    
    
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DeliveryOptimiserMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DeliveryOptimiserMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DeliveryOptimiserMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DeliveryOptimiserMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               new DeliveryOptimiserMain().setVisible(true);
              
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AppScreen;
    private javax.swing.JPanel BtnPanel;
    private javax.swing.JButton CargoBtn;
    private javax.swing.JPanel CargoScreen;
    private javax.swing.JButton ItemInfoBtn;
    private javax.swing.JPanel ItemScreen;
    private javax.swing.JPanel LoginScreen;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JLabel Map;
    private javax.swing.JButton MapBtn;
    private javax.swing.JPanel MapScreen;
    private javax.swing.JLabel NavAddr;
    private javax.swing.JLabel NavItem;
    private javax.swing.JLabel NavLeft;
    private javax.swing.JLabel NavPCode;
    private javax.swing.JPanel NavPanel;
    private javax.swing.JLabel NavRight;
    private javax.swing.JPanel ScreenViews;
    private javax.swing.JLabel cargoCapacity;
    private javax.swing.JList<String> cargoList;
    private javax.swing.JScrollPane cargoListScroll;
    private javax.swing.JLabel cargoVolume;
    private javax.swing.JProgressBar cargoVolumeBar;
    private javax.swing.JLabel cargoWeight;
    private javax.swing.JProgressBar cargoWeightBar;
    private javax.swing.JButton itemCancelBtn;
    private javax.swing.JButton itemConfirmBtn;
    private javax.swing.JLabel itemDimensions;
    private javax.swing.JLabel itemName;
    private javax.swing.JTextArea itemText;
    private javax.swing.JLabel itemType;
    private javax.swing.JLabel itemWeight;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField loginPassword;
    private javax.swing.JButton loginRegisterBtn;
    private javax.swing.JTextField loginUsername;
    // End of variables declaration//GEN-END:variables
}
