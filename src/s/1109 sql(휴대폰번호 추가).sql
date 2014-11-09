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
	size  VARCHAR(20) NULL
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
	num_total_item integer not null,
	user_num  integer NULL,
	total_price  integer NOT NULL,
	payment  tinyint NOT NULL,
	complete tinyint NOT NULL,
	table_name  varchar(20) NULL,
	etc  varchar(20) NULL
);

ALTER TABLE order_info
	ADD  PRIMARY KEY (order_num);


CREATE TABLE order_list
(
	order_num  integer NOT NULL,
	item_num integer not null,
	menu_num  integer NOT NULL,
	payment  tinyint NOT NULL,
	payment_option tinyint not null,
	coupon_num integer not null,
	menu_name  varchar(50) NOT NULL,
	size  VARCHAR(20) DEFAULT NULL,
	price  integer NOT NULL
);

ALTER TABLE order_list
	ADD  PRIMARY KEY (order_num,item_num);

CREATE TABLE user_info
(
	user_id  varchar(18) NOT NULL,
	password  varchar(18) NOT NULL,
	user_num  integer NOT NULL,
	name  varchar(18) NOT NULL,
	phone varchar(14) NOT NULL,
	stamp_total  varchar(20) NULL DEFAULT 0,
	stamp_available  integer NULL DEFAULT 0,
	stamp_month integer NULL default 0,
	balance  integer NULL DEFAULT 0,
	sex  tinyint NOT NULL DEFAULT 0,
	e_mail  varchar(100) NOT NULL,
	birthday date NOT NULL,
	register_date  timestamp NULL DEFAULT current_timestamp,
	device_id varchar(300) null,
	latest_login timestamp null
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

CREATE TABLE balance_log
(
	user_num integer not null,
	modify_time timestamp null default current_timestamp,
	increase integer null default 0,
	decrease integer null default 0,
	balance integer not null,
	employee_num integer not null,
	employee_name varchar(18)
);

ALTER TABLE balance_log
	ADD primary	KEY (user_num,modify_time);

CREATE TABLE employee
(
	employee_num integer not null,
	employee_id varchar(18),
	employee_password varchar(18),
	name varchar(18),
	register_date timestamp not null default current_timestamp,
	phone varchar(20)
);

ALTER TABLE employee
	ADD primary key (employee_num);

CREATE TABLE music
(
	user_num integer not null,
	request_time timestamp not null default current_timestamp,
	message varchar(100)
);

ALTER TABLE music
	ADD primary key (user_num,request_time);

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
	ADD FOREIGN KEY ouser_inforder_list_ibfk_2 (menu_num) REFERENCES menu(menu_num);

ALTER TABLE order_list
	ADD FOREIGN KEY order_list_ibfk_2 (coupon_num) REFERENCES coupon(coupon_num);

ALTER TABLE balance_log
	ADD FOREIGN KEY balance_log_ibfk_1 (user_num) REFERENCES user_info(user_num);

ALTER TABLE balance_log
	ADD FOREIGN KEY balance_log_ibfk_2 (employee_num) REFERENCES employee(employee_num);

ALTER TABLE music
	ADD FOREIGN KEY music_ibfk_1 (user_num) REFERENCES user_info(user_num);