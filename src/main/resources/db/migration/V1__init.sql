CREATE TABLE product (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(255) NOT NULL,
     price FLOAT NOT NULL,
     quantity INTEGER NOT NULL,
     CONSTRAINT id UNIQUE (id)
);

INSERT INTO product (id, name, price, quantity) VALUES (1, 'Product A', 10.99, 10);
INSERT INTO product (id, name, price, quantity) VALUES (2, 'Product B', 10.99, 10);