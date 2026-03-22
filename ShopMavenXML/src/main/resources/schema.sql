CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(20) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(30) UNIQUE NOT NULL,
    age INT
);

CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(9, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS promocode (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(10) NOT NULL,
    "value" NUMERIC(10, 2) NOT NULL,
    usage_type VARCHAR(10) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    expires_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user" (id),
    applied_promo_code_id INT REFERENCES promocode (id),
    UNIQUE (id, user_id)
);

CREATE TABLE IF NOT EXISTS cart_product (
    id SERIAL PRIMARY KEY,
    cart_id INT NOT NULL REFERENCES cart (id),
    product_id INT NOT NULL REFERENCES product (id),
    total_count INT DEFAULT 0,
    UNIQUE (cart_id, product_id)
);
