/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 05/11/2022 11:04:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_book
-- ----------------------------
DROP TABLE IF EXISTS `tb_book`;
CREATE TABLE `tb_book`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '书名',
  `author` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `price` decimal(8, 2) NOT NULL COMMENT '价格',
  `number` int(11) NOT NULL COMMENT '库存',
  `publisher` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出版社',
  `type_id` int(11) NOT NULL COMMENT '外键，类型编号',
  `borrow_number` int(11) NOT NULL DEFAULT 0 COMMENT '借出去的数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_book
-- ----------------------------
INSERT INTO `tb_book` VALUES (2, '新编大学德语', '朱建华', 23.00, 10, '外语教学与研究出版社', 3, 0);
INSERT INTO `tb_book` VALUES (3, '瓦尔登湖', '梭罗', 10.60, 10, '中央编译出版社', 11, 0);
INSERT INTO `tb_book` VALUES (4, '杀死一只知更鸟', '哈珀•李', 48.00, 10, '译林出版社', 11, 0);
INSERT INTO `tb_book` VALUES (5, '趣说中国史1+2', '趣哥', 49.80, 10, '台海出版社', 5, 0);
INSERT INTO `tb_book` VALUES (6, '半小时漫画史', '陈磊', 24.90, 10, '中信出版社', 5, 0);
INSERT INTO `tb_book` VALUES (7, '习近平新时代中国特色社会主义思想学习', '中央宣传部', 23.80, 10, '学习出版社', 4, 0);
INSERT INTO `tb_book` VALUES (8, '为什么是中国', '金一南', 16.99, 10, '北京联合出版有限公司', 4, 0);
INSERT INTO `tb_book` VALUES (9, '初中生必背古诗词61篇', '中华书局', 16.00, 10, '中华书局', 1, 0);
INSERT INTO `tb_book` VALUES (10, '一本涂书初中物理', '张连生', 55.10, 10, '天津人民出版社', 7, 0);
INSERT INTO `tb_book` VALUES (11, '一本涂书初中化学', '张连生', 55.10, 10, '天津人民出版社', 8, 0);
INSERT INTO `tb_book` VALUES (12, '孙维刚初中数学', '孙维刚', 46.60, 10, '北京大学出版社', 2, 0);
INSERT INTO `tb_book` VALUES (13, '学霸笔记初中地理', '牛胜玉', 42.80, 10, '陕西师范大学出版社', 6, 0);
INSERT INTO `tb_book` VALUES (14, '曲一线初中生物', '曲一线', 44.30, 10, '教育科学出版社', 9, 0);
INSERT INTO `tb_book` VALUES (15, '大数据技术导论', '张寺宁', 19.40, 10, '西安科技大学出版社', 10, 0);
INSERT INTO `tb_book` VALUES (16, 'Java程序设计基础', '刘晓英', 27.70, 10, '清华大学出版社', 10, 0);
INSERT INTO `tb_book` VALUES (17, '四级单词一笑而过', '周思成', 26.40, 10, '机械工业出版社', 3, 0);
INSERT INTO `tb_book` VALUES (20, '红楼梦', '曹雪芹', 34.23, 10, '上海人民美术出版社', 6, 0);
INSERT INTO `tb_book` VALUES (22, '西游记', '吴承恩', 34.00, 10, '上海人民美术出版社', 6, 0);
INSERT INTO `tb_book` VALUES (23, '水浒传', '施耐庵', 25.80, 10, '上海人民美术出版社', 7, 0);
INSERT INTO `tb_book` VALUES (24, '大聪明全集', '大聪明', 96.00, 10, '大聪明集团', 13, 1);
INSERT INTO `tb_book` VALUES (27, '鬼又吹灯', '篱笆', 69.00, 1, '鬼口渴', 12, 0);
INSERT INTO `tb_book` VALUES (28, '考古全集1', '鬼吹灯', 88.00, 0, '鬼吹灯', 12, 0);

-- ----------------------------
-- Table structure for tb_borrow
-- ----------------------------
DROP TABLE IF EXISTS `tb_borrow`;
CREATE TABLE `tb_borrow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `borrow_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '借书日期',
  `return_time` timestamp(0) NULL DEFAULT NULL COMMENT '还书日期',
  `reader_id` int(11) NOT NULL COMMENT '外键，读者编号',
  `book_id` int(11) NOT NULL COMMENT '外键，图书编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_borrow
-- ----------------------------
INSERT INTO `tb_borrow` VALUES (11, '2022-11-03 23:16:07', NULL, 1, 24);
INSERT INTO `tb_borrow` VALUES (12, '2022-11-03 23:16:48', '2022-11-04 18:17:41', 1, 17);

-- ----------------------------
-- Table structure for tb_emp
-- ----------------------------
DROP TABLE IF EXISTS `tb_emp`;
CREATE TABLE `tb_emp`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `account` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `tel` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_emp
-- ----------------------------
INSERT INTO `tb_emp` VALUES (1, '张三', '男', 'admin111', 'a1111111', '13487476997', 0);
INSERT INTO `tb_emp` VALUES (2, '李四', '女', 'blue', '12345678', '18373207932', 1);
INSERT INTO `tb_emp` VALUES (3, '王五', '男', 'test', '12345678', '17374676084', 1);

-- ----------------------------
-- Table structure for tb_reader
-- ----------------------------
DROP TABLE IF EXISTS `tb_reader`;
CREATE TABLE `tb_reader`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `tel` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `account` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_reader
-- ----------------------------
INSERT INTO `tb_reader` VALUES (1, 'Marry', '男', '13487476997', 'Peter111', '12345670');
INSERT INTO `tb_reader` VALUES (2, 'Jack', '女', '12345678901', 'Jack111', '12345679');
INSERT INTO `tb_reader` VALUES (7, 'Hary', '女', '18373207932', 'Hary1111', '12345678');
INSERT INTO `tb_reader` VALUES (8, '曹某人', '男', '18944962331', 'Tom12345', '1qqqqqqqqqqqqqqq');

-- ----------------------------
-- Table structure for tb_root
-- ----------------------------
DROP TABLE IF EXISTS `tb_root`;
CREATE TABLE `tb_root`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_root
-- ----------------------------
INSERT INTO `tb_root` VALUES (1, 'admin111', '12345678');

-- ----------------------------
-- Table structure for tb_storage
-- ----------------------------
DROP TABLE IF EXISTS `tb_storage`;
CREATE TABLE `tb_storage`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '入库时间',
  `number` int(11) NOT NULL COMMENT '数量',
  `emp_id` int(11) NOT NULL COMMENT '外键，管理员编号',
  `book_id` int(11) NOT NULL COMMENT '外键，图书编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_storage
-- ----------------------------
INSERT INTO `tb_storage` VALUES (7, '2022-11-03 18:50:05', 10, 2, 2);
INSERT INTO `tb_storage` VALUES (8, '2022-11-03 18:50:05', 10, 2, 3);
INSERT INTO `tb_storage` VALUES (9, '2022-11-03 18:50:05', 10, 2, 4);
INSERT INTO `tb_storage` VALUES (10, '2022-11-03 18:50:05', 10, 2, 5);
INSERT INTO `tb_storage` VALUES (11, '2022-11-03 18:50:05', 10, 2, 6);
INSERT INTO `tb_storage` VALUES (12, '2022-11-03 18:50:05', 10, 2, 7);
INSERT INTO `tb_storage` VALUES (13, '2022-11-03 18:50:05', 10, 2, 8);
INSERT INTO `tb_storage` VALUES (14, '2022-11-03 18:50:05', 10, 2, 9);
INSERT INTO `tb_storage` VALUES (15, '2022-11-03 18:50:05', 10, 2, 10);
INSERT INTO `tb_storage` VALUES (16, '2022-11-03 18:50:05', 10, 2, 11);
INSERT INTO `tb_storage` VALUES (17, '2022-11-03 18:50:05', 10, 2, 12);
INSERT INTO `tb_storage` VALUES (18, '2022-11-03 18:50:05', 10, 2, 13);
INSERT INTO `tb_storage` VALUES (19, '2022-11-03 18:50:05', 10, 2, 14);
INSERT INTO `tb_storage` VALUES (20, '2022-11-03 18:50:05', 10, 2, 15);
INSERT INTO `tb_storage` VALUES (21, '2022-11-03 18:50:05', 10, 2, 16);
INSERT INTO `tb_storage` VALUES (22, '2022-11-03 18:50:05', 10, 2, 17);
INSERT INTO `tb_storage` VALUES (23, '2022-11-03 18:50:05', 10, 2, 20);
INSERT INTO `tb_storage` VALUES (24, '2022-11-03 18:50:05', 10, 2, 22);
INSERT INTO `tb_storage` VALUES (25, '2022-11-03 18:50:05', 10, 2, 23);
INSERT INTO `tb_storage` VALUES (26, '2022-11-03 18:50:05', 10, 2, 24);
INSERT INTO `tb_storage` VALUES (27, '2022-11-04 03:10:15', 1, 2, 27);

-- ----------------------------
-- Table structure for tb_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_type`;
CREATE TABLE `tb_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `number` int(11) NOT NULL DEFAULT 0 COMMENT '书架号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_type
-- ----------------------------
INSERT INTO `tb_type` VALUES (1, '语文', 10);
INSERT INTO `tb_type` VALUES (2, '数学', 10);
INSERT INTO `tb_type` VALUES (3, '英语', 20);
INSERT INTO `tb_type` VALUES (4, '政治', 20);
INSERT INTO `tb_type` VALUES (5, '历史', 20);
INSERT INTO `tb_type` VALUES (6, '地理', 30);
INSERT INTO `tb_type` VALUES (7, '物理', 20);
INSERT INTO `tb_type` VALUES (8, '化学', 10);
INSERT INTO `tb_type` VALUES (9, '生物', 10);
INSERT INTO `tb_type` VALUES (10, '计算机', 20);
INSERT INTO `tb_type` VALUES (11, '小说', 20);
INSERT INTO `tb_type` VALUES (12, '考古', 1);
INSERT INTO `tb_type` VALUES (13, '其它', 10);

-- ----------------------------
-- Procedure structure for pa
-- ----------------------------
DROP PROCEDURE IF EXISTS `pa`;
delimiter ;;
CREATE PROCEDURE `pa`()
begin

    declare i int;
    declare iddd int;

    declare done int default 0;
    # 1、游标的定义
    declare cur cursor for select id from tb_type;
    # 捕获系统抛出的 not found 错误，如果捕获到，将 done 设置为 1  相当于try异常
    declare continue handler for not found set done=1;

    # 2、打开游标
    open cur;

    www:loop
        # 3、使用游标
        fetch cur into i;

        set iddd = (select sum(number) from tb_book where type_id = i);

        if iddd is null then
            set iddd=0;
        end if;


        update tb_type
        set number = iddd
        where id = i;

    end loop ;
    # 4、关闭游标
    close cur;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
