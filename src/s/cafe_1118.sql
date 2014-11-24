-- MySQL dump 10.13  Distrib 5.5.40, for Linux (x86_64)
--
-- Host: localhost    Database: cafe
-- ------------------------------------------------------
-- Server version	5.5.40

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
-- Table structure for table `balance_log`
--

DROP TABLE IF EXISTS `balance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `balance_log` (
  `user_num` int(11) NOT NULL,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `increase` int(11) DEFAULT '0',
  `decrease` int(11) DEFAULT '0',
  `balance` int(11) NOT NULL,
  `employee_num` int(11) NOT NULL,
  `employee_name` varchar(18) DEFAULT NULL,
  PRIMARY KEY (`user_num`,`modify_time`),
  KEY `balance_log_ibfk_2` (`employee_num`),
  CONSTRAINT `balance_log_ibfk_1` FOREIGN KEY (`user_num`) REFERENCES `user_info` (`user_num`),
  CONSTRAINT `balance_log_ibfk_2` FOREIGN KEY (`employee_num`) REFERENCES `employee` (`employee_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balance_log`
--

LOCK TABLES `balance_log` WRITE;
/*!40000 ALTER TABLE `balance_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `balance_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon` (
  `coupon_num` int(11) NOT NULL,
  `menu_num` int(11) NOT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `use_date` timestamp NULL DEFAULT NULL,
  `available` tinyint(4) DEFAULT NULL,
  `picture` varchar(200) DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `user_id` varchar(18) DEFAULT NULL,
  `from_user_id` varchar(18) DEFAULT NULL,
  `name` varchar(18) DEFAULT NULL,
  `from_user_name` varchar(18) DEFAULT NULL,
  `size` varchar(20) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  PRIMARY KEY (`coupon_num`),
  KEY `coupon_ibfk_3` (`user_id`,`name`),
  KEY `coupon_ibfk_2` (`from_user_id`,`from_user_name`),
  KEY `coupon_ibfk_1` (`menu_num`),
  CONSTRAINT `coupon_ibfk_1` FOREIGN KEY (`user_id`, `name`) REFERENCES `user_info` (`user_id`, `name`),
  CONSTRAINT `coupon_ibfk_2` FOREIGN KEY (`from_user_id`, `from_user_name`) REFERENCES `user_info` (`user_id`, `name`),
  CONSTRAINT `coupon_ibfk_3` FOREIGN KEY (`menu_num`) REFERENCES `menu` (`menu_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `employee_num` int(11) NOT NULL,
  `name` varchar(18) DEFAULT NULL,
  `register_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `phone` varchar(20) DEFAULT NULL,
  `invisible` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`employee_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (141115001,'hhh','2014-11-15 22:16:16','000-0000-0000',0);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `menu_num` int(11) NOT NULL,
  `category` varchar(50) NOT NULL,
  `category_order` int(11) NOT NULL,
  `menu_name` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `thumbnail` varchar(200) DEFAULT NULL,
  `detail` varchar(200) DEFAULT NULL,
  `image` varchar(200) DEFAULT NULL,
  `add_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `size` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`menu_num`),
  UNIQUE KEY `lineing_menu` (`category`,`category_order`),
  UNIQUE KEY `Unique_menu` (`menu_name`,`size`,`price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'커피',1,'에스프레소 HOT',4000,'/image/thum_1.jpg','','/image/1.jpg','2014-11-16 17:43:47',''),(2,'커피',2,'에스프레소 ICE',4500,'/image/thum_2.jpg','','/image/2.jpg','2014-11-16 17:44:06',''),(3,'커피',3,'아메리카노 HOT',4000,'/image/thum_3.jpg','','/image/3.jpg','2014-11-16 17:44:34',''),(4,'커피',4,'아메리카노 ICE',4500,'/image/thum_4.jpg','','/image/4.jpg','2014-11-16 17:45:02',''),(5,'커피',5,'더치커피',6000,'/image/thum_5.jpg','','/image/5.jpg','2014-11-16 17:56:00',''),(6,'커피',6,'아포카토',5000,'/image/thum_6.jpg','','/image/6.jpg','2014-11-16 17:56:13',''),(7,'커피',7,'카페모카 HOT',5000,'/image/thum_7.jpg','','/image/7.jpg','2014-11-16 17:56:45',''),(8,'커피',8,'카페모카 ICE',5500,'/image/thum_8.jpg','','/image/8.jpg','2014-11-16 17:57:03',''),(9,'커피',9,'카라멜 마끼아또 HOT',5000,'/image/thum_9.jpg','','/image/9.jpg','2014-11-16 17:57:39',''),(10,'커피',10,'카라멜 마끼아또 ICE',5500,'/image/thum_10.jpg','','/image/10.jpg','2014-11-16 17:58:01',''),(11,'커피',11,'카페라떼 HOT',4500,'/image/thum_11.jpg','','/image/11.jpg','2014-11-16 17:58:32',''),(12,'커피',12,'카페라떼 ICE',5000,'/image/thum_12.jpg','','/image/12.jpg','2014-11-16 17:58:49',''),(13,'커피',13,'바닐라라떼 HOT',5000,'/image/thum_13.jpg','','/image/13.jpg','2014-11-16 17:59:07',''),(14,'커피',14,'바닐라라떼 ICE',5500,'/image/thum_14.jpg','','/image/14.jpg','2014-11-16 17:59:20',''),(15,'커피',15,'카라멜라떼 HOT',5000,'/image/thum_15.jpg','','/image/15.jpg','2014-11-16 17:59:41',''),(16,'커피',16,'카라멜라떼 ICE',5500,'/image/thum_16.jpg','','/image/16.jpg','2014-11-16 17:59:58',''),(17,'커피',17,'카푸치노 HOT',4500,'/image/thum_17.jpg','','/image/17.jpg','2014-11-16 18:00:26',''),(18,'커피',18,'카푸치노 ICE',5000,'/image/thum_18.jpg','','/image/18.jpg','2014-11-16 18:00:44',''),(19,'커피없는 라떼',1,'그린티라떼 HOT',4500,'/image/thum_19.jpg','','/image/19.jpg','2014-11-16 18:01:23',''),(20,'커피없는 라떼',2,'그린티라떼 ICE',5000,'/image/thum_20.jpg','','/image/20.jpg','2014-11-16 18:01:48',''),(21,'0',1,'홍차라떼',4500,'/image/thum_21.jpg','','/image/21.jpg','2014-11-16 18:02:05',''),(22,'커피없는 라떼',3,'홍차라떼 HOT',4500,'/image/thum_22.jpg','','/image/22.jpg','2014-11-16 18:02:48',''),(23,'커피없는 라떼',4,'홍차라떼 ICE',5000,'/image/thum_23.jpg','','/image/23.jpg','2014-11-16 18:03:07',''),(24,'커피없는 라떼',5,'레즈빈라떼 HOT',5000,'/image/thum_24.jpg','','/image/24.jpg','2014-11-16 18:03:27',''),(25,'커피없는 라떼',6,'차이티라떼 HOT',4500,'/image/thum_25.jpg','','/image/25.jpg','2014-11-16 18:03:53',''),(26,'커피없는 라떼',7,'차이티라떼 ICE',5000,'/image/thum_26.jpg','','/image/26.jpg','2014-11-16 18:04:11',''),(27,'커피없는 라떼',8,'고구마라떼 HOT',4500,'/image/thum_27.jpg','','/image/27.jpg','2014-11-16 18:04:34',''),(28,'커피없는 라떼',9,'고구마라떼 ICE',5000,'/image/thum_28.jpg','','/image/28.jpg','2014-11-16 18:04:54',''),(29,'커피없는 라떼',10,'곡물라떼 HOT',4500,'/image/thum_29.jpg','','/image/29.jpg','2014-11-16 18:05:11',''),(30,'커피없는 라떼',11,'곡물라떼 ICE',5000,'/image/thum_30.jpg','','/image/30.jpg','2014-11-16 18:05:27',''),(31,'커피없는 라떼',12,'초코 HOT',5000,'/image/thum_31.jpg','','/image/31.jpg','2014-11-16 18:05:59',''),(32,'커피없는 라떼',13,'초코 ICE',5500,'/image/thum_32.jpg','','/image/32.jpg','2014-11-16 18:06:17',''),(33,'티/와인',1,'카모마일',4500,'/image/thum_33.jpg','','/image/33.jpg','2014-11-16 18:21:49',''),(34,'티/와인',2,'우롱차',4500,'/image/thum_34.jpg','','/image/34.jpg','2014-11-16 18:23:24',''),(35,'티/와인',3,'페퍼민트',4500,'/image/thum_35.jpg','','/image/35.jpg','2014-11-16 18:23:51',''),(36,'티/와인',4,'홍차',4500,'/image/thum_36.jpg','','/image/36.jpg','2014-11-16 18:26:59',''),(37,'티/와인',5,'레몬티',5000,'/image/thum_37.jpg','','/image/37.jpg','2014-11-16 18:27:27',''),(38,'티/와인',6,'자몽티',5000,'/image/thum_38.jpg','','/image/38.jpg','2014-11-16 19:16:22',''),(41,'스무디/쉐이크',1,'딸기스무디',5500,'/image/thum_41.jpg','','/image/41.jpg','2014-11-16 19:18:17',''),(42,'스무디/쉐이크',2,'블루베리스무디',5500,'/image/thum_42.jpg','','/image/42.jpg','2014-11-16 19:18:44',''),(43,'스무디/쉐이크',3,'자몽스무디',5500,'/image/thum_43.jpg','','/image/43.jpg','2014-11-16 19:19:25',''),(44,'스무디/쉐이크',4,'요거트쉐이크스무디',6000,'/image/thum_44.jpg','','/image/44.jpg','2014-11-16 19:19:54',''),(45,'스무디/쉐이크',5,'복숭아요거트쉐이크',6500,'/image/thum_45.jpg','','/image/45.jpg','2014-11-16 19:22:52',''),(46,'스무디/쉐이크',6,'라즈베리요거트쉐이크',6500,'/image/thum_46.jpg','','/image/46.jpg','2014-11-16 19:23:14',''),(47,'에이드/생과일주스',1,'레몬에이드',5500,'/image/thum_47.jpg','','/image/47.jpg','2014-11-16 19:24:33',''),(48,'에이드/생과일주스',2,'자몽에이드',5500,'/image/thum_48.jpg','','/image/48.jpg','2014-11-16 19:25:04',''),(49,'에이드/생과일주스',3,'복숭아아이스티',4500,'/image/thum_49.jpg','','/image/49.jpg','2014-11-16 19:25:32',''),(50,'에이드/생과일주스',4,'토마토 주스',6000,'/image/thum_50.jpg','','/image/50.jpg','2014-11-16 19:26:03',''),(51,'에이드/생과일주스',5,'키위 주스',6000,'/image/thum_51.jpg','','/image/51.jpg','2014-11-16 19:26:35',''),(52,'에이드/생과일주스',6,'청포도 주스',6000,'/image/thum_52.jpg','','/image/52.jpg','2014-11-16 19:26:59',''),(53,'티/와인',7,'유자차',5000,'/image/thum_53.jpg','','/image/53.jpg','2014-11-17 00:10:59',''),(54,'티/와인',8,'와인',7000,'/image/thum_54.jpg','','/image/54.jpg','2014-11-17 00:11:05',''),(55,'0',2,'청포도주스',6000,'/image/thum_55.jpg','','/image/55.jpg','2014-11-17 11:03:45',''),(56,'프라푸치노',1,'쿠키앤프라푸치노',6000,'/image/thum_56.jpg','','/image/56.jpg','2014-11-17 11:05:09',''),(57,'프라푸치노',2,'레드빈프라푸치노',6000,'/image/thum_57.jpg','','/image/57.jpg','2014-11-17 11:05:28',''),(58,'프라푸치노',3,'그린티프라푸치노',6000,'/image/thum_58.jpg','','/image/58.jpg','2014-11-17 11:05:45',''),(59,'프라푸치노',4,'그린티프라푸치노/에스프레소',6500,'/image/thum_59.jpg','','/image/59.jpg','2014-11-17 11:06:09',''),(60,'프라푸치노',5,'자바칩프라푸치노',6000,'/image/thum_60.jpg','','/image/60.jpg','2014-11-17 11:06:34',''),(61,'프라푸치노',6,'민트초코프라푸치노',6000,'/image/thum_61.jpg','','/image/61.jpg','2014-11-17 11:06:55',''),(62,'사이드',1,'베이글/ 크림치즈',3500,'/image/thum_62.jpg','','/image/62.jpg','2014-11-17 11:07:20',''),(63,'사이드',2,'치즈케이크',4000,'/image/thum_63.jpg','','/image/63.jpg','2014-11-17 11:07:55',''),(64,'사이드',3,'츄러스',3500,'/image/thum_64.jpg','','/image/64.jpg','2014-11-17 11:08:12',''),(65,'사이드',4,'브라우니/아이스크림',4500,'/image/thum_65.jpg','','/image/65.jpg','2014-11-17 11:08:41','');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music` (
  `user_num` int(11) NOT NULL,
  `request_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `message` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_num`,`request_time`),
  CONSTRAINT `music_ibfk_1` FOREIGN KEY (`user_num`) REFERENCES `user_info` (`user_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (3,'2014-11-17 17:52:25','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-17 17:56:35','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-17 17:56:39','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-17 17:56:43','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-17 17:57:40','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-18 07:14:01','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-18 07:14:17','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-18 07:21:46','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-18 07:42:34','아이유!아이유! 우윳빛깔아유이유이유'),(3,'2014-11-18 08:02:57','아이유!아이유! 우윳빛깔아유이유이유'),(6,'2014-11-18 06:39:08','하이'),(6,'2014-11-18 06:39:31','하이'),(6,'2014-11-18 06:39:49','하이ㅋㅋㅋ'),(6,'2014-11-18 06:41:15','하이ㅋㅋㅋ'),(6,'2014-11-18 06:43:24','하이ㅋㅋㅋ'),(6,'2014-11-18 06:44:27','하이ㄴㄴㅋㅋㅋ'),(6,'2014-11-18 07:07:09','ff'),(6,'2014-11-18 07:43:16','손유빈바보'),(6,'2014-11-18 07:43:27','손유빈바보'),(6,'2014-11-18 07:43:34','손유빈바보'),(6,'2014-11-18 07:43:39','손유빈바보'),(6,'2014-11-18 07:43:44','손유빈바보'),(6,'2014-11-18 07:44:12','음악신청'),(12,'2014-11-18 07:46:24','김필기');
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice` (
  `num` int(11) NOT NULL,
  `writing_type` tinyint(4) NOT NULL DEFAULT '0',
  `title` varchar(20) NOT NULL,
  `image` varchar(200) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `write_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (2,0,'hhn','/notice/2.jpg','hn','2014-11-12 15:54:21'),(3,1,'55','/notice/3.jpg','66','2014-11-13 01:04:10');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
  `order_num` int(11) NOT NULL,
  `order_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `num_total_item` int(11) NOT NULL,
  `user_num` int(11) DEFAULT NULL,
  `total_price` int(11) NOT NULL,
  `payment` tinyint(4) NOT NULL,
  `complete` tinyint(4) NOT NULL DEFAULT '0',
  `table_name` varchar(20) DEFAULT NULL,
  `etc` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`order_num`),
  KEY `order_info_ibfk_1` (`user_num`),
  CONSTRAINT `order_info_ibfk_1` FOREIGN KEY (`user_num`) REFERENCES `user_info` (`user_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES (1,'2014-11-18 08:02:58',2,NULL,0,1,1,'어플','없음'),(2,'2014-11-18 08:02:59',2,NULL,0,1,1,'어플','없음'),(3,'2014-11-18 08:03:00',2,NULL,0,1,1,'어플','없음'),(4,'2014-11-18 08:07:14',2,NULL,0,1,1,'어플','없음'),(5,'2014-11-18 08:14:18',2,6,0,1,1,'어플','없음'),(6,'2014-11-18 08:19:45',2,6,0,1,1,'어플','없음'),(7,'2014-11-18 08:21:30',2,6,0,1,1,'어플','없음'),(8,'2014-11-18 08:28:46',8,6,0,1,1,'어플','없음'),(9,'2014-11-18 08:34:17',1,6,0,1,1,'어플','없음'),(10,'2014-11-18 08:37:02',1,6,0,1,1,'어플','없음');
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_list`
--

DROP TABLE IF EXISTS `order_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_list` (
  `order_num` int(11) NOT NULL,
  `item_num` int(11) NOT NULL,
  `menu_num` int(11) NOT NULL,
  `payment` tinyint(4) NOT NULL,
  `payment_option` tinyint(4) NOT NULL,
  `coupon_num` int(11) DEFAULT NULL,
  `menu_name` varchar(50) NOT NULL,
  `size` varchar(20) DEFAULT NULL,
  `price` int(11) NOT NULL,
  PRIMARY KEY (`order_num`,`item_num`),
  KEY `ouser_inforder_list_ibfk_2` (`menu_num`),
  KEY `order_list_ibfk_2` (`coupon_num`),
  CONSTRAINT `order_list_ibfk_1` FOREIGN KEY (`order_num`) REFERENCES `order_info` (`order_num`),
  CONSTRAINT `order_list_ibfk_2` FOREIGN KEY (`menu_num`) REFERENCES `menu` (`menu_num`),
  CONSTRAINT `order_list_ibfk_3` FOREIGN KEY (`coupon_num`) REFERENCES `coupon` (`coupon_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_list`
--

LOCK TABLES `order_list` WRITE;
/*!40000 ALTER TABLE `order_list` DISABLE KEYS */;
INSERT INTO `order_list` VALUES (5,1,1,1,1,NULL,'에스프레소 HOT','',4000),(5,2,2,1,1,NULL,'에스프레소 ICE','',4500),(6,1,1,1,1,NULL,'에스프레소 HOT','',4000),(6,2,2,1,1,NULL,'에스프레소 ICE','',4500),(7,1,1,1,1,NULL,'에스프레소 HOT','',4000),(7,2,2,1,1,NULL,'에스프레소 ICE','',4500),(8,1,1,1,1,NULL,'에스프레소 HOT','',4000),(8,2,2,1,1,NULL,'에스프레소 ICE','',4500),(8,3,3,1,1,NULL,'아메리카노 HOT','',4000),(8,4,4,1,1,NULL,'아메리카노 ICE','',4500),(9,1,1,1,1,NULL,'에스프레소 HOT','',4000),(10,1,1,1,1,NULL,'에스프레소 HOT','',4000);
/*!40000 ALTER TABLE `order_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password`
--

DROP TABLE IF EXISTS `password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password` (
  `password` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password`
--

LOCK TABLES `password` WRITE;
/*!40000 ALTER TABLE `password` DISABLE KEYS */;
INSERT INTO `password` VALUES ('1234');
/*!40000 ALTER TABLE `password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `user_id` varchar(18) NOT NULL,
  `password` varchar(18) NOT NULL,
  `user_num` int(11) NOT NULL,
  `name` varchar(18) NOT NULL,
  `phone` varchar(14) NOT NULL,
  `stamp_total` int(11) DEFAULT '0',
  `stamp_available` int(11) DEFAULT '0',
  `stamp_month` int(11) DEFAULT '0',
  `balance` int(11) DEFAULT '0',
  `sex` tinyint(4) NOT NULL DEFAULT '0',
  `e_mail` varchar(100) NOT NULL,
  `birthday` date NOT NULL,
  `register_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `device_id` varchar(300) DEFAULT NULL,
  `latest_login` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_num`),
  UNIQUE KEY `Unique_Key` (`user_id`,`name`),
  UNIQUE KEY `user_id_Unique_Key` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES ('아이디','비밀번호',2,'이름','전화번호',0,0,0,5000,0,'이메일','1999-09-03','2014-11-12 14:11:02',NULL,NULL),('필기','필기바보',3,'김필기2','011-123-4556',0,0,0,0,0,'e필기바보','1991-01-01','2014-11-12 14:22:46',NULL,NULL),('jjj','jjjjjj',4,'jjj','010-0000-0000',0,0,0,5000,0,'gggg','1999-09-09','2014-11-13 01:12:25',NULL,NULL),('미녀','뻥',5,'jjjjj','마차',0,0,0,8000,1,'없음','1995-01-05','2014-11-13 01:18:17',NULL,NULL),('aa','a',6,'jdjsjfjsjs','0165643',12,12,12,0,0,'jcjsjfjf','1991-11-11','2014-11-16 17:40:37','APA91bEjJmTW9dNG7sen3UO5bYi4_bDNsa5KoyYuxqxFQPLpJ1p5gi_fJ2bCE47bu_NnVJYAStYlhnltMli7lvFfgh57bnVgmDmr2liNPTzIcxxQX2uH98eVffi_0-4P2_QW__WbyScLLjLL34sJC6s7LeGusTqZyQ',NULL),('qwe','a',7,'a','5',0,0,0,0,0,'f','1991-11-11','2014-11-16 17:42:01',NULL,NULL),('fdfgvv','a',8,'a','5',0,0,0,0,0,'f','1991-11-11','2014-11-16 17:42:31',NULL,NULL),('affc','a',9,'a','8',0,0,0,0,0,'f','1991-11-11','2014-11-16 17:42:45',NULL,NULL),('adcx','a',10,'a','5',0,0,0,0,0,'d','1991-11-11','2014-11-16 17:42:58',NULL,NULL),('ccca','a',11,'a','8',0,0,0,0,0,'fc','1991-11-11','2014-11-16 17:43:15',NULL,NULL),('qqq','qqq',12,'김필기','010-8888-2569',0,0,0,0,0,'pilgi@naver.com','1992-02-11','2014-11-18 07:46:00','APA91bFK_04TxRaZWkcqRIoJ_qUCBbTw9KH6GzDSQrxqTlszSDc-8AEhyN5Tb7kwsOqnC_MwSWM6HUf1fBvU8tuOaaPftsQmPDl0bbeILDlgjFpnPSd3oGKpBwDuJR9kkEVPXlGigG_E9YPtRUJzBLWFaJnRAkj2dQ',NULL);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-18 10:06:48
