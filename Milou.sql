CREATE DATABASE IF NOT EXISTS Milou_email;
USE Milou_email;

CREATE TABLE emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code INT NOT NULL UNIQUE,
    sender VARCHAR(100) NOT NULL,
    recipients TEXT NOT NULL,
    subject VARCHAR(200),
    body TEXT,
    read boolean DEFAULT FALSE,
    sent_date DATETIME DEFAULT CURRENT_DATE
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    pass VARCHAR(100)
);