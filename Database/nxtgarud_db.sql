-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 31, 2018 at 04:39 PM
-- Server version: 5.6.39-log
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nxtgarud_db`
--

-- --------------------------------------------------------

-- Table structure for table `payment_info`
--

CREATE TABLE `payment_info` (
  `id` int(11) NOT NULL,
  `number` text NOT NULL,
  `name` text NOT NULL,
  `amount` text NOT NULL,
  `pay_id` text NOT NULL,
  `service_name` text NOT NULL,
  `service_term` text NOT NULL,
  `date` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `registration`
--

CREATE TABLE `registration` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `email` text NOT NULL,
  `number` text NOT NULL,
  `gender` text NOT NULL,
  `dob` text NOT NULL,
  `password` text NOT NULL,
  `rpm` text NOT NULL,
  `kyc` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registration`
--

INSERT INTO `registration` (`id`, `name`, `email`, `number`, `gender`, `dob`, `password`, `rpm`, `kyc`) VALUES
(21, 'Rishabh Khandelwal', 'khandelwalrishabh333@gmail.com', '8871107447', 'MALE', '07/02/18', 'Rishabh333', '[{\"question\":\"com.adityagupta.nxtgaruda.data.Question@406145e\",\"answer\":\"Priyank\"},{\"question\":\"com.adityagupta.nxtgaruda.data.Question@f7f803f\",\"answer\":\"Cool\"}]', '[{\"first_name\":\"aditya\",\"last_name\":\"Gupta\",\"dropdown\":\"male\",\"fname\":\"pt-list.php\"}]'),
(22, 'Priyank Jain', 'priyank.jain@nxtvision.com', '8871965000', 'MALE', '11/02/17', 'Priyash15', '', ''),
(23, 'Rishabh Khandelwal', 'khandelwalrishabh333@gmail.com', '9111818196', 'MALE', '07/26/18', 'rishabh333', '[{\"question\":\"com.adityagupta.nxtgaruda.data.Question@7d6ed58\",\"answer\":\"Prefer.\"},{\"question\":\"com.adityagupta.nxtgaruda.data.Question@ce40eb1\",\"answer\":\"Strongly prefer.\"},{\"question\":\"com.adityagupta.nxtgaruda.data.Question@77a5f96\",\"answer\":\"High.\"},{\"question\":\"com.adityagupta.nxtgaruda.data.Question@c3e9717\",\"answer\":\"Intraday.\"}]', ''),
(24, 'Chotu', 'chotu.ck@gmail.com', '8319080074', 'MALE', '07/21/18', 'chotu33', '', ''),
(25, 'Narendra', 'narendra.mourya@nxtvision.com', '8770846759', 'MALE', '06/08/18', 'nsmourya', '[{\"question\":\"com.adityagupta.nxtgaruda.data.Question@8fcce25\",\"answer\":\"Rishabh\"},{\"question\":\"com.adityagupta.nxtgaruda.data.Question@113bfa\",\"answer\":\"Fine\"}]', ''),
(26, '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `des` text NOT NULL,
  `monthly` double NOT NULL,
  `quaterly` double NOT NULL,
  `hyearly` double NOT NULL,
  `yearly` double NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `payment_info`
--
ALTER TABLE `payment_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `registration`
--
ALTER TABLE `registration`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `payment_info`
--
ALTER TABLE `payment_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `registration`
--
ALTER TABLE `registration`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
