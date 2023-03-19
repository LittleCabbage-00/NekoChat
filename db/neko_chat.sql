create database nekodb;
use nakedb;

/* 用户表 */
CREATE TABLE IF NOT EXISTS user (
    user_id varchar(80) not null,   	 /* 用户Id  */
    user_pwd varchar(25)  not null,	     /* 用户密码 */
    user_name varchar(80) not null,  	 /* 用户名 */
    user_icon varchar(100) not null, 	 /* 用户头像 */
    state   int(2) not null,
    PRIMARY KEY (user_id)
);
INSERT INTO user VALUES('111','123', '雷军','1','0');
INSERT INTO user VALUES('222','123', '金凡', '1','0');
INSERT INTO user VALUES('333','123', 'cloud米米', '2','0');
INSERT INTO user VALUES('888','123', '大哈鱼', '3','0');
INSERT INTO user VALUES('000','123','测试','10','0');
INSERT INTO user VALUES('1024','root','群聊','groupIcon','1');
INSERT INTO user VALUES('9999','9999','user_0','10','0');

/* 用户好友表Id1和Id2互为好友 */
CREATE TABLE IF NOT EXISTS friend (
    user_id1 varchar(80) not null,   	 /* 用户Id1  */
    user_id2 varchar(80) not null,   	 /* 用户Id2  */
    PRIMARY KEY (user_id1, user_id2)
);
/* 用户好友表Id1和Id2互为好友 */
INSERT INTO friend VALUES('111','222');
INSERT INTO friend VALUES('111','333');
INSERT INTO friend VALUES('888','111');
INSERT INTO friend VALUES('222','333');
INSERT INTO friend VALUES('111','1024');
INSERT INTO friend VALUES('222','1024');
INSERT INTO friend VALUES('333','1024');

CREATE TABLE `online_info` (
    `user_id` varchar(80) COLLATE utf8_czech_ci NOT NULL,
    `ip` varchar(45) COLLATE utf8_czech_ci DEFAULT NULL,
    `port` int(5) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
);
