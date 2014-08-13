drop database cafe;
create database cafe;
use cafe;

CREATE TABLE coupon
(
	coupon_num  integer NOT NULL,
	menu_num integer not null,
	menu_name  varchar(50) NULL,
	create_date  timestamp NULL DEFAULT current_timestamp,
	use_date  timestamp NULL,
	available  tinyint NULL,
	picture  varchar(200) NULL,
	message  varchar(200) NULL,
	user_id  varchar(18) NULL,
	from_user_id  varchar(18) NULL,
	name  varchar(18) NULL,
	from_user_name  varchar(18) NULL,
	size  VARCHAR(20) NULL,
	price  integer NULL
);


ALTER TABLE coupon
	ADD  PRIMARY KEY (coupon_num);


CREATE TABLE menu
(
	menu_num  integer NOT NULL,
	category  varchar(50) NOT NULL,
	category_order integer not null,
	menu_name  varchar(50) NOT NULL,
	price  integer NOT NULL,
	thumbnail  varchar(200) NULL,
	detail  varchar(200) NULL,
	image  varchar(200) NULL,
	add_date  timestamp NULL DEFAULT current_timestamp,
	size  VARCHAR(20) NOT NULL
);


ALTER TABLE menu
	ADD  PRIMARY KEY (menu_num);

CREATE UNIQUE INDEX lineing_menu ON menu
(
	category,
	category_order
);

CREATE UNIQUE INDEX Unique_menu ON menu
(
	menu_name,
	size,
	price
);

CREATE TABLE notice
(
	num  integer NOT NULL,
	writing_type tinyint not null default 0,
	title  varchar(20) NOT NULL,
	image  varchar(200) NULL,
	content  varchar(500) NULL,
	wirte_date  timestamp NULL DEFAULT current_timestamp
);

ALTER TABLE notice
	ADD  PRIMARY KEY (num);

CREATE TABLE order_info
(
	order_num  integer NOT NULL,
	order_time  timestamp NULL DEFAULT current_timestamp,
	table_name  varchar(20) NULL,
	total_price  integer NOT NULL,
	payment  tinyint NOT NULL,
	etc  varchar(20) NULL,
	user_num  integer NULL
);

ALTER TABLE order_info
	ADD  PRIMARY KEY (order_num);

create table order_pay
(
	order_num  integer NOT NULL,
	item_num integer not null,
	coupon_num  integer NULL default 0,
	pre_pay integer not null
);

ALTER TABLE order_pay
	ADD  PRIMARY KEY (order_num,coupon_num,pre_pay);


CREATE TABLE order_list
(
	order_num  integer NOT NULL,
	item_num integer not null,
	menu_num  integer NOT NULL,
	payment  tinyint NOT NULL,
	menu_name  varchar(50) NOT NULL,
	size  VARCHAR(20) DEFAULT NULL,
	price  integer NOT NULL,
	complete tinyint not null
);

ALTER TABLE order_list
	ADD  PRIMARY KEY (order_num,item_num);

CREATE TABLE user_info
(
	user_id  varchar(18) NOT NULL,
	password  varchar(18) NOT NULL,
	user_num  integer NOT NULL,
	name  varchar(18) NOT NULL,
	stamp_total  varchar(20) NULL DEFAULT 0,
	stamp_available  integer NULL DEFAULT 0,
	stamp_month integer NULL default 0,
	balance  integer NULL DEFAULT 0,
	sex  tinyint NOT NULL DEFAULT 0,
	e_mail  varchar(100) NOT NULL,
	birthday date NOT NULL,
	register_date  timestamp NULL DEFAULT current_timestamp
);

ALTER TABLE user_info
	ADD  PRIMARY KEY (user_num);

CREATE UNIQUE INDEX Unique_Key ON user_info
(
	user_id,
	name
);
CREATE UNIQUE INDEX user_id_Unique_Key ON user_info
(
	user_id
);

ALTER TABLE coupon
	ADD FOREIGN KEY coupon_ibfk_3 (user_id,name) REFERENCES user_info(user_id,name);

ALTER TABLE coupon
	ADD FOREIGN KEY coupon_ibfk_2 (from_user_id,from_user_name) REFERENCES user_info(user_id,name);

ALTER TABLE coupon
	ADD FOREIGN KEY coupon_ibfk_1 (menu_num) REFERENCES menu(menu_num);

ALTER TABLE order_info
	ADD FOREIGN KEY order_info_ibfk_1 (user_num) REFERENCES user_info(user_num);

ALTER TABLE order_list
	ADD FOREIGN KEY order_list_ibfk_1 (order_num) REFERENCES order_info(order_num);

ALTER TABLE order_list
	ADD FOREIGN KEY order_list_ibfk_2 (menu_num) REFERENCES menu(menu_num);

ALTER TABLE order_pay
	ADD FOREIGN KEY order_pay_ibfk_1 (order_num) REFERENCES order_info(order_num);

ALTER TABLE order_pay
	ADD FOREIGN KEY order_pay_ibfk_2 (coupon_num) REFERENCES coupon(coupon_num);
