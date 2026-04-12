CREATE DATABASE IF NOT EXISTS ITP4511_DB
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ITP4511_DB;

CREATE TABLE IF NOT EXISTS customer (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL,
  phone VARCHAR(30),
  address VARCHAR(255)
);

INSERT INTO customer (name, email, phone, address)
SELECT 'Chan Tai Man', 'chan@example.com', '91234567', 'Kowloon'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE email = 'chan@example.com');

INSERT INTO customer (name, email, phone, address)
SELECT 'Lee Ka Yan', 'lee@example.com', '93456789', 'Hong Kong Island'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE email = 'lee@example.com');
