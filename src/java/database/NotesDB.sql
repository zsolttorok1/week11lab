DROP DATABASE if exists NotesDB;
CREATE DATABASE NotesDB;

USE NotesDB;

-- create tables ***********************************************************************

CREATE TABLE `Role` (
  `RoleID` int(11) NOT NULL,
  `RoleName` varchar(25) NOT NULL,
  PRIMARY KEY (`RoleID`)
);

CREATE TABLE `User`(
    Username VARCHAR(10) NOT NULL,
    Password VARCHAR(10) NOT NULL,
    Email VARCHAR (30) NOT NULL,
    Active BIT NOT NULL,
    Firstname VARCHAR(50) NOT NULL,
    Lastname VARCHAR(50) NOT NULL,
    Role INT(11) NOT NULL,
    Salt VARCHAR (16),
    PRIMARY KEY (Username),
    KEY `FK_Role_User` (`Role`),
    CONSTRAINT `FK_Role_User` FOREIGN KEY (`Role`) REFERENCES `Role` (`RoleID`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE `Note` (
  `NoteID` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Title` varchar(30) NOT NULL,
  `Contents` varchar(20000) CHARACTER SET utf8 NOT NULL,
  `Owner` varchar(10) NOT NULL,
  PRIMARY KEY (`NoteID`),
  KEY `FK_Note_User` (`Owner`),
  CONSTRAINT `FK_Note_User` FOREIGN KEY (`Owner`) REFERENCES `User` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `Password_change_request` (
  `ID` varchar(128) NOT NULL,
  `Time` datetime NOT NULL,
  `Owner` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_Pass_User` FOREIGN KEY (`Owner`) REFERENCES `User` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- triggers ***********************************************************************
delimiter //

CREATE TRIGGER random_salt_generator
    BEFORE INSERT ON User
    FOR EACH ROW
BEGIN
    IF NEW.Salt IS NULL THEN SET NEW.Salt = HEX(RANDOM_BYTES(8)); 
    END IF;
END;//

CREATE TRIGGER new_date_on_password_change
    BEFORE INSERT ON Password_change_request
    FOR EACH ROW
BEGIN
    IF NEW.Time IS NULL THEN SET NEW.Time = NOW(); 
    END IF;
END;//

delimiter ;
  
-- inserts ***********************************************************************

INSERT INTO `Role` VALUES (1,'admin');
INSERT INTO `Role` VALUES (2,'regular user');

INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('admin', 'password', 'cprg352+admin@gmail.com', 1, 'Bob', 'Bobberson', 1);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('admin2', 'password', 'cprg352+admin2@gmail.com', 0, 'Admin2', 'Admin2', 1);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('admin3', 'password', 'cprg352+admin3@gmail.com', 1, 'Admin3', 'Admin3', 1);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('anne', 'password', 'cprg352+anne@gmail.com', 1, 'Anne', 'Annie', 2);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('barb', 'password', 'cprg352+barb@gmail.com', 0, 'Barb', 'Barker', 2);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('carl', 'password', 'cprg352+carl@gmail.com', 1, 'Carl', 'Carlson', 2);
INSERT INTO `User` (`Username`, `Password`, `Email`, `Active`,`Firstname`,`Lastname`, `Role`) VALUES ('zsolt', 'abcd', 'enter email plz', 1, 'Zsolt', 'Torok', 2);

INSERT INTO `Note` (`DateCreated`, `Title`, `Contents`, `Owner`) VALUES (NOW(), 'Sample note 1', 'This is a sample note.\n\nMore text in the sample note.', 'anne');
INSERT INTO `Note` (`DateCreated`, `Title`, `Contents`, `Owner`) VALUES (NOW(), 'Sample note 2', 'This is a sample note.\n\nMore text in the sample note.', 'anne');
INSERT INTO `Note` (`DateCreated`, `Title`, `Contents`, `Owner`) VALUES (NOW(), 'Sample note 3', 'This is a sample note.\n\nMore text in the sample note.', 'anne');
