DROP TABLE IF EXISTS relationships;
CREATE TABLE IF NOT EXISTS  relationships (relation_id INT NOT NULL AUTO_INCREMENT, cid VARCHAR(255), product VARCHAR(255), productId VARCHAR(255),  create_at TIMESTAMP,  PRIMARY KEY (relation_id));
