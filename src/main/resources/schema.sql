
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    age INT,
    job VARCHAR(100),
    company VARCHAR(100)
);
