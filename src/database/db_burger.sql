CREATE DATABASE IF NOT EXISTS `burgergame`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE `burgergame`;

CREATE TABLE IF NOT EXISTS `akun` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `player_stats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_akun` int NOT NULL,
  `jumlah_burger_dibuat` int NOT NULL DEFAULT 0,
  `skor` int NOT NULL DEFAULT 0,
  `koin` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_akun` (`id_akun`),
  CONSTRAINT `fk_player_stats_akun`
    FOREIGN KEY (`id_akun`) REFERENCES `akun` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `scores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_akun` int NOT NULL,
  `highscore` int NOT NULL DEFAULT 0,
  `current_score` int NOT NULL DEFAULT 0,
  `total_order` int NOT NULL DEFAULT 0,
  `order_selesai` int NOT NULL DEFAULT 0,
  `order_gagal` int NOT NULL DEFAULT 0,
  `total_bermain` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_akun` (`id_akun`),
  CONSTRAINT `fk_scores_akun`
    FOREIGN KEY (`id_akun`) REFERENCES `akun` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `shop_data` (
  `id_data` int NOT NULL,
  `saldo` int NOT NULL DEFAULT 500,
  PRIMARY KEY (`id_data`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `inventory` (
  `id_bahan` int NOT NULL,
  `nama_bahan` varchar(50) NOT NULL,
  `stok` int NOT NULL DEFAULT 0,
  `harga` int NOT NULL,
  PRIMARY KEY (`id_bahan`),
  UNIQUE KEY `nama_bahan` (`nama_bahan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `bahan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama_bahan` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nama_bahan` (`nama_bahan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `resep_burger` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama_resep` varchar(100) NOT NULL,
  `urutan_bahan` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT IGNORE INTO `bahan` (`id`, `nama_bahan`) VALUES
  (1, 'Bread'),
  (2, 'Patty'),
  (3, 'Cheese'),
  (4, 'Lettuce'),
  (5, 'Egg'),
  (6, 'Salmon'),
  (7, 'Tomato'),
  (8, 'Onion');

INSERT IGNORE INTO `resep_burger` (`id`, `nama_resep`, `urutan_bahan`) VALUES
  (1, 'Cheese Burger', 'Bread,Patty,Cheese,Bread'),
  (2, 'Double Burger', 'Bread,Patty,Patty,Bread'),
  (3, 'Healthy Burger', 'Bread,Lettuce,Patty,Lettuce,Bread'),
  (4, 'Only Cheese', 'Cheese,Cheese'),
  (5, 'Egg Burger', 'Bread,Patty,Egg,Bread'),
  (6, 'Salmon Burger', 'Bread,Salmon,Lettuce,Bread'),
  (7, 'Fresh Burger', 'Bread,Patty,Tomato,Onion,Bread');

INSERT IGNORE INTO `shop_data` (`id_data`, `saldo`) VALUES
  (1, 500);

INSERT IGNORE INTO `inventory` (`id_bahan`, `nama_bahan`, `stok`, `harga`) VALUES
  (1, 'Bread', 10, 150),
  (2, 'Patty', 4, 200),
  (3, 'Onion', 10, 50),
  (4, 'Egg', 5, 100),
  (5, 'Cheese', 4, 150),
  (6, 'Lettuce', 10, 50),
  (7, 'Tomato', 6, 60),
  (8, 'Salmon', 3, 300);
