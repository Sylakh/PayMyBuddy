/* Setting up PAYMYBUDDY DB */
create database paymybuddy;
use paymybuddy;

/* TABLE user */
create table user(
    id int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL,
    balance decimal(6,2),
    nickname varchar(255),
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    user_actif boolean
);

INSERT INTO user (email, password, role, balance) VALUES 
('user@email.com', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', 'USER', 0),
('admin@email.com', '$2y$10$kp1V7UYDEWn17WSK16UcmOnFd1mPFVF6UkLrOOCGtf24HOYt8p1iC', 'ADMIN', 0),
('userA', '$2a$10$FyarOZiEmDf4b6dVI8ZYBOkVlyRa0HW8n9SaRw6sSon925hAQiCNm', 'USER', 1000),
('userB', '$2a$10$pdfSBcrDLssorgbXKxqC4uKKwehiyixk.TTKpxlks32S/UL33wgQ.', 'USER', 1000),
('userC', '$2a$10$.FC3iIKetaGX/vDpl6VmW.o6fa9zKSeLcls5coY7jjgymb9cSoXRu', 'USER', 1000),
('userD', '$2a$10$Zjyc7mnVKxI71PdSoJgCxOKbFmU1yn74hVPTWS9kdOS1Po/ZdHvDO', 'USER', 1000);

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
(4,6), (6,3);

commit;

/* TABLE bankaccount */
create table bankaccount(
    id int PRIMARY KEY AUTO_INCREMENT,
    bankname varchar(255),
    iban varchar(255),
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
    description varchar(255),
    status boolean,
    FOREIGN KEY (emitter_id) REFERENCES user(id)
);
commit;
