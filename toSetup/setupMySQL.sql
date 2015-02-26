--
-- Create Database: `pmc`
--
CREATE DATABASE IF NOT EXISTS `pmc` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `pmc`;

-- --------------------------------------------------------

--
-- Create table `place`
--

CREATE TABLE IF NOT EXISTS `place` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `isTaken` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=129 ;

