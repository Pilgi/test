create database order_test;
use order_test;
CREATE TABLE coupon (
    coupon_num INTEGER NOT NULL,
    menu_name VARCHAR(50) NULL,
    date TIMESTAMP NULL,
    available TINYINT NULL,
    picture VARCHAR(200) NULL,
    user_id VARCHAR(18) NOT NULL,
    name VARCHAR(18) NOT NULL,
    price INTEGER
)
;


ALTER TABLE coupon
	ADD  PRIMARY KEY (coupon_num,user_id,name)
;


CREATE TABLE menu
(
	menu_name  VARCHAR(50) NOT NULL,
	category  VARCHAR(50) NOT NULL,
	price  INTEGER NOT NULL default 0,
	thumbnail  VARCHAR(200) NULL,
	detail  VARCHAR(200) NULL,
	image  VARCHAR(200) NULL,
	check (price >=0)
)
;


ALTER TABLE menu
	ADD  PRIMARY KEY (menu_name,price)
;


CREATE TABLE notice
(
	num  INTEGER NOT NULL,
	title  varchar(20) NOT NULL,
	image  VARCHAR(200) NULL,
	content  VARCHAR(500) NULL
)
;


ALTER TABLE notice
	ADD  PRIMARY KEY (num)
;


CREATE TABLE order_info
(
	order_num  INTEGER NOT NULL,
	order_time  TIMESTAMP NOT NULL,
	table_name  varchar(20) NULL,
	total_price  INTEGER NOT NULL default 0,
	payment  TINYINT NOT NULL,
	etc  VARCHAR(20) NULL,
	user_id  VARCHAR(18) NOT NULL,
	name  VARCHAR(18) NULL,
	check (total_price >=0)
)
;


ALTER TABLE order_info
	ADD  PRIMARY KEY (order_num)
;


CREATE TABLE order_list
(
	order_num  INTEGER NOT NULL,
	menu_name  VARCHAR(50) NOT NULL,
	payment  TINYINT NULL,
	price  INTEGER NOT NULL
)
;


ALTER TABLE order_list
	ADD  PRIMARY KEY (order_num,menu_name)
;


CREATE TABLE user_info
(
	user_id  VARCHAR(18) NOT NULL,
	password  VARCHAR(18) NOT NULL,
	user_num  INTEGER NOT NULL,
	name  VARCHAR(18) NOT NULL,
	stamp_total  varchar(20) NULL default 0,
	stamp_available  INTEGER NULL default 0,
	balance  INTEGER NULL default 0,
	sex  TINYINT NOT NULL,
	e_mail  VARCHAR(100) NOT NULL,
	check (stamp_available >=0),
	check (stamp_total >=0)
)
;


ALTER TABLE user_info
	ADD  PRIMARY KEY (name,user_id)
;


ALTER TABLE coupon
	ADD FOREIGN KEY R_7 (menu_name,price) REFERENCES menu(menu_name,price)
;


ALTER TABLE coupon
	ADD FOREIGN KEY R_8 (name,user_id) REFERENCES user_info(name,user_id)
;


ALTER TABLE order_info
	ADD FOREIGN KEY R_2 (name,user_id) REFERENCES user_info(name,user_id)
;


ALTER TABLE order_list
	ADD FOREIGN KEY R_3 (order_num) REFERENCES order_info(order_num)
;


ALTER TABLE order_list
	ADD FOREIGN KEY R_5 (menu_name,price) REFERENCES menu(menu_name,price)
;


