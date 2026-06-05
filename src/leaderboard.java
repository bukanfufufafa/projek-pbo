/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

/**
 *
 * @author Salsabila Ramadhania
 */
public class leaderboard extends javax.swing.JFrame {
    
    KoneksiDB db = new KoneksiDB();

    private String usernameLogin = session.username;
    private int currentScore = 0;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(leaderboard.class.getName());

    /**
     * Creates new form leaderboard
     */
    public leaderboard() {
    initComponents();
    
    setTitle("LEADERBOARD");
    setIconImage(new ImageIcon(getClass().getResource("/gameburger/image/burger icon 2.png")).getImage());

    setLocationRelativeTo(null);

    db = new KoneksiDB();

    System.out.println("Koneksi = " + db.con);

    aturHeaderTable();

    tampilkanTop10Leaderboard();
    tampilkanPosisiUser();
}
    
    public leaderboard(String usernameLogin) {
        initComponents();
        
        setLocationRelativeTo(null);

        db = new KoneksiDB();

        this.usernameLogin = usernameLogin;

        aturHeaderTable();

        tampilkanTop10Leaderboard();
        tampilkanPosisiUser();
    }
    
    private void aturHeaderTable() {
        
        // Header table
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBackground(new java.awt.Color(107, 45, 15));
        headerRenderer.setForeground(java.awt.Color.WHITE);
        headerRenderer.setFont(new java.awt.Font("Pixelify Sans", java.awt.Font.BOLD, 18));
        headerRenderer.setOpaque(true);

        tblLeaderboard.getTableHeader().setDefaultRenderer(headerRenderer);
        tblLeaderboard.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 36));
    }
     
    public void simpanHasilGame(String username, int scoreBaru, int totalOrderBaru, int orderSelesaiBaru, int orderGagalBaru) {
       String cek_data = "SELECT highscore FROM scores WHERE id_akun = " + session.idUser;
        try {
            Statement st = db.con.createStatement();
            ResultSet rs = st.executeQuery(cek_data);

            if (rs.next()) {
                // UPDATE DATA
                int highscoreLama = rs.getInt("highscore");
                int highscoreFinal = highscoreLama;

                if (scoreBaru > highscoreLama) {
                    highscoreFinal = scoreBaru;
                }

                String update_score = "UPDATE scores SET "
                    + "highscore = " + highscoreFinal + ", "
                    + "current_score = " + scoreBaru + ", "
                    + "total_order = total_order + " + totalOrderBaru + ", "
                    + "order_selesai = order_selesai + " + orderSelesaiBaru + ", "
                    + "order_gagal = order_gagal + " + orderGagalBaru + ", "
                    + "total_bermain = total_bermain + 1 "
                    + "WHERE id_akun = " + session.idUser;

                st.executeUpdate(update_score);

            } else {
                // CREATE DATA
                String tambah_score = "INSERT INTO scores "
                    + "(id_akun, highscore, current_score, total_order, "
                    + "order_selesai, order_gagal, total_bermain) "
                    + "VALUES ("
                    + session.idUser + ", "
                    + scoreBaru + ", "
                    + scoreBaru + ", "
                    + totalOrderBaru + ", "
                    + orderSelesaiBaru + ", "
                    + orderGagalBaru + ", "
                    + "1)";
                st.executeUpdate(tambah_score);
            }

            usernameLogin = username;

            tampilkanTop10Leaderboard();
            tampilkanPosisiUser();

        } catch (Exception e) {
            System.out.println("Gagal simpan hasil game: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal menyimpan hasil game!");
        }
    }
    
    // menampilkan top 10 leaderboard
   private void tampilkanTop10Leaderboard() {

    System.out.println("=== LOAD LEADERBOARD ===");

    if (db == null) {
        System.out.println("db NULL");
        return;
    }

    if (db.con == null) {
        System.out.println("db.con NULL");
        return;
    }

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Rank");
    model.addColumn("Username");
    model.addColumn("Highscore");

    tblLeaderboard.setModel(model);

    try {

        String sql =
            "SELECT akun.username, scores.highscore " +
            "FROM scores " +
            "INNER JOIN akun ON akun.id = scores.id_akun " +
            "ORDER BY scores.highscore DESC";

        Statement st = db.con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        int rank = 1;

        while (rs.next()) {

            String username = rs.getString("username");
            int highscore = rs.getInt("highscore");

            System.out.println(
                rank + " | " +
                username + " | " +
                highscore
            );

            model.addRow(new Object[]{
                rank,
                username,
                highscore
            });

            rank++;
        }

        System.out.println(
            "Jumlah baris tabel = " +
            model.getRowCount()
        );

        tblLeaderboard.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            this,
            e.getMessage()
        );
    }
}
    
    private void tampilkanPosisiUser() {
        String query_score =
            "SELECT akun.username, scores.highscore, scores.current_score " +
            "FROM scores " +
            "JOIN akun ON scores.id_akun = akun.id " +
            "WHERE akun.id = " + session.idUser;

        try {
            Statement st = db.con.createStatement();
            ResultSet rs = st.executeQuery(query_score);

            if (rs.next()) {
                String username = rs.getString("username");
                int highscore = rs.getInt("highscore");
                currentScore = rs.getInt("current_score");

                String query_rank = "SELECT COUNT(*) + 1 AS rank_user FROM scores WHERE highscore > " + highscore;

                Statement stRank = db.con.createStatement();
                ResultSet rsRank = stRank.executeQuery(query_rank);

                if (rsRank.next()) {
                    int rankUser = rsRank.getInt("rank_user");

                    lblUsernameLogin.setText(username);
                    lblCurrentScore.setText(String.valueOf(currentScore));

                    lblRankUser.setText("#" + rankUser);
                    lblUsernameUser.setText(username.toUpperCase());
                    lblHighscoreUser.setText(String.valueOf(highscore));
                }

            } else {
                lblUsernameLogin.setText(usernameLogin);
                lblCurrentScore.setText("0");

                lblRankUser.setText("#-");
                lblUsernameUser.setText(usernameLogin.toUpperCase());
                lblHighscoreUser.setText("0");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal menampilkan posisi user!");
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

        panelUtama = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        panelInfoUser = new javax.swing.JPanel();
        lblLoggedIn = new javax.swing.JLabel();
        lblUsernameLogin = new javax.swing.JLabel();
        lblCurrentScoreText = new javax.swing.JLabel();
        lblCurrentScore = new javax.swing.JLabel();
        separatorInfoUser = new javax.swing.JSeparator();
        lblAvatarIcon = new javax.swing.JLabel();
        lblTrophyIcon = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelLeaderboard = new javax.swing.JPanel();
        lblTop10Header = new javax.swing.JLabel();
        scrollLeaderboard = new javax.swing.JScrollPane();
        tblLeaderboard = new javax.swing.JTable();
        panelPosisiKamu = new javax.swing.JPanel();
        lblPosisiHeader = new javax.swing.JLabel();
        lblRankText = new javax.swing.JLabel();
        lblRankUser = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblUsernameUser = new javax.swing.JLabel();
        lblHighscoreText = new javax.swing.JLabel();
        lblHighscoreUser = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnResetData = new javax.swing.JButton();
        btnStatistikGame = new javax.swing.JButton();
        lblMainMenu = new javax.swing.JButton();
        lblBurger = new javax.swing.JLabel();
        lblBurger1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelUtama.setBackground(new java.awt.Color(107, 45, 15));

        lblJudul.setFont(new java.awt.Font("Pixelify Sans", 1, 32)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(255, 255, 255));
        lblJudul.setText("LEADERBOARD");

        panelInfoUser.setBackground(new java.awt.Color(248, 220, 163));
        panelInfoUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(224, 175, 96), 2));

        lblLoggedIn.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        lblLoggedIn.setForeground(new java.awt.Color(43, 27, 18));
        lblLoggedIn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblLoggedIn.setText("Logged in as:");

        lblUsernameLogin.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        lblUsernameLogin.setForeground(new java.awt.Color(43, 27, 18));
        lblUsernameLogin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUsernameLogin.setText("Asya");

        lblCurrentScoreText.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        lblCurrentScoreText.setForeground(new java.awt.Color(43, 27, 18));
        lblCurrentScoreText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCurrentScoreText.setText("Current Score");

        lblCurrentScore.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
        lblCurrentScore.setForeground(new java.awt.Color(43, 27, 18));
        lblCurrentScore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCurrentScore.setText("8500");

        separatorInfoUser.setBackground(new java.awt.Color(120, 85, 45));
        separatorInfoUser.setForeground(new java.awt.Color(120, 85, 45));
        separatorInfoUser.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblAvatarIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvatarIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/avatar.png"))); // NOI18N

        lblTrophyIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/trophy.png"))); // NOI18N

        javax.swing.GroupLayout panelInfoUserLayout = new javax.swing.GroupLayout(panelInfoUser);
        panelInfoUser.setLayout(panelInfoUserLayout);
        panelInfoUserLayout.setHorizontalGroup(
            panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoUserLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblAvatarIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoggedIn)
                    .addComponent(lblUsernameLogin))
                .addGap(43, 43, 43)
                .addComponent(separatorInfoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(lblTrophyIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCurrentScoreText)
                    .addComponent(lblCurrentScore))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        panelInfoUserLayout.setVerticalGroup(
            panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoUserLayout.createSequentialGroup()
                .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoUserLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInfoUserLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblAvatarIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelInfoUserLayout.createSequentialGroup()
                                        .addComponent(lblLoggedIn, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblUsernameLogin))))
                            .addComponent(separatorInfoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelInfoUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelInfoUserLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(lblCurrentScoreText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCurrentScore, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelInfoUserLayout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(lblTrophyIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        panelLeaderboard.setBackground(new java.awt.Color(232, 188, 116));
        panelLeaderboard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 247, 220), 2, true));

        lblTop10Header.setBackground(new java.awt.Color(253, 140, 0));
        lblTop10Header.setFont(new java.awt.Font("Pixelify Sans", 1, 18)); // NOI18N
        lblTop10Header.setForeground(new java.awt.Color(255, 255, 255));
        lblTop10Header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTop10Header.setText("TOP 10 LEADERBOARD");
        lblTop10Header.setOpaque(true);

        scrollLeaderboard.setBackground(new java.awt.Color(240, 210, 154));
        scrollLeaderboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblLeaderboard.setBackground(new java.awt.Color(248, 220, 163));
        tblLeaderboard.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        tblLeaderboard.setForeground(new java.awt.Color(43, 27, 18));
        tblLeaderboard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Rank", "Username", "Highscore"
            }
        ));
        tblLeaderboard.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLeaderboard.setGridColor(new java.awt.Color(120, 85, 45));
        tblLeaderboard.setRowHeight(24);
        tblLeaderboard.setRowSelectionAllowed(false);
        tblLeaderboard.setSelectionBackground(new java.awt.Color(240, 210, 154));
        tblLeaderboard.setSelectionForeground(new java.awt.Color(43, 27, 18));
        tblLeaderboard.setShowGrid(true);
        scrollLeaderboard.setViewportView(tblLeaderboard);

        javax.swing.GroupLayout panelLeaderboardLayout = new javax.swing.GroupLayout(panelLeaderboard);
        panelLeaderboard.setLayout(panelLeaderboardLayout);
        panelLeaderboardLayout.setHorizontalGroup(
            panelLeaderboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLeaderboardLayout.createSequentialGroup()
                .addGroup(panelLeaderboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLeaderboardLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(lblTop10Header, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLeaderboardLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(scrollLeaderboard, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panelLeaderboardLayout.setVerticalGroup(
            panelLeaderboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLeaderboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTop10Header, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollLeaderboard, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelPosisiKamu.setBackground(new java.awt.Color(248, 220, 163));
        panelPosisiKamu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 188, 116), 10, true));

        lblPosisiHeader.setBackground(new java.awt.Color(76, 175, 80));
        lblPosisiHeader.setFont(new java.awt.Font("Pixelify Sans", 1, 18)); // NOI18N
        lblPosisiHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblPosisiHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosisiHeader.setText("POSISI KAMU");
        lblPosisiHeader.setOpaque(true);

        lblRankText.setFont(new java.awt.Font("Pixelify Sans", 1, 20)); // NOI18N
        lblRankText.setForeground(new java.awt.Color(43, 27, 18));
        lblRankText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRankText.setText("RANK");

        lblRankUser.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        lblRankUser.setForeground(new java.awt.Color(43, 27, 18));
        lblRankUser.setText("#15");

        jLabel1.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(43, 27, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("USERNAME");

        lblUsernameUser.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        lblUsernameUser.setForeground(new java.awt.Color(43, 27, 18));
        lblUsernameUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsernameUser.setText("ASYA");

        lblHighscoreText.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        lblHighscoreText.setForeground(new java.awt.Color(43, 27, 18));
        lblHighscoreText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHighscoreText.setText("HIGHSCORE");

        lblHighscoreUser.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        lblHighscoreUser.setForeground(new java.awt.Color(43, 27, 18));
        lblHighscoreUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHighscoreUser.setText("8500");

        jSeparator1.setBackground(new java.awt.Color(122, 74, 34));
        jSeparator1.setForeground(new java.awt.Color(120, 85, 45));

        jSeparator2.setBackground(new java.awt.Color(120, 85, 45));
        jSeparator2.setForeground(new java.awt.Color(120, 85, 45));

        javax.swing.GroupLayout panelPosisiKamuLayout = new javax.swing.GroupLayout(panelPosisiKamu);
        panelPosisiKamu.setLayout(panelPosisiKamuLayout);
        panelPosisiKamuLayout.setHorizontalGroup(
            panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPosisiKamuLayout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPosisiKamuLayout.createSequentialGroup()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPosisiKamuLayout.createSequentialGroup()
                                .addComponent(lblPosisiHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67))))))
            .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                .addGroup(panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(lblRankText))
                    .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lblRankUser)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                .addGroup(panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblUsernameUser))
                            .addComponent(jLabel1)))
                    .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(lblHighscoreText))
                    .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lblHighscoreUser, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelPosisiKamuLayout.setVerticalGroup(
            panelPosisiKamuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPosisiKamuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPosisiHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRankText)
                .addGap(8, 8, 8)
                .addComponent(lblRankUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUsernameUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHighscoreText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHighscoreUser)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnResetData.setBackground(new java.awt.Color(214, 58, 44));
        btnResetData.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        btnResetData.setForeground(new java.awt.Color(255, 255, 255));
        btnResetData.setText("RESET DATA");
        btnResetData.setBorderPainted(false);
        btnResetData.setFocusPainted(false);
        btnResetData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetDataMouseClicked(evt);
            }
        });

        btnStatistikGame.setBackground(new java.awt.Color(253, 140, 0));
        btnStatistikGame.setFont(new java.awt.Font("Pixelify Sans", 1, 16)); // NOI18N
        btnStatistikGame.setForeground(new java.awt.Color(255, 255, 255));
        btnStatistikGame.setText("STATISTIK GAME");
        btnStatistikGame.setBorderPainted(false);
        btnStatistikGame.setFocusPainted(false);
        btnStatistikGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStatistikGameMouseClicked(evt);
            }
        });
        btnStatistikGame.addActionListener(this::btnStatistikGameActionPerformed);

        lblMainMenu.setBackground(new java.awt.Color(56, 141, 16));
        lblMainMenu.setFont(new java.awt.Font("Pixelify Sans", 1, 20)); // NOI18N
        lblMainMenu.setForeground(new java.awt.Color(43, 27, 18));
        lblMainMenu.setText("Main Menu");
        lblMainMenu.setBorder(null);
        lblMainMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMainMenuMouseClicked(evt);
            }
        });
        lblMainMenu.addActionListener(this::lblMainMenuActionPerformed);

        lblBurger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/burger.png"))); // NOI18N

        lblBurger1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/burger.png"))); // NOI18N

        javax.swing.GroupLayout panelUtamaLayout = new javax.swing.GroupLayout(panelUtama);
        panelUtama.setLayout(panelUtamaLayout);
        panelUtamaLayout.setHorizontalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUtamaLayout.createSequentialGroup()
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUtamaLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelUtamaLayout.createSequentialGroup()
                                .addComponent(panelLeaderboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelPosisiKamu, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panelInfoUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelUtamaLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelUtamaLayout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(lblBurger1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblJudul)
                        .addGap(27, 27, 27)
                        .addComponent(lblBurger, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(panelUtamaLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(btnResetData)
                .addGap(87, 87, 87)
                .addComponent(btnStatistikGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMainMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        panelUtamaLayout.setVerticalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUtamaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblJudul)
                        .addComponent(lblBurger, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblBurger1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(panelInfoUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPosisiKamu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLeaderboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStatistikGame, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetData, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMainMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1298, 1298, 1298)
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStatistikGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatistikGameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnStatistikGameActionPerformed

    private void btnResetDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetDataMouseClicked
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin reset data " + usernameLogin + " ?",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            String hapus_score = "DELETE FROM scores WHERE id_akun = " + session.idUser;
            try {
                Statement st = db.con.createStatement();
                st.executeUpdate(hapus_score);

                JOptionPane.showMessageDialog(null, "Data " + usernameLogin + " berhasil direset");

                tampilkanTop10Leaderboard();
                tampilkanPosisiUser();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Gagal reset data!");
            }
        }
    }//GEN-LAST:event_btnResetDataMouseClicked

    private void btnStatistikGameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatistikGameMouseClicked
        // TODO add your handling code here:
        statistikGame form_statistik = new statistikGame(usernameLogin, currentScore);
        form_statistik.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnStatistikGameMouseClicked

    private void lblMainMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMainMenuMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Nanti diarahkan ke Main Menu");
        
        //nanti ganti jadi gini
//        MainMenuFrame form_menu = new MainMenuFrame();
//        form_menu.setVisible(true);
//        this.setVisible(false);

    }//GEN-LAST:event_lblMainMenuMouseClicked

    private void lblMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblMainMenuActionPerformed
        // TODO add your handling code here:
        MainMenu mainmenu = new MainMenu();
        this.setVisible(false);
        mainmenu.setVisible(true);

    }//GEN-LAST:event_lblMainMenuActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new leaderboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnResetData;
    private javax.swing.JButton btnStatistikGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAvatarIcon;
    private javax.swing.JLabel lblBurger;
    private javax.swing.JLabel lblBurger1;
    private javax.swing.JLabel lblCurrentScore;
    private javax.swing.JLabel lblCurrentScoreText;
    private javax.swing.JLabel lblHighscoreText;
    private javax.swing.JLabel lblHighscoreUser;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblLoggedIn;
    private javax.swing.JButton lblMainMenu;
    private javax.swing.JLabel lblPosisiHeader;
    private javax.swing.JLabel lblRankText;
    private javax.swing.JLabel lblRankUser;
    private javax.swing.JLabel lblTop10Header;
    private javax.swing.JLabel lblTrophyIcon;
    private javax.swing.JLabel lblUsernameLogin;
    private javax.swing.JLabel lblUsernameUser;
    private javax.swing.JPanel panelInfoUser;
    private javax.swing.JPanel panelLeaderboard;
    private javax.swing.JPanel panelPosisiKamu;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JScrollPane scrollLeaderboard;
    private javax.swing.JSeparator separatorInfoUser;
    private javax.swing.JTable tblLeaderboard;
    // End of variables declaration//GEN-END:variables
}
