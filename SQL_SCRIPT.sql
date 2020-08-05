-- MySQL Script for setting up database structure for Student Management

DROP DATABASE IF EXISTS `student_management`;
CREATE DATABASE IF NOT EXISTS `student_management`;

DROP USER IF EXISTS 'student_manager'@'localhost';
CREATE USER 'student_manager'@'localhost' IDENTIFIED BY 'student_manager';
GRANT ALL PRIVILEGES ON student_management.* TO 'student_manager'@'localhost';

USE `student_management`;

-- ENTITY TABLES

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
    `id` int(15) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(128) NOT NULL,
    `last_name` varchar(128) NOT NULL,
    `enrollment_id` varchar(128) NOT NULL,
    `times_updated` int(15) DEFAULT 0,
    `last_modification` DATETIME DEFAULT NOW(),
    `created_at` DATETIME DEFAULT NOW(),
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `ENROLLMENT_ID_UNIQUE` (`enrollment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `phone`;

CREATE TABLE `phone` (
    `id` int(15) NOT NULL AUTO_INCREMENT,
    `phone_number` varchar(128) DEFAULT NULL,
    `phone_type` varchar(128) DEFAULT NULL,
    `times_updated` int(15) DEFAULT 0,
    `student_id` int(15) NOT NULL,
    `last_modification` DATETIME DEFAULT NOW(),
    `created_at` DATETIME DEFAULT NOW(),

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_STUDENT_ID` FOREIGN KEY (`student_id`)
    REFERENCES `student` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- AUDITING TABLES

-- Table for keeping track of student transactions that were made 
DROP TABLE IF EXISTS `student_history`;

CREATE TABLE `student_history` (
    `id` int(15) NOT NULL AUTO_INCREMENT,
    `student_id` int(15) NOT NULL,
    `first_name` varchar(128) NOT NULL,
    `last_name` varchar(128) NOT NULL,
    `enrollment_id` varchar(128) NOT NULL,
    `times_updated` int(15) DEFAULT 0,
    `last_modification` DATETIME NOT NULL,
    `created_at` DATETIME DEFAULT NOW(),
    `history_insertion` DATETIME NOT NULL DEFAULT NOW(),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Table for keeping track of phone transactions that were made 
DROP TABLE IF EXISTS `phone_history`;

CREATE TABLE `phone_history` (
    `id` int(15) NOT NULL AUTO_INCREMENT,
    `phone_id` int(15) NOT NULL,
    `phone_number` varchar(128) DEFAULT NULL,
    `phone_type` varchar(128) DEFAULT NULL,
    `times_updated` int(15) DEFAULT 0,
    `last_modification` DATETIME NOT NULL,
    `created_at` DATETIME DEFAULT NOW(),
    `history_insertion` DATETIME NOT NULL DEFAULT NOW(),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- AUDITING TRIGGERS: STUDENT

-- Trigger for keeping track of num. of updates and last modification
-- On student table
LOCK TABLES `student` WRITE;
DROP TRIGGER IF EXISTS tg_student_ctrl;

DELIMITER $$
CREATE TRIGGER tg_student_ctrl
BEFORE UPDATE
ON student FOR EACH ROW
BEGIN
    SET NEW.times_updated = OLD.times_updated + 1;
    SET NEW.last_modification = NOW();
END $$
DELIMITER ;

-- Trigger for keeping a history of transactions on student table
DROP TRIGGER IF EXISTS tg_student_hist_update;

DELIMITER $$
CREATE TRIGGER tg_student_hist_update
BEFORE UPDATE
ON student FOR EACH ROW
BEGIN

INSERT INTO student_history(student_id, first_name, last_name, enrollment_id, times_updated, last_modification, created_at, history_insertion)
VALUES (OLD.id, OLD.first_name, OLD.last_name, OLD.enrollment_id, OLD.times_updated, OLD.last_modification, OLD.created_at, NOW());

END $$
DELIMITER ;

DROP TRIGGER IF EXISTS tg_student_hist_delete;

DELIMITER $$
CREATE TRIGGER tg_student_hist_delete
BEFORE DELETE
ON student FOR EACH ROW
BEGIN

INSERT INTO student_history(student_id, first_name, last_name, enrollment_id, times_updated, last_modification, created_at, history_insertion)
VALUES (OLD.id, OLD.first_name, OLD.last_name, OLD.enrollment_id, OLD.times_updated, OLD.last_modification, OLD.created_at, NOW());

END $$
DELIMITER ;

UNLOCK TABLES;

-- AUDITING TRIGGERS: PHONE

-- Trigger for keeping track of num. of updates and last modification
-- On phone table
LOCK TABLES `phone` WRITE;
DROP TRIGGER IF EXISTS tg_phone_ctrl;

DELIMITER $$
CREATE TRIGGER tg_phone_ctrl
BEFORE UPDATE
ON phone FOR EACH ROW
BEGIN
    SET NEW.times_updated = OLD.times_updated + 1;
    SET NEW.last_modification = NOW();
END $$
DELIMITER ;

-- Trigger for keeping a history of transactions on phone table
DROP TRIGGER IF EXISTS tg_phone_hist_update;

DELIMITER $$
CREATE TRIGGER tg_phone_hist_update
BEFORE UPDATE
ON phone FOR EACH ROW
BEGIN

INSERT INTO phone_history(phone_id, phone_number, phone_type, times_updated, last_modification, created_at, history_insertion)
VALUES (OLD.id, OLD.phone_number, OLD.phone_type, OLD.times_updated, OLD.last_modification, OLD.created_at, NOW());

END $$
DELIMITER ;

DROP TRIGGER IF EXISTS tg_phone_hist_delete;

DELIMITER $$
CREATE TRIGGER tg_phone_hist_delete
BEFORE DELETE
ON phone FOR EACH ROW
BEGIN

INSERT INTO phone_history(phone_id, phone_number, phone_type, times_updated, last_modification, created_at, history_insertion)
VALUES (OLD.id, OLD.phone_number, OLD.phone_type, OLD.times_updated, OLD.last_modification, OLD.created_at, NOW());

END $$
DELIMITER ;

UNLOCK TABLES;

