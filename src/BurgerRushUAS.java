/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author Salsabila Ramadhania
 */
public class BurgerRushUAS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String usernameLogin = "Asya";
        int scoreAkhir = 9200;
        int totalOrder = 10;
        int orderSelesai = 8;
        int orderGagal = 2;

        // Membuka frame leaderboard
        leaderboard form_leaderboard = new leaderboard(usernameLogin);

        // Menyimpan hasil game ke database
        form_leaderboard.simpanHasilGame(
                usernameLogin,
                scoreAkhir,
                totalOrder,
                orderSelesai,
                orderGagal
        );

        // Menampilkan leaderboard
        form_leaderboard.setVisible(true);
    }
    
}
