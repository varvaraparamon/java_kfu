INSERT INTO "user" (name, surname, phone, email, age) VALUES
                                                          ('Ivan', 'Ivanov', '89990001122', 'ivan@mail.com', 30),
                                                          ('Petr', 'Petrov', '89990001123', 'petr@mail.com', 25),
                                                          ('Maria', 'Sidorova', '89990001124', 'maria@mail.com', 28);

INSERT INTO product (name, description, price) VALUES
                                                   ('Milk', '1 liter', 90.50),
                                                   ('Bread', 'White bread', 45.00),
                                                   ('Cheese', 'Russian cheese 200g', 150.00);

INSERT INTO cart (user_id) VALUES (1), (2);

INSERT INTO cart_product (cart_id, product_id, total_count) VALUES
                                                                (1, 1, 2),
                                                                (1, 2, 1);

INSERT INTO promocode (code, type, "value", usage_type, active, expires_at) VALUES
                                                                              ('PROMO10', 'PERCENT', 10.00, 'SINGLE_USE', true, '2030-12-31 23:59:59'),
                                                                              ('FIXED50', 'FIXED', 50.00, 'MULTI_USE', true, '2030-12-31 23:59:59'),
                                                                              ('EXPIRED', 'PERCENT', 20.00, 'SINGLE_USE', true, '2020-01-01 00:00:00');