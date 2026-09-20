-- Reglog MySQL Database Schema
-- Target database: localhost:3306 / reglog_db

CREATE DATABASE IF NOT EXISTS reglog_db;
USE reglog_db;

-- 1. Users Table
-- Plain-text passwords must NEVER be stored; only BCrypt hashes are inserted into the password column.
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_no VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. JWT_tokens Table
-- Stores issued JWT tokens with user identifier, created-at (cat), and expires-at (eat) timestamps.
CREATE TABLE IF NOT EXISTS JWT_tokens (
    tid BIGINT AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    token VARCHAR(512) NOT NULL,
    cat DATETIME NOT NULL,
    eat DATETIME NOT NULL,
    INDEX idx_jwt_token (token),
    INDEX idx_jwt_uid (uid),
    CONSTRAINT fk_jwt_user FOREIGN KEY (uid) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
