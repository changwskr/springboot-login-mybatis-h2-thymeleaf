CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    address VARCHAR(255),
    age INT,
    job VARCHAR(100),
    company VARCHAR(100),
    dbName VARCHAR(10) DEFAULT 'DB1'
);


CREATE TABLE IF NOT EXISTS MEMBER (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    contact VARCHAR(100)
);