CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(20) NOT NULL,
                        surname VARCHAR(20) NOT NULL,
                        phone VARCHAR(20) UNIQUE,
                        email VARCHAR(30) UNIQUE NOT NULL,
                        age INT
);

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(20) NOT NULL,
                         description TEXT NOT NULL,
                         price NUMERIC(9, 2) NOT NULL
);



CREATE TABLE cart (
                      id SERIAL PRIMARY KEY,
                      user_id INT NOT NULL,
                      applied_promo_code_id INT,
                      Foreign key (user_id) REFERENCES "user" (id),
                      UNIQUE (id, user_id)
);

CREATE TABLE cart_product (
                              id SERIAL PRIMARY KEY,
                              cart_id INT NOT NULL,
                              product_id INT NOT NULL,
                              total_count INT DEFAULT 0,
                              Foreign key (product_id) REFERENCES product (id),
                              Foreign key (cart_id) REFERENCES cart (id),
                              UNIQUE (cart_id, product_id)
);

CREATE TABLE promocode (
                           id SERIAL PRIMARY KEY,
                           code VARCHAR(20) NOT NULL UNIQUE,
                           type VARCHAR(10) NOT NULL,
                           "value" NUMERIC(10,2) NOT NULL,
                           usage_type VARCHAR(10) NOT NULL,
                           active BOOLEAN NOT NULL DEFAULT true,
                           expires_at TIMESTAMP NOT NULL
);