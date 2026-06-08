import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Profile extends JFrame {
    private static final Color BG = new Color(49, 29, 15);
    private static final Color HEADER = new Color(114, 60, 21);
    private static final Color PANEL = new Color(248, 244, 236);
    private static final Color PANEL_ALT = new Color(238, 231, 219);
    private static final Color TEXT = new Color(64, 44, 27);
    private static final Color MUTED = new Color(117, 99, 81);
    private static final Color PRIMARY = new Color(67, 157, 82);
    private static final Color DANGER = new Color(182, 53, 53);
    private static final Color NEUTRAL = new Color(112, 94, 75);
    private static final Color ACCENT = new Color(242, 206, 127);

    private final KoneksiDB db = new KoneksiDB();

    private JLabel lblHeaderUser;
    private JLabel lblUsernameAktif;
    private JLabel lblStatus;
    private JTextField txtUsernameBaru;
    private JPasswordField txtPasswordBaru;
    private JPasswordField txtKonfirmasiPassword;
    private JButton btnSimpan;
    private JButton btnReset;
    private JButton btnHapus;
    private JButton btnLogout;
    private JButton btnKembali;

    public Profile() {
        initComponents();
        loadCurrentUser();
        GameWindow.apply(this, "PROFILE");
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setPreferredSize(GameWindow.SIZE);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setMinimumSize(GameWindow.SIZE);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(145, 97, 53)),
                new EmptyBorder(14, 22, 14, 22)));

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        center.setOpaque(false);
        center.add(new JLabel(loadScaledIcon("/img/Hamburger.png", 42, 42)));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("PROFILE");
        title.setFont(new Font("Times New Roman", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Kelola data akun dengan lebih rapi");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 237, 205));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        titleBox.add(title);
        titleBox.add(subtitle);

        center.add(titleBox);
        center.add(new JLabel(loadScaledIcon("/img/Hamburger.png", 42, 42)));

        JPanel chip = new JPanel();
        chip.setBackground(new Color(88, 42, 16));
        chip.setBorder(new CompoundBorder(
                new LineBorder(new Color(167, 123, 78), 1, true),
                new EmptyBorder(7, 12, 7, 12)));
        chip.setLayout(new BoxLayout(chip, BoxLayout.Y_AXIS));

        JLabel chipLabel = new JLabel("Akun Aktif");
        chipLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chipLabel.setForeground(ACCENT);
        chipLabel.setAlignmentX(CENTER_ALIGNMENT);

        lblHeaderUser = new JLabel("-");
        lblHeaderUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHeaderUser.setForeground(Color.WHITE);
        lblHeaderUser.setAlignmentX(CENTER_ALIGNMENT);

        chip.add(chipLabel);
        chip.add(lblHeaderUser);

        header.add(center, BorderLayout.CENTER);
        header.add(chip, BorderLayout.EAST);
        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new java.awt.GridBagLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(20, 20, 18, 20));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.34;
        gbc.insets = new Insets(0, 0, 0, 14);
        content.add(buildSummaryPanel(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.66;
        gbc.insets = new Insets(0, 14, 0, 0);
        content.add(buildFormPanel(), gbc);

        return content;
    }

    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_ALT);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(206, 189, 166), 1, true),
                new EmptyBorder(22, 20, 22, 20)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel avatar = new JLabel(loadScaledIcon("/img/Profile_3.png", 108, 108));
        avatar.setAlignmentX(CENTER_ALIGNMENT);

        JLabel section = new JLabel("RINGKASAN AKUN");
        section.setAlignmentX(CENTER_ALIGNMENT);
        section.setFont(new Font("Segoe UI", Font.BOLD, 12));
        section.setForeground(new Color(148, 104, 56));

        lblUsernameAktif = new JLabel("-");
        lblUsernameAktif.setAlignmentX(CENTER_ALIGNMENT);
        lblUsernameAktif.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblUsernameAktif.setForeground(TEXT);

        JLabel divider = new JLabel();
        divider.setAlignmentX(CENTER_ALIGNMENT);
        divider.setPreferredSize(new Dimension(1, 1));
        divider.setMaximumSize(new Dimension(999, 1));
        divider.setBorder(new MatteBorder(1, 0, 0, 0, new Color(214, 201, 183)));

        JLabel desc = new JLabel("<html><div style='width: 220px; line-height: 1.35'>"
                + "Gunakan halaman ini untuk memperbarui username atau password akun tanpa mengganggu alur game."
                + "</div></html>");
        desc.setAlignmentX(CENTER_ALIGNMENT);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(MUTED);

        JLabel noteTitle = new JLabel("Catatan");
        noteTitle.setAlignmentX(CENTER_ALIGNMENT);
        noteTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        noteTitle.setForeground(TEXT);

        JLabel note = new JLabel("<html><div style='width: 220px; line-height: 1.35'>"
                + "Biarkan field password kosong bila hanya ingin mengganti username."
                + "</div></html>");
        note.setAlignmentX(CENTER_ALIGNMENT);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(MUTED);

        lblStatus = new JLabel("Siap diperbarui.");
        lblStatus.setAlignmentX(CENTER_ALIGNMENT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(96, 77, 50));

        panel.add(avatar);
        panel.add(Box.createVerticalStrut(14));
        panel.add(section);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblUsernameAktif);
        panel.add(Box.createVerticalStrut(14));
        panel.add(divider);
        panel.add(Box.createVerticalStrut(14));
        panel.add(desc);
        panel.add(Box.createVerticalStrut(16));
        panel.add(noteTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(note);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblStatus);

        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(PANEL);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(211, 199, 183), 1, true),
                new EmptyBorder(22, 22, 22, 22)));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel section = new JLabel("DETAIL PROFIL");
        section.setFont(new Font("Segoe UI", Font.BOLD, 12));
        section.setForeground(new Color(148, 104, 56));

        JLabel title = new JLabel("Perbarui Data Akun");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel hint = new JLabel("Username baru harus unik. Password bisa diubah terpisah.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(MUTED);

        heading.add(section);
        heading.add(Box.createVerticalStrut(4));
        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(hint);

        JPanel fields = new JPanel(new java.awt.GridBagLayout());
        fields.setOpaque(false);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        txtUsernameBaru = new JTextField(24);
        txtPasswordBaru = new JPasswordField(24);
        txtKonfirmasiPassword = new JPasswordField(24);

        styleField(txtUsernameBaru);
        styleField(txtPasswordBaru);
        styleField(txtKonfirmasiPassword);

        fields.add(createFieldBlock("Username Baru", txtUsernameBaru), gbc);
        gbc.gridy++;
        fields.add(createFieldBlock("Password Baru", txtPasswordBaru), gbc);
        gbc.gridy++;
        fields.add(createFieldBlock("Konfirmasi Password", txtKonfirmasiPassword), gbc);

        JPanel actionBar = new JPanel(new BorderLayout(0, 8));
        actionBar.setOpaque(false);

        JPanel actionRows = new JPanel(new java.awt.GridLayout(2, 1, 0, 8));
        actionRows.setOpaque(false);

        JPanel navigationGroup = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        navigationGroup.setOpaque(false);

        JPanel accountGroup = new JPanel(new java.awt.GridLayout(1, 3, 10, 0));
        accountGroup.setOpaque(false);

        btnKembali = createButton("Kembali", new Color(96, 39, 10), Color.WHITE);
        btnKembali.addActionListener(evt -> kembaliKeMenu());

        btnReset = createButton("Reset", NEUTRAL, Color.WHITE);
        btnReset.addActionListener(evt -> resetForm());

        btnLogout = createButton("Logout", new Color(201, 112, 35), Color.WHITE);
        btnLogout.addActionListener(evt -> logout());

        btnHapus = createButton("Hapus Akun", DANGER, Color.WHITE);
        btnHapus.addActionListener(evt -> hapusAkun());

        btnSimpan = createButton("Simpan", PRIMARY, Color.WHITE);
        btnSimpan.addActionListener(evt -> simpanPerubahan());

        navigationGroup.add(btnKembali);
        navigationGroup.add(btnReset);

        accountGroup.add(btnLogout);
        accountGroup.add(btnHapus);
        accountGroup.add(btnSimpan);

        actionRows.add(navigationGroup);
        actionRows.add(accountGroup);

        actionBar.add(actionRows, BorderLayout.CENTER);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(fields, BorderLayout.CENTER);
        panel.add(actionBar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFieldBlock(String labelText, JTextField field) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        block.add(label, BorderLayout.NORTH);
        block.add(field, BorderLayout.CENTER);
        return block;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(196, 181, 162), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        field.setPreferredSize(new Dimension(340, 38));
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(background.darker(), 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        button.setMinimumSize(new Dimension(118, 38));
        button.setPreferredSize(new Dimension(132, 38));
        return button;
    }

    private ImageIcon loadScaledIcon(String resource, int width, int height) {
        java.net.URL url = getClass().getResource(resource);
        if (url == null) {
            return new ImageIcon();
        }

        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(0, 20, 18, 20));

        JLabel footerHint = new JLabel("Perubahan username dan password akan langsung disimpan ke database.");
        footerHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerHint.setForeground(new Color(232, 216, 195));

        footer.add(footerHint, BorderLayout.WEST);
        return footer;
    }

    private void loadCurrentUser() {
        if (session.idUser <= 0) {
            lblHeaderUser.setText("Guest");
            lblUsernameAktif.setText("Guest");
            txtUsernameBaru.setText("");
            lblStatus.setText("Silakan login terlebih dahulu.");
            return;
        }

        String username = session.username == null ? "-" : session.username;
        lblHeaderUser.setText(username);
        lblUsernameAktif.setText(username);
        txtUsernameBaru.setText(username);
        txtPasswordBaru.setText("");
        txtKonfirmasiPassword.setText("");
        lblStatus.setText("Siap diperbarui.");
    }

    private void resetForm() {
        loadCurrentUser();
        txtUsernameBaru.requestFocusInWindow();
    }

    private void simpanPerubahan() {
        if (session.idUser <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu.");
            return;
        }

        Connection conn = db.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal.");
            return;
        }

        String currentUsername = session.username == null ? "" : session.username;
        String newUsername = txtUsernameBaru.getText().trim();
        String newPassword = new String(txtPasswordBaru.getPassword());
        String confirmPassword = new String(txtKonfirmasiPassword.getPassword());

        if (newUsername.isEmpty()) {
            newUsername = currentUsername;
        }

        boolean ubahUsername = !newUsername.equals(currentUsername);
        boolean ubahPassword = !newPassword.isEmpty() || !confirmPassword.isEmpty();

        if (!ubahUsername && !ubahPassword) {
            JOptionPane.showMessageDialog(this, "Tidak ada perubahan yang diisi.");
            return;
        }

        if (ubahPassword && !newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password baru dan konfirmasi tidak sama.");
            return;
        }

        try {
            if (ubahUsername) {
                try (PreparedStatement cek = conn.prepareStatement(
                        "SELECT id FROM akun WHERE username = ? AND id <> ?")) {
                    cek.setString(1, newUsername);
                    cek.setInt(2, session.idUser);

                    try (ResultSet rs = cek.executeQuery()) {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "Username sudah dipakai.");
                            return;
                        }
                    }
                }
            }

            if (ubahUsername) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE akun SET username = ? WHERE id = ?")) {
                    ps.setString(1, newUsername);
                    ps.setInt(2, session.idUser);
                    ps.executeUpdate();
                }
                session.username = newUsername;
            }

            if (ubahPassword) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE akun SET password = ? WHERE id = ?")) {
                    ps.setString(1, newPassword);
                    ps.setInt(2, session.idUser);
                    ps.executeUpdate();
                }
            }

            loadCurrentUser();
            lblStatus.setText("Profil berhasil diperbarui.");
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui profil: " + e.getMessage());
        }
    }

    private void hapusAkun() {
        if (session.idUser <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus akun ini? Semua data game akan ikut terhapus.",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = db.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal.");
            return;
        }

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psScores = conn.prepareStatement("DELETE FROM scores WHERE id_akun = ?");
                 PreparedStatement psStats = conn.prepareStatement("DELETE FROM player_stats WHERE id_akun = ?");
                 PreparedStatement psAkun = conn.prepareStatement("DELETE FROM akun WHERE id = ?")) {

                psScores.setInt(1, session.idUser);
                psScores.executeUpdate();

                psStats.setInt(1, session.idUser);
                psStats.executeUpdate();

                psAkun.setInt(1, session.idUser);
                int hasil = psAkun.executeUpdate();

                conn.commit();

                if (hasil > 0) {
                    session.idUser = 0;
                    session.username = null;
                    JOptionPane.showMessageDialog(this, "Akun berhasil dihapus.");
                    new loginburger().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Akun tidak ditemukan.");
                }
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ignore) {
                // ignore rollback errors
            }
            JOptionPane.showMessageDialog(this, "Gagal menghapus akun: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ignore) {
                // ignore reset errors
            }
        }
    }

    private void kembaliKeMenu() {
        new MainMenu().setVisible(true);
        dispose();
    }

    private void logout() {
        session.idUser = 0;
        session.username = null;
        new loginburger().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Profile().setVisible(true));
    }
}
