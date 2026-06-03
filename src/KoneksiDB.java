
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author
 */
public class KoneksiDB {

    Connection con;

//    public Connection configDB() throws SQLException {
    public KoneksiDB() {
        String id, pass, url, driver;
        id = "root";
        pass = "";
        url = "jdbc:mysql://localhost:3306/burgergame?userTimezone=true&server=UTC";
        driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, id, pass);

            if (con == null) {
                System.out.println("KONEKSI GAGAL");
            } else {
                System.out.println("KONEKSI BERHASIL");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Koneksi");
            System.err.println("koneksi gagal " + e.getMessage());
        }
    }

    public static void main(String args[]) throws SQLException {
//        Connection C = (Connection)KoneksiDB.configDB();
        KoneksiDB kon = new KoneksiDB();
    }
    
    public Connection getConnection() {
        return con;
    }

}
