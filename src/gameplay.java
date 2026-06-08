/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author msi
 */
public class gameplay extends javax.swing.JFrame implements ActionListener{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(gameplay.class.getName());
    KoneksiDB db = new KoneksiDB();
    ArrayList<String> order = new ArrayList<>();
    ArrayList<String> burger = new ArrayList<>();
    int totalBurgerDibuat = 0;
    Random random = new Random();

    int score = 0;

    int coin = 0;

    int time = 100;

    Timer gameTimer;
    /**
     * Creates new form tes
     */
    //image
    Image breadImg;
    Image pattyImg;
    Image cheeseImg;
    Image lettuceImg;
    Image eggImg;
    Image salmonImg;
    Image tomatoImg;
    Image onionImg;

    //tes
    public gameplay() {
    initComponents();
    setTitle("BURGER RUSH");
    setIconImage(new ImageIcon(getClass().getResource("/gameburger/image/burger icon 2.png")).getImage());
    //setSize(800, 600);
    setLocationRelativeTo(null);
    System.out.println("ID User : " + session.idUser);
    System.out.println("Username : " + session.username);
    Akun.setText(session.username);
    //image
    breadImg = new ImageIcon(
        getClass().getResource("/asset/bun.png")
    ).getImage();

    pattyImg = new ImageIcon(
            getClass().getResource("/asset/beef.png")
    ).getImage();

    cheeseImg = new ImageIcon(
            getClass().getResource("/asset/cheese.png")
    ).getImage();

    lettuceImg = new ImageIcon(
            getClass().getResource("/asset/lettuce.png")
    ).getImage();
    eggImg = new ImageIcon(
            getClass().getResource("/asset/egg.png")
    ).getImage();
    salmonImg = new ImageIcon(
            getClass().getResource("/asset/salmon.png")
    ).getImage();
    tomatoImg = new ImageIcon(
            getClass().getResource("/asset/tomato.png")
    ).getImage();
    onionImg = new ImageIcon(
            getClass().getResource("/asset/onion.png")
    ).getImage();

    setResizable(false);

    breadButton.addActionListener(this);
    lettuceButton.addActionListener(this);
    pattyButton.addActionListener(this);
    cheeseButton.addActionListener(this);
    eggButton.addActionListener(this);
    salmonButton.addActionListener(this);
    onionButton.addActionListener(this);
    tomatoButton.addActionListener(this);

    serveaButton.addActionListener(this);
  
    

    cheeseButton.setText("Cheese");

    timeBar.setMaximum(100);
    timeBar.setValue(100);

    coin = loadKoinFromDB();

    scoreField.setText("0");
    scoreField.setEditable(false);
    scoreField.setFocusable(false);

    jTextPane1.setText(String.valueOf(coin));
    jTextPane1.setEditable(false);
    jTextPane1.setFocusable(false);

    generateOrder();

    startTimer();
}

    
    
    private int loadKoinFromDB() {
    try {
        String sql = "SELECT koin FROM player_stats WHERE id_akun = ?";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1, session.idUser);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("koin");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

    private void savePlayerStats() {

    try {

        // Update statistik total pemain
        String sql =
        "UPDATE player_stats SET " +
        "jumlah_burger_dibuat = jumlah_burger_dibuat + ?, " +
        "skor = GREATEST(skor, ?), " +
        "koin = koin + ? " +
        "WHERE id_akun = ?";

        PreparedStatement ps =
        db.getConnection().prepareStatement(sql);

        ps.setInt(1, totalBurgerDibuat);
        ps.setInt(2, score);
        ps.setInt(3, coin);
        ps.setInt(4, session.idUser);

        ps.executeUpdate();

        // Simpan riwayat permainan ke tabel scores
        String insertScore =
        "INSERT INTO scores " +
        "(id_akun, highscore, current_score, total_order, order_selesai, order_gagal, total_bermain) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement psScore =
        db.getConnection().prepareStatement(insertScore);

        psScore.setInt(1, session.idUser);
        psScore.setInt(2, score);              // highscore sesi ini
        psScore.setInt(3, score);              // current score
        psScore.setInt(4, totalBurgerDibuat);  // total order
        psScore.setInt(5, totalBurgerDibuat);  // order selesai
        psScore.setInt(6, 0);                  // order gagal
        psScore.setInt(7, 1);                  // 1 kali bermain

        psScore.executeUpdate();

        System.out.println("Data score berhasil disimpan.");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
void startTimer() {

    gameTimer = new Timer(100, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            time--;

            timeBar.setValue(time);

           if (time <= 0) {

                gameTimer.stop();

                int pilihan = JOptionPane.showConfirmDialog(
                        null,
                        "Time Up!\nMain Lagi?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION
                );

                if (pilihan == JOptionPane.YES_OPTION) {

                    // reset game
                    savePlayerStats();

                    score = 0;
                    coin = loadKoinFromDB();
                    time = 100;

                    burger.clear();
                    order.clear();

                    scoreField.setText("0");
                    jTextPane1.setText(String.valueOf(coin));

                    timeBar.setValue(time);

                    generateOrder();

                    repaint();

                    gameTimer.start();

                } else {
                    savePlayerStats();
                    MainMenu mainmenu = new MainMenu();
                    mainmenu.setVisible(true);
                    dispose();
                }
            }
        }
    });

    gameTimer.start();
}

    void generateOrder() {

    order.clear();
    burger.clear();

    menuList.removeAll();

    try {

        String sql =
        "SELECT * FROM resep_burger ORDER BY RAND() LIMIT 1";

        PreparedStatement ps =
        db.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        if(rs.next()){

            String urutan =
            rs.getString("urutan_bahan");

            String[] bahan =
            urutan.split(",");

            for(String b : bahan){

                b = b.trim();

                order.add(b);

                menuList.add(b);
            }
        }

        } catch(Exception e){

            e.printStackTrace();
        }

        repaint();
}

void addIngredient(String ingredient) {

    burger.add(ingredient);

    repaint();
}

void serveBurger() {

    if (burger.equals(order)) {

        totalBurgerDibuat++;
        
        score += time;

        coin += 5;

        scoreField.setText(String.valueOf(score));

        jTextPane1.setText(String.valueOf(coin));

        if (time > 90) {
            compalinText.setText("Perfect!");
        }
        else if (time > 70) {
            compalinText.setText("Great!");
        }
        else if (time > 40) {
            compalinText.setText("Good");
        }
        else {
            compalinText.setText("Too Slow!");
        }

        time = 100;

        if (time > 100) {
            time = 100;
        }
        
        

        timeBar.setValue(time);

        generateOrder();

    } else {

        int pilihan = JOptionPane.showConfirmDialog(
                        null,
                        "Pesanan salah!\nMain Lagi?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION
                );

                if (pilihan == JOptionPane.YES_OPTION) {

                    // reset game
                    savePlayerStats();
                   
                    score = 0;
                    coin = loadKoinFromDB();
                    time = 100;
                    
                    burger.clear();
                    order.clear();

                    scoreField.setText("0");
                    jTextPane1.setText(String.valueOf(coin));

                    timeBar.setValue(time);

                    generateOrder();

                    repaint();

                    gameTimer.start();

                } else {
                    savePlayerStats();
                    MainMenu mainmenu = new MainMenu();
                    mainmenu.setVisible(true);
                    this.setVisible(false);
                }
    }
}

@Override
public void actionPerformed(ActionEvent e) {

    if (e.getSource() == breadButton) {
        addIngredient("Bread");
    }

    if (e.getSource() == pattyButton) {
        addIngredient("Patty");
    }

    if (e.getSource() == cheeseButton) {
        addIngredient("Cheese");
    }

    if (e.getSource() == lettuceButton) {
        addIngredient("Lettuce");
    }
    if (e.getSource() == tomatoButton) {
        addIngredient("Tomato");
    }
    if (e.getSource() == salmonButton) {
        addIngredient("Salmon");
    }
    if (e.getSource() == eggButton) {
        addIngredient("Egg");
    }
    if (e.getSource() == onionButton) {
        addIngredient("Onion");
    }

    if (e.getSource() == serveaButton) {
        serveBurger();
    }
}

@Override
public void paint(Graphics g) {

    super.paint(g);

    int x = 180;
    int y = 300;

   for (int i = 0; i < burger.size(); i++) {

    String ingredient = burger.get(i);

    Image currentImage = null;

    int height = 0;

    int offsetY = 0;

    switch (ingredient) {

        case "Bread":

            currentImage = breadImg;

            height = 32;

            break;

        case "Patty":

            currentImage = pattyImg;

            height = 26;

            break;

        case "Cheese":

            currentImage = cheeseImg;

            height = 12;

            offsetY = 5;

            break;

        case "Lettuce":

            currentImage = lettuceImg;

            height = 10;

            offsetY = 8;

            break;
        
        case "Tomato":

            currentImage = tomatoImg;

            height = 13;

            break;

        case "Egg":

            currentImage = eggImg;

            height = 13;

            break;

        case "Salmon":

            currentImage = salmonImg;

            height = 26;

            break;

        case "Onion":

            currentImage = onionImg;

            height = 13;

            break;
    }

    if (currentImage != null) {

        y -= height;

        g.drawImage(
                currentImage,
                x,
                y + offsetY,
                this
        );

        // ingredient berikutnya ikut turun
        y += offsetY;
    }
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        breadButton = new javax.swing.JButton();
        lettuceButton = new javax.swing.JButton();
        pattyButton = new javax.swing.JButton();
        cheeseButton = new javax.swing.JButton();
        serveaButton = new javax.swing.JButton();
        menuText = new javax.swing.JLabel();
        timeBar = new javax.swing.JProgressBar();
        scoreText = new javax.swing.JLabel();
        scoreField = new javax.swing.JTextField();
        conText = new javax.swing.JLabel();
        menuList = new java.awt.List();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        onionButton = new javax.swing.JButton();
        tomatoButton = new javax.swing.JButton();
        trashButton = new javax.swing.JButton();
        eggButton = new javax.swing.JButton();
        salmonButton = new javax.swing.JButton();
        compalinText = new javax.swing.JLabel();
        menuText1 = new javax.swing.JLabel();
        menuText2 = new javax.swing.JLabel();
        menuText3 = new javax.swing.JLabel();
        menuText4 = new javax.swing.JLabel();
        menuText5 = new javax.swing.JLabel();
        menuText6 = new javax.swing.JLabel();
        menuText7 = new javax.swing.JLabel();
        menuText8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Akun = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuOption = new javax.swing.JMenu();
        mainMenu = new javax.swing.JMenuItem();
        restartMenu = new javax.swing.JMenuItem();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(39, 18, 10));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        breadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/bread-icon.png"))); // NOI18N
        breadButton.setBorderPainted(false);
        breadButton.setContentAreaFilled(false);
        breadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                breadButtonMouseClicked(evt);
            }
        });
        getContentPane().add(breadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        lettuceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/lettuce-icon.png"))); // NOI18N
        lettuceButton.setBorderPainted(false);
        lettuceButton.setContentAreaFilled(false);
        getContentPane().add(lettuceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        pattyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/beef-icon.png"))); // NOI18N
        pattyButton.setBorderPainted(false);
        pattyButton.setContentAreaFilled(false);
        pattyButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pattyButtonMouseMoved(evt);
            }
        });
        pattyButton.addActionListener(this::pattyButtonActionPerformed);
        getContentPane().add(pattyButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, -1, -1));

        cheeseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/cheese-icon.png"))); // NOI18N
        cheeseButton.setBorderPainted(false);
        cheeseButton.setContentAreaFilled(false);
        cheeseButton.addHierarchyListener(this::cheeseButtonHierarchyChanged);
        cheeseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cheeseButtonMouseClicked(evt);
            }
        });
        cheeseButton.addActionListener(this::cheeseButtonActionPerformed);
        getContentPane().add(cheeseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 360, -1, -1));

        serveaButton.setBackground(new java.awt.Color(255, 255, 204));
        serveaButton.setText("Serve");
        getContentPane().add(serveaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 360, -1, -1));

        menuText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText.setForeground(new java.awt.Color(255, 255, 255));
        menuText.setText("Tomato");
        getContentPane().add(menuText, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 430, 50, -1));
        getContentPane().add(timeBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 10, 198, 12));

        scoreText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        scoreText.setForeground(new java.awt.Color(255, 255, 255));
        scoreText.setText("Score");
        getContentPane().add(scoreText, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 10, 71, -1));
        getContentPane().add(scoreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 32, 96, -1));

        conText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        conText.setForeground(new java.awt.Color(255, 255, 255));
        conText.setText("Coin");
        getContentPane().add(conText, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 66, 37, -1));

        menuList.setName("fddvvdvd"); // NOI18N
        menuList.addActionListener(this::menuListActionPerformed);
        getContentPane().add(menuList, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 78, 118));

        jScrollPane1.setViewportView(jTextPane1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 88, 96, -1));

        onionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/onion-icon.png"))); // NOI18N
        onionButton.setBorderPainted(false);
        onionButton.setContentAreaFilled(false);
        getContentPane().add(onionButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, -1, -1));

        tomatoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/tomato-icon.png"))); // NOI18N
        tomatoButton.setBorderPainted(false);
        tomatoButton.setContentAreaFilled(false);
        getContentPane().add(tomatoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 360, -1, -1));

        trashButton.setBackground(new java.awt.Color(255, 255, 204));
        trashButton.setText("Trash");
        trashButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trashButtonMouseClicked(evt);
            }
        });
        trashButton.addActionListener(this::trashButtonActionPerformed);
        getContentPane().add(trashButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, -1, -1));

        eggButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/egg-icon.png"))); // NOI18N
        eggButton.setBorderPainted(false);
        eggButton.setContentAreaFilled(false);
        getContentPane().add(eggButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, -1, -1));

        salmonButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/salmon-icon.png"))); // NOI18N
        salmonButton.setBorderPainted(false);
        salmonButton.setContentAreaFilled(false);
        salmonButton.addActionListener(this::salmonButtonActionPerformed);
        getContentPane().add(salmonButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, -1, -1));

        compalinText.setBackground(new java.awt.Color(251, 227, 189));
        compalinText.setFont(new java.awt.Font("Eras Bold ITC", 1, 18)); // NOI18N
        compalinText.setForeground(new java.awt.Color(255, 255, 255));
        compalinText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        compalinText.setText("tes");
        getContentPane().add(compalinText, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 30, 190, -1));

        menuText1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText1.setForeground(new java.awt.Color(255, 255, 255));
        menuText1.setText("Order");
        getContentPane().add(menuText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 72, -1));

        menuText2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText2.setForeground(new java.awt.Color(255, 255, 255));
        menuText2.setText("Bread");
        getContentPane().add(menuText2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 40, -1));

        menuText3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText3.setForeground(new java.awt.Color(255, 255, 255));
        menuText3.setText("Patty");
        getContentPane().add(menuText3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 40, -1));

        menuText4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText4.setForeground(new java.awt.Color(255, 255, 255));
        menuText4.setText("Onion");
        getContentPane().add(menuText4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 40, -1));

        menuText5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText5.setForeground(new java.awt.Color(255, 255, 255));
        menuText5.setText("Egg");
        getContentPane().add(menuText5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, 40, -1));

        menuText6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText6.setForeground(new java.awt.Color(255, 255, 255));
        menuText6.setText("Lettuce");
        getContentPane().add(menuText6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 40, -1));

        menuText7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText7.setForeground(new java.awt.Color(255, 255, 255));
        menuText7.setText("Cheese");
        getContentPane().add(menuText7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 430, 60, -1));

        menuText8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        menuText8.setForeground(new java.awt.Color(255, 255, 255));
        menuText8.setText("Tomato");
        getContentPane().add(menuText8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 430, 50, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        Akun.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Akun.setText("Akun");
        jPanel1.add(Akun);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 60));

        jPanel2.setBackground(new java.awt.Color(102, 0, 0));
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 460));

        menuOption.setText("Menu");
        menuOption.addActionListener(this::menuOptionActionPerformed);

        mainMenu.setText("Main Menu");
        mainMenu.addActionListener(this::mainMenuActionPerformed);
        menuOption.add(mainMenu);

        restartMenu.setText("Restart");
        restartMenu.addActionListener(this::restartMenuActionPerformed);
        menuOption.add(restartMenu);

        jMenuBar1.add(menuOption);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cheeseButtonHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_cheeseButtonHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cheeseButtonHierarchyChanged

    private void cheeseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cheeseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cheeseButtonActionPerformed

    private void menuListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuListActionPerformed

    private void pattyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pattyButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pattyButtonActionPerformed

    private void pattyButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pattyButtonMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_pattyButtonMouseMoved

    private void cheeseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cheeseButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cheeseButtonMouseClicked

    private void salmonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salmonButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salmonButtonActionPerformed

    private void trashButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trashButtonMouseClicked
        // TODO add your handling code here:
        burger.clear();
        repaint();
    }//GEN-LAST:event_trashButtonMouseClicked

    private void breadButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_breadButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_breadButtonMouseClicked

    private void trashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trashButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trashButtonActionPerformed

    private void mainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainMenuActionPerformed
        // TODO add your handling code here:
        MainMenu mainmenu = new MainMenu();
        mainmenu.setVisible(true);
        this.setVisible(false);
        
    }//GEN-LAST:event_mainMenuActionPerformed

    private void menuOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOptionActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_menuOptionActionPerformed

    private void restartMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartMenuActionPerformed
        // TODO add your handling code here:
        score = 0;
                    coin = loadKoinFromDB();
                    time = 100;
                    
                    burger.clear();
                    order.clear();

                    scoreField.setText("0");
                    jTextPane1.setText(String.valueOf(coin));

                    timeBar.setValue(time);

                    generateOrder();

                    repaint();

                    gameTimer.start();
    }//GEN-LAST:event_restartMenuActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new gameplay().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Akun;
    private javax.swing.JButton breadButton;
    private javax.swing.JButton cheeseButton;
    private javax.swing.JLabel compalinText;
    private javax.swing.JLabel conText;
    private javax.swing.JButton eggButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton lettuceButton;
    private javax.swing.JMenuItem mainMenu;
    private java.awt.List menuList;
    private javax.swing.JMenu menuOption;
    private javax.swing.JLabel menuText;
    private javax.swing.JLabel menuText1;
    private javax.swing.JLabel menuText2;
    private javax.swing.JLabel menuText3;
    private javax.swing.JLabel menuText4;
    private javax.swing.JLabel menuText5;
    private javax.swing.JLabel menuText6;
    private javax.swing.JLabel menuText7;
    private javax.swing.JLabel menuText8;
    private javax.swing.JButton onionButton;
    private javax.swing.JButton pattyButton;
    private javax.swing.JMenuItem restartMenu;
    private javax.swing.JButton salmonButton;
    private javax.swing.JTextField scoreField;
    private javax.swing.JLabel scoreText;
    private javax.swing.JButton serveaButton;
    private javax.swing.JProgressBar timeBar;
    private javax.swing.JButton tomatoButton;
    private javax.swing.JButton trashButton;
    // End of variables declaration//GEN-END:variables
}
