-- init.sql: Script completo para inicializar la base de datos food_order_db

BEGIN; -- Iniciar una transacción

-- Crear la tabla 'orders' si no existe
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    item VARCHAR(100) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(50) NOT NULL
);

\echo 'Tabla "orders" creada o ya existente.'

-- Crear la tabla 'allowed_dishes' si no existe
CREATE TABLE IF NOT EXISTS allowed_dishes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

\echo 'Tabla "allowed_dishes" creada o ya existente.'

-- Insertar los platillos permitidos en la tabla 'allowed_dishes'
INSERT INTO allowed_dishes (name) VALUES
    ('Pizza Margherita') ON CONFLICT(name) DO NOTHING;
INSERT INTO allowed_dishes (name) VALUES
    ('Hamburguesa Clásica') ON CONFLICT(name) DO NOTHING;
INSERT INTO allowed_dishes (name) VALUES
    ('Ensalada César') ON CONFLICT(name) DO NOTHING;
INSERT INTO allowed_dishes (name) VALUES
    ('Tacos al Pastor') ON CONFLICT(name) DO NOTHING;
INSERT INTO allowed_dishes (name) VALUES
    ('Sopa de Tortilla') ON CONFLICT(name) DO NOTHING;

\echo 'Platillos permitidos insertados o ya existentes en "allowed_dishes".'

COMMIT; -- Confirmar la transacción

\echo 'Script init.sql completado.'