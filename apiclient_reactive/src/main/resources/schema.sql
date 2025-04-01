DROP TABLE IF EXISTS relationships;
CREATE TABLE IF NOT EXISTS  relationships (relation_id INT PRIMARY KEY AUTO_INCREMENT, cid VARCHAR(255), product VARCHAR(255), product_id VARCHAR(255),  create_at TIMESTAMP,  PRIMARY KEY (relation_id));
