-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 24. Mai 2018 um 22:27
-- Server-Version: 10.1.32-MariaDB
-- PHP-Version: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `groupup`
--
DROP DATABASE IF EXISTS `groupup`;
CREATE DATABASE IF NOT EXISTS `groupup` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `groupup`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `game`
--

DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `game`
--

INSERT INTO `game` (`id`, `name`) VALUES
(1, 'Counter-Strike: Global Offensive'),
(2, 'DOTA 2'),
(3, 'Fortnite');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `idinuse` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `group_player_mapping`
--

DROP TABLE IF EXISTS `group_player_mapping`;
CREATE TABLE `group_player_mapping` (
  `groupid` int(11) NOT NULL,
  `playerid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `player`
--

DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` int(11) NOT NULL,
  `forename` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `pseudonym` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `sessionid` char(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `player`
--

INSERT INTO `player` (`id`, `forename`, `surname`, `pseudonym`, `password`, `email`, `sessionid`) VALUES
(1, 'Lukas', 'Zanner', 'zann1x', 'test', 'zannix@test.de', NULL),
(2, 'Christian', 'Goller', 'Flame4Fame', 'test', 'Flame4Fame@test.de', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `team`
--

DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `team_player_mapping`
--

DROP TABLE IF EXISTS `team_player_mapping`;
CREATE TABLE `team_player_mapping` (
  `teamid` int(11) NOT NULL,
  `playerid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indizes für die Tabelle `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `group_name_uindex` (`name`);

--
-- Indizes für die Tabelle `group_player_mapping`
--
ALTER TABLE `group_player_mapping`
  ADD KEY `group_player_mapping_group_id_fk` (`groupid`),
  ADD KEY `group_player_mapping_player_id_fk` (`playerid`);

--
-- Indizes für die Tabelle `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pseudonym` (`pseudonym`),
  ADD UNIQUE KEY `player_sessionid_uindex` (`sessionid`);

--
-- Indizes für die Tabelle `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indizes für die Tabelle `team_player_mapping`
--
ALTER TABLE `team_player_mapping`
  ADD KEY `tp_mapping_team_id_fk` (`teamid`),
  ADD KEY `tp_mapping_player_id_fk` (`playerid`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `game`
--
ALTER TABLE `game`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT für Tabelle `group`
--
ALTER TABLE `group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `player`
--
ALTER TABLE `player`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT für Tabelle `team`
--
ALTER TABLE `team`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `group`
--
ALTER TABLE `group`
  ADD CONSTRAINT `group_player_pseudonym_fk` FOREIGN KEY (`name`) REFERENCES `player` (`pseudonym`);

--
-- Constraints der Tabelle `group_player_mapping`
--
ALTER TABLE `group_player_mapping`
  ADD CONSTRAINT `group_player_mapping_group_id_fk` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`),
  ADD CONSTRAINT `group_player_mapping_player_id_fk` FOREIGN KEY (`playerId`) REFERENCES `player` (`id`);

--
-- Constraints der Tabelle `team_player_mapping`
--
ALTER TABLE `team_player_mapping`
  ADD CONSTRAINT `tp_mapping_player_id_fk` FOREIGN KEY (`playerId`) REFERENCES `player` (`id`),
  ADD CONSTRAINT `tp_mapping_team_id_fk` FOREIGN KEY (`teamId`) REFERENCES `team` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
