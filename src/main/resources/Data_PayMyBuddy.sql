/* Setting up PAYMYBUDDY DB */
create database paymybuddy;
use paymybuddy;

/* TABLE user */
create table user(
    id int PRIMARY KEY AUTO_INCREMENT,
    email varchar(63) UNIQUE NOT NULL,
    password varchar(63) NOT NULL,
    role varchar(31) NOT NULL,
    balance decimal(6,2),
    nickname varchar(31),
    firstname VARCHAR(31),
    lastname VARCHAR(31),
    user_actif boolean
);

INSERT INTO user (email, password, role, balance, nickname) VALUES 
('user@email.com', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', 'USER', 2000, 'User'),
('admin@email.com', '$2y$10$kp1V7UYDEWn17WSK16UcmOnFd1mPFVF6UkLrOOCGtf24HOYt8p1iC', 'ADMIN', 2000, 'Admin'),
('userA@email.com', '$2a$10$c7xnYElkWkX79GFlZPONAOVjINHgtWjWRacpYsc30PhpEk4CruwzG', 'USER', 1000, 'UserA'),
('userB@email.com', '$2a$10$.Xrmk3gtZ55ORVl3kY6RV.ok8s.ZvEb03a3aleBNyTEvUBkXy/lVG', 'USER', 1000, 'UserB'),
('userC@email.com', '$2a$10$2WrJ9RxRot6URMtdHjv5JOMpoZA6RWrP.whZg4naUgtn9Zqvcja76', 'USER', 1000, 'UserC'),
('userD@email.com', '$2a$10$imu7.j92EPZ20KP/SrrI6uaM4Pl1Ehbde6YT98/fwWxv2WSbHF1UK', 'USER', 1000, 'UserD'),
('userE@email.com', '$$2a$10$qnJUZA0YmPh/8mfWWW1WwOSFo4McDZba72v7U5.4CCjkUMrVFqTKO', 'USER', 1000, 'UserE');

commit;

/* TABLE friendrelationship */
create table friendrelationship(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int NOT NULL,
    friend_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO friendrelationship (user_id, friend_id) VALUES
(3,4), (4,3),
(3,5), (5,3),
(4,6), (6,4);

commit;

/* TABLE bankaccount */
create table bankaccount(
    id int PRIMARY KEY AUTO_INCREMENT,
    bankname varchar(63),
    iban varchar(31),
    user_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO bankaccount (bankname, iban, user_id) VALUES
('banque', 'ibanA1', 3),
('banque', 'ibanA2', 3),
('banque', 'ibanB1', 4),
('banque', 'ibanB2', 4),
('banque', 'ibanC1', 5),
('banque', 'ibanC2', 5),
('banque', 'ibanD1', 6),
('banque', 'ibanD2', 6);

commit;

/* TABLE transaction */
create table transaction(
    id int PRIMARY KEY AUTO_INCREMENT,
    emitter_id int not null,
    receiver_id int not null,
    withdraw boolean,
    amount decimal(6,2),
    date date,
    description varchar(63),
    status boolean,
    FOREIGN KEY (emitter_id) REFERENCES user(id)
);
commit;
