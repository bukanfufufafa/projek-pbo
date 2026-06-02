/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package burgerrushuas;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Salsabila Ramadhania
 */
public class Koneksi {
    Connection con;
    
    public Koneksi() {
        String id, pass, url, driver;
        id = "root";
        pass = "";
        url = "jdbc:mysql://localhost:3306/uas_pbo_burger_rush?serverTimezone=UTC";
        driver = "com.mysql.cj.jdbc.Driver";
        
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, id, pass);
            
            if (con == null) {
                System.out.println("KONEKSI GAGAL!!!");
            } else {
                System.out.println("KONEKSI BERHASIL!!!");
            }
                
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
             }
    }
    
    public static void main(String args[]) {
        Koneksi kon = new Koneksi();
    }
    
}
