
INSERT INTO roles (name) SELECT 'GERENTE' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'GERENTE');
INSERT INTO roles (name) SELECT 'LOGISTICA' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'LOGISTICA');
INSERT INTO roles (name) SELECT 'VENDEDOR' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'VENDEDOR');
INSERT INTO roles (name) SELECT 'USUARIO' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USUARIO');
INSERT INTO sellers (name, lastname, age, address, phone, email, password, role_id)
VALUES (
           'Vendedor',
           'Principal',
           25,
           'Calle Falsa 123',
           '987654321',
           'seller@example.com',
           '$2a$10$ZdYUHQSkrKXo6JDLZ6Y/Tep.Jexpyo9DqXiXAKIL0GZBju8rqVEHm',
           (SELECT id FROM roles WHERE name = 'VENDEDOR')
       )