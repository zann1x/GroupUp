--
-- Datenbank: `groupup`
--
DROP DATABASE `groupup`;
CREATE DATABASE IF NOT EXISTS `groupup` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `groupup`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `games`
--

DROP TABLE IF EXISTS `games`;
CREATE TABLE `games` (
  `id` int(11) NOT NULL,
  `name` char(255) NOT NULL
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
  `sessionid` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `player`
--

INSERT INTO `player` (`id`, `forename`, `surname`, `pseudonym`, `password`, `email`, `sessionid`) VALUES
(1, 'Lukas', 'Zanner', 'Zann1x', 'test', 'zannix@test.de', NULL),
(2, 'Christian', 'Goller', 'Flame4Fame', 'test', 'Flame4Fame@test.de', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `team`
--

DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` int(11) NOT NULL,
  `name` char(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `team_player_mapping`
--

DROP TABLE IF EXISTS `team_player_mapping`;
CREATE TABLE `team_player_mapping` (
  `teamId` int(11) NOT NULL,
  `playerId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indizes für die Tabelle `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pseudonym` (`pseudonym`);

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
  ADD PRIMARY KEY (`teamId`,`playerId`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `games`
--
ALTER TABLE `games`
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
