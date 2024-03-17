/* Setting up PAYMYBUDDY DB */
create database paymybuddy;
use paymybuddy;

/* TABLE user */
create table user(
    id int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    nickname varchar(255) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    user_actif boolean
);

/* TABLE friendrelationship */
create table friendrelationship(
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int NOT NULL,
    friend_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

/* TABLE bankaccount */
create table bankaccount(
    id int PRIMARY KEY AUTO_INCREMENT,
    bankname varchar(255),
    iban varchar(255),
    balance decimal(6,2),
    user_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

/* TABLE transaction */
create table transaction(
    id int PRIMARY KEY AUTO_INCREMENT,
    emitter_id int not null,
    receiver_id int not null,
    withdraw boolean,
    amount decimal(6,2),
    date date,
    descrition varchar(255),
    status boolean,
    FOREIGN KEY (emitter_id) REFERENCES user(id)
);
commit;
