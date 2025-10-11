-- ========================================
-- DATASET INICIAL PARA PRUEBAS
-- Base de datos: booking-test
-- ========================================

-- ========================================
-- TABLA: user
-- ========================================

INSERT INTO `user` (
    `id`, `created_at`, `date_birth`, `email`, `is_host`,
    `name`, `password`, `phone`, `photo_url`, `role`, `status`
) VALUES
      ('c94d1f8e-22b5-4f12-bfc4-91b784e76a01', '2025-10-05 14:30:00', '1990-06-15', 'maria.gomez@example.com', b'0',
       'María Gómez', '$2b$10$A1bCdEfGhIjKlMnOpQrStUvWxYz1234567890abcdEfGhIj',
       '+573001112233', 'https://example.com/photos/maria.jpg', 0, 'ACTIVE'),

      ('3e8a67b0-7a12-40a5-9e33-99c68ff8f9e2', '2025-09-28 09:45:00', '1985-12-02', 'juan.perez@example.com', b'1',
       'Juan Pérez', '$2b$10$ZyXwVuTsRqPoNmLkJiHgFeDcBa9876543210ZYXWVUTSRQPON',
       '+573224445566', 'https://example.com/photos/juan.jpg', 1, 'ACTIVE'),

      ('f80d3b2a-ec84-4f60-a2b4-09f1c8f391a4', '2025-10-01 17:20:00', '1998-03-22', 'laura.mendoza@example.com', b'0',
       'Laura Mendoza', '$2b$10$MnOpQrStUvWxYz1234567890abcdefGhIjKlMnOpQrStUvWxY',
       NULL, NULL, 2, 'INACTIVE');

-- ========================================
-- TABLA: lodging
-- ========================================

INSERT INTO `lodging` (
    `id`, `address`, `city`, `latitude`, `longitude`, `avg_rating`,
    `created_at`, `description`, `max_guests`, `num_ratings`,
    `price_per_night`, `status`, `title`, `host_id`
) VALUES
      ('6a05a77b-72ce-4f21-a681-2b523f70e3d3', 'Carrera 45 #23-10', 'Medellín', 6.2518, -75.5636, 4.8,
       '2025-09-15 10:20:00',
       'Apartamento moderno en El Poblado con vista a la ciudad y acceso a piscina y gimnasio.',
       4, 56, 320000, 'ACTIVE', 'Apartamento moderno con vista panorámica', '3e8a67b0-7a12-40a5-9e33-99c68ff8f9e2'),

      ('0d27b3a5-93e4-49a1-93a2-ff1c3c8f431e', 'Calle 10 #5-22', 'Cartagena', 10.3910, -75.4794, 4.6,
       '2025-08-30 18:45:00',
       'Casa colonial en el centro histórico, ideal para grupos grandes, con terraza y jacuzzi.',
       8, 89, 580000, 'ACTIVE', 'Casa colonial en el centro histórico', '3e8a67b0-7a12-40a5-9e33-99c68ff8f9e'),

      ('87e90d55-cb43-4b21-9dc4-6db248084aaf', 'Carrera 7 #72-50', 'Bogotá', 4.6486, -74.0990, 4.2,
       '2025-10-02 09:10:00',
       'Estudio acogedor en Chapinero, cerca de restaurantes, transporte y zonas comerciales.',
       2, 34, 190000, 'INACTIVE', 'Estudio céntrico en Chapinero', '3e8a67b0-7a12-40a5-9e33-99c68ff8f9e');

-- ========================================
-- TABLA: reservation
-- ========================================

INSERT INTO `reservation` (
    `id`, `lodging_id`, `user_id`, `check_in`, `check_out`,
    `num_guests`, `total_price`, `status`, `created_at`
) VALUES
      ('47db8b2c-f417-44e2-86a4-10b5a21d04c4', '6a05a77b-72ce-4f21-a681-2b523f70e3d3', 'c94d1f8e-22b5-4f12-bfc4-91b784e76a01',
       '2025-10-10', '2025-10-15', 2, 1600000, 'CONFIRMED', '2025-09-30 10:00:00'),

      ('16e4a02e-dbb8-4f32-8232-9fc3859b9b8b', '0d27b3a5-93e4-49a1-93a2-ff1c3c8f431e', 'f80d3b2a-ec84-4f60-a2b4-09f1c8f391a4',
       '2025-11-05', '2025-11-12', 4, 4060000, 'PENDING',   '2025-10-01 12:30:00'),

      ('cf6f70f5-ec53-4512-890e-5ad3ef8c7a4d', '87e90d55-cb43-4b21-9dc4-6db248084aaf', 'c94d1f8e-22b5-4f12-bfc4-91b784e76a01',
       '2025-09-01', '2025-09-03', 1, 380000,  'CANCELLED', '2025-08-20 08:45:00');

-- ========================================
-- TABLA: review
-- ========================================

INSERT INTO `review` (
    `id`, `lodging_id`, `user_id`, `rating`, `comment`, `created_at`
) VALUES
      ('da7826c3-c2e9-45b0-8e73-fd0b08a998df', '6a05a77b-72ce-4f21-a681-2b523f70e3d3', 'c94d1f8e-22b5-4f12-bfc4-91b784e76a01',
       5, 'Excelente alojamiento, todo muy limpio y moderno.', '2025-10-16 09:00:00'),

      ('b1edb82b-5c56-4a7a-943b-3bc1da196d3a', '0d27b3a5-93e4-49a1-93a2-ff1c3c8f431e', 'f80d3b2a-ec84-4f60-a2b4-09f1c8f391a4',
       4, 'Muy buena ubicación, aunque el wifi era inestable.', '2025-11-14 18:20:00'),

      ('fd5b3f66-7819-4f17-bf33-544f21c7c1b8', '87e90d55-cb43-4b21-9dc4-6db248084aaf', '3e8a67b0-7a12-40a5-9e33-99c68ff8f9e2',
       3, 'Aceptable por el precio, pero el ruido era molesto.', '2025-09-05 20:45:00');
