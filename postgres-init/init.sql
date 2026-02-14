-- Create databases if they don't exist
SELECT 'CREATE DATABASE orionpay_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'orionpay_db')\gexec
SELECT 'CREATE DATABASE orionpay_identity' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'orionpay_identity')\gexec
SELECT 'CREATE DATABASE orionpay_capture' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'orionpay_capture')\gexec
SELECT 'CREATE DATABASE orionpay_settlement' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'orionpay_settlement')\gexec

-- Connect to orionpay_db to create tables and insert data
\c orionpay_db

CREATE TABLE IF NOT EXISTS bin_info (
    bin_range VARCHAR(20) PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    card_type VARCHAR(20) NOT NULL,
    bank_name VARCHAR(100)
);

INSERT INTO bin_info (bin_range, brand, card_type, bank_name) VALUES
('4', 'VISA', 'CREDIT', 'Visa Default'),
('5', 'MASTERCARD', 'CREDIT', 'Mastercard Default'),
('34', 'AMEX', 'CREDIT', 'American Express'),
('37', 'AMEX', 'CREDIT', 'American Express'),
('6011', 'DISCOVER', 'CREDIT', 'Discover'),
('411111', 'VISA', 'CREDIT', 'Visa Test Bank'),
('555555', 'MASTERCARD', 'PREPAID', 'Mastercard Prepaid Test')
ON CONFLICT (bin_range) DO NOTHING;
