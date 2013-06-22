-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: gdc_dashboard
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.13.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert` (
  `id` int(10) NOT NULL,
  `level` varchar(20) DEFAULT NULL,
  `node_id` varchar(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `node_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert`
--

LOCK TABLES `alert` WRITE;
/*!40000 ALTER TABLE `alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `node_history`
--

DROP TABLE IF EXISTS `node_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_history` (
  `id` int(10) NOT NULL,
  `node_id` varchar(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `data_type` varchar(20) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `node_history`
--

LOCK TABLES `node_history` WRITE;
/*!40000 ALTER TABLE `node_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `node_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `node_status`
--

DROP TABLE IF EXISTS `node_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_status` (
  `node_id` varchar(20) NOT NULL DEFAULT '',
  `data_type` varchar(20) NOT NULL DEFAULT '',
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`node_id`,`data_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `node_status`
--

LOCK TABLES `node_status` WRITE;
/*!40000 ALTER TABLE `node_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `node_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seq_table`
--

DROP TABLE IF EXISTS `seq_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seq_table` (
  `name` varchar(20) NOT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seq_table`
--

LOCK TABLES `seq_table` WRITE;
/*!40000 ALTER TABLE `seq_table` DISABLE KEYS */;
INSERT INTO `seq_table` VALUES ('alert',200),('node_history',150);
/*!40000 ALTER TABLE `seq_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-15  4:32:58
