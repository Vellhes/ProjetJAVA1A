-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le :  Dim 14 juin 2020 à 13:37
-- Version du serveur :  8.0.18
-- Version de PHP :  7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `projet_java`
--

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `ID` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prénom` varchar(255) NOT NULL,
  `Fidelité` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`ID`, `nom`, `prénom`, `Fidelité`) VALUES
(5, 'DUSSAUSSOIS', 'Tom', 1),
(4, 'ISTE', 'Malo', 0),
(3, 'ISTE', 'Adrian', 0),
(2, 'BEHR', 'Arthur', 1),
(1, 'BEHR', 'Malo', 1),
(6, 'BEHR', 'Jean mIChel', 0),
(7, 'SCANU', 'Antoine', 0),
(8, 'SCANU', 'Louise', 1),
(9, 'LUCKHAUS', 'Pierre', 0);

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `ID` int(11) NOT NULL,
  `ID_client` int(11) NOT NULL,
  `reduction` tinyint(1) NOT NULL,
  `date_d` date NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`ID`, `ID_client`, `reduction`, `date_d`) VALUES
(5, 5, 1, '2020-06-08'),
(4, 4, 0, '2020-06-08'),
(3, 2, 1, '2020-06-08'),
(2, 5, 1, '2020-06-08'),
(1, 3, 0, '2020-06-07'),
(6, 1, 1, '2020-06-10'),
(7, 1, 1, '2020-06-10'),
(8, 7, 0, '2020-06-11'),
(9, 1, 1, '2020-06-11'),
(10, 5, 1, '2020-06-11');

-- --------------------------------------------------------

--
-- Structure de la table `emprunt`
--

DROP TABLE IF EXISTS `emprunt`;
CREATE TABLE IF NOT EXISTS `emprunt` (
  `ID` int(11) NOT NULL,
  `ID_prod` int(11) NOT NULL,
  `Tarif` float NOT NULL,
  `Quantité` int(11) NOT NULL,
  `date_d` date NOT NULL,
  `date_f` date NOT NULL,
  PRIMARY KEY (`ID`,`ID_prod`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `emprunt`
--

INSERT INTO `emprunt` (`ID`, `ID_prod`, `Tarif`, `Quantité`, `date_d`, `date_f`) VALUES
(2, 3, 10, 2, '2020-06-07', '2020-06-18'),
(2, 2, 15, 1, '2020-06-08', '2020-06-30'),
(2, 5, 37.8, 3, '2020-06-08', '2020-06-09'),
(4, 3, 0, 24, '2020-06-17', '2020-06-06'),
(1, 6, 39, 1, '2020-06-10', '2020-06-12'),
(1, 3, 0, 1, '2020-06-10', '2020-06-12'),
(1, 4, 168, 4, '2020-06-17', '2020-06-19'),
(2, 6, 234, 4, '2020-06-10', '2020-06-14'),
(1, 7, 12, 2, '2020-06-10', '2020-06-10'),
(6, 3, 202.5, 15, '2020-06-10', '2020-06-10'),
(4, 1, 15, 3, '2020-06-10', '2020-06-10'),
(5, 1, 9, 2, '2020-06-10', '2020-06-10'),
(2, 4, 12.6, 1, '2020-06-10', '2020-06-10'),
(7, 1, 13.5, 3, '2020-06-10', '2020-06-10'),
(7, 2, 14.4, 4, '2020-06-10', '2020-06-10'),
(7, 3, 40.5, 3, '2020-06-10', '2020-06-10'),
(8, 3, 90, 3, '2020-06-11', '2020-06-12'),
(8, 4, 0, 4, '2020-06-11', '0000-00-00');

-- --------------------------------------------------------

--
-- Structure de la table `produits`
--

DROP TABLE IF EXISTS `produits`;
CREATE TABLE IF NOT EXISTS `produits` (
  `ID` int(11) NOT NULL,
  `Type` varchar(255) NOT NULL,
  `Titre` varchar(255) NOT NULL,
  `Année` varchar(255) NOT NULL,
  `Réalisateur` varchar(255) NOT NULL,
  `Langue` varchar(255) NOT NULL,
  `Auteur` varchar(255) NOT NULL,
  `Tarif` int(11) NOT NULL,
  `Stock` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `produits`
--

INSERT INTO `produits` (`ID`, `Type`, `Titre`, `Année`, `Réalisateur`, `Langue`, `Auteur`, `Tarif`, `Stock`) VALUES
(2, 'Dictionnaire', 'Larousse', '', '', 'Français', '', 4, 43),
(3, 'BD', 'Injustice', '', '', '', 'Tom Taylor', 15, 20),
(1, 'CD', 'American Idiot', '2004', '', '', '', 5, 47),
(4, 'Roman', 'Harry potter', '', '', '', 'JK Rowling', 14, 44),
(5, 'Manuel', 'A mi me encanta', '', '', '', 'ecole', 7, 45),
(6, 'DVD', 'avatar', '', 'J Cameron', '', '', 13, 5),
(7, 'Roman', 'la cantatrice chaive', '', '', '', 'Ionesco', 6, 17),
(8, 'Dictionnaire', 'Le petit robert', '', '', 'Français', '', 3, 40);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
