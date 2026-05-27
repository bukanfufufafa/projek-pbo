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
/**
 *
 * @author msi
 */
public class gameplay extends javax.swing.JFrame implements ActionListener{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(gameplay.class.getName());
    
    ArrayList<String> order = new ArrayList<>();
    ArrayList<String> burger = new ArrayList<>();

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
    //setSize(800, 600);
    setLocationRelativeTo(null);

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

    scoreField.setText("0");
    jTextPane1.setText("0");

    generateOrder();

    startTimer();
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
                    score = 0;
                    coin = 0;
                    time = 100;

                    burger.clear();
                    order.clear();

                    scoreField.setText("0");
                    jTextPane1.setText("0");

                    timeBar.setValue(time);

                    generateOrder();

                    repaint();

                    gameTimer.start();

                } else {

                    System.exit(0);
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

    String[] ingredients = {
        "Bread",
        "Patty",
        "Cheese",
        "Lettuce",
        "Salmon",
        "Egg",
        "Onion",
        "Tomato"
    };

    int total = random.nextInt(3) + 3;

    for (int i = 0; i < total; i++) {

        String item =
                ingredients[random.nextInt(
                        ingredients.length)];

        order.add(item);

        menuList.add(item);
    }

    repaint();
}

void addIngredient(String ingredient) {

    burger.add(ingredient);

    repaint();
}

void serveBurger() {

    if (burger.equals(order)) {

        score += 10;

        coin += 5;

        scoreField.setText(String.valueOf(score));

        jTextPane1.setText(String.valueOf(coin));

        

        time = 100;

        if (time > 100) {
            time = 100;
        }

        timeBar.setValue(time);

        generateOrder();

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Wrong Burger!\nGame Over"
        );

        System.exit(0);
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        breadButton.setText("Bread");
        breadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                breadButtonMouseClicked(evt);
            }
        });

        lettuceButton.setText("Lettuce");

        pattyButton.setText("Patty");
        pattyButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pattyButtonMouseMoved(evt);
            }
        });
        pattyButton.addActionListener(this::pattyButtonActionPerformed);

        cheeseButton.setText("Cheese");
        cheeseButton.addHierarchyListener(this::cheeseButtonHierarchyChanged);
        cheeseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cheeseButtonMouseClicked(evt);
            }
        });
        cheeseButton.addActionListener(this::cheeseButtonActionPerformed);

        serveaButton.setText("Serve");

        menuText.setText("Order");

        scoreText.setText("Score");

        conText.setText("Coin");

        menuList.setName("fddvvdvd"); // NOI18N
        menuList.addActionListener(this::menuListActionPerformed);

        jScrollPane1.setViewportView(jTextPane1);

        onionButton.setText("Onion");

        tomatoButton.setText("tomato");

        trashButton.setText("Trash");
        trashButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trashButtonMouseClicked(evt);
            }
        });
        trashButton.addActionListener(this::trashButtonActionPerformed);

        eggButton.setText("Egg");

        salmonButton.setText("Salmon");
        salmonButton.addActionListener(this::salmonButtonActionPerformed);

        jMenu1.setText("Menu");

        jMenuItem1.setText("Main Menu");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(breadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lettuceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(menuList, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuText, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cheeseButton)
                                .addGap(18, 18, 18)
                                .addComponent(tomatoButton)
                                .addGap(18, 18, 18)
                                .addComponent(salmonButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pattyButton)
                                .addGap(18, 18, 18)
                                .addComponent(onionButton)
                                .addGap(18, 18, 18)
                                .addComponent(eggButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(conText, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreField, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))
                                .addGap(17, 17, 17))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(trashButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(serveaButton, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(33, 33, 33))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(timeBar, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(scoreText, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeBar, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuList, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(breadButton)
                            .addComponent(pattyButton)
                            .addComponent(onionButton)
                            .addComponent(trashButton)
                            .addComponent(eggButton))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lettuceButton)
                            .addComponent(cheeseButton)
                            .addComponent(tomatoButton)
                            .addComponent(serveaButton)
                            .addComponent(salmonButton))
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scoreText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scoreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(conText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

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
    private javax.swing.JButton breadButton;
    private javax.swing.JButton cheeseButton;
    private javax.swing.JLabel conText;
    private javax.swing.JButton eggButton;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton lettuceButton;
    private java.awt.List menuList;
    private javax.swing.JLabel menuText;
    private javax.swing.JButton onionButton;
    private javax.swing.JButton pattyButton;
    private javax.swing.JButton salmonButton;
    private javax.swing.JTextField scoreField;
    private javax.swing.JLabel scoreText;
    private javax.swing.JButton serveaButton;
    private javax.swing.JProgressBar timeBar;
    private javax.swing.JButton tomatoButton;
    private javax.swing.JButton trashButton;
    // End of variables declaration//GEN-END:variables
}
