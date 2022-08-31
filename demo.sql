/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 31/08/2022 20:38:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ali_yun_instance_configs
-- ----------------------------
DROP TABLE IF EXISTS `ali_yun_instance_configs`;
CREATE TABLE `ali_yun_instance_configs` (
  `region` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_type_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `instance_type_family` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cpu_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cpu_core_count` int DEFAULT NULL,
  `memory_size` double DEFAULT NULL,
  `gpu_spec` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gpu_amount` int DEFAULT NULL,
  `instance_bandwidth_rx` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_bandwidth_tx` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_pps_rx` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_pps_tx` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ali_yun_instances
-- ----------------------------
DROP TABLE IF EXISTS `ali_yun_instances`;
CREATE TABLE `ali_yun_instances` (
  `zone_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status_category` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ali_yun_regions
-- ----------------------------
DROP TABLE IF EXISTS `ali_yun_regions`;
CREATE TABLE `ali_yun_regions` (
  `region_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `region_endpoint` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `local_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ali_yun_zones
-- ----------------------------
DROP TABLE IF EXISTS `ali_yun_zones`;
CREATE TABLE `ali_yun_zones` (
  `zone_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status_category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tencent_instance_configs
-- ----------------------------
DROP TABLE IF EXISTS `tencent_instance_configs`;
CREATE TABLE `tencent_instance_configs` (
  `zone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cpu_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_family` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cpu` int DEFAULT NULL,
  `memory` int DEFAULT NULL,
  `instance_bandwidth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_pps` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instance_charge_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tencent_regions
-- ----------------------------
DROP TABLE IF EXISTS `tencent_regions`;
CREATE TABLE `tencent_regions` (
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tencent_zones
-- ----------------------------
DROP TABLE IF EXISTS `tencent_zones`;
CREATE TABLE `tencent_zones` (
  `zone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `zone_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `zone_id` int NOT NULL,
  `zone_state` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
