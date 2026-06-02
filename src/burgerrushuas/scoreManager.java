/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package burgerrushuas;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Salsabila Ramadhania
 */
public class scoreManager {
    Koneksi kon;

    public scoreManager() {
        kon = new Koneksi();
    }
    
    public void simpanAtauUpdateScore(String username, int scoreBaru, int totalOrderBaru, int orderSelesaiBaru, int orderGagalBaru) {
        String cek_data = "SELECT highscore FROM scores WHERE username = '" + username + "'";

        try {
            Statement st = kon.con.createStatement();
            ResultSet rs = st.executeQuery(cek_data);

            if (rs.next()) {
                int highscoreLama = rs.getInt("highscore");
                int highscoreFinal = highscoreLama;

                if (scoreBaru > highscoreLama) {
                    highscoreFinal = scoreBaru;
                }

                String update_score = "UPDATE scores SET "
                        + "highscore = '" + highscoreFinal + "', "
                        + "current_score = '" + scoreBaru + "', "
                        + "total_order = total_order + '" + totalOrderBaru + "', "
                        + "order_selesai = order_selesai + '" + orderSelesaiBaru + "', "
                        + "order_gagal = order_gagal + '" + orderGagalBaru + "', "
                        + "total_bermain = total_bermain + 1 "
                        + "WHERE username = '" + username + "'";
                
                 st.executeUpdate(update_score);
                 
            } else {
                String tambah_score = "INSERT INTO scores "
                        + "(username, highscore, current_score, total_order, order_selesai, order_gagal, total_bermain) "
                        + "VALUES "
                        + "('" + username + "', '" + scoreBaru + "', '" + scoreBaru + "', '" + totalOrderBaru + "', '" + orderSelesaiBaru + "', '" + orderGagalBaru + "', '1')";

                st.executeUpdate(tambah_score);
            }

        } catch (Exception e) {
            System.out.println("Gagal simpan/update score: " + e.getMessage());
        }
    }
    
}
