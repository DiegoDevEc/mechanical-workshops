-- Enable extension for UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla personas
CREATE TABLE persons (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  firstname TEXT NOT NULL,
  lastname TEXT NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO')),
  address TEXT
);

-- Crear tabla vehiculos
CREATE TABLE vehicles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id UUID REFERENCES persons (id),
  brand TEXT NOT NULL,
  model TEXT NOT NULL,
  "year" INT NOT NULL,
  plate TEXT NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

CREATE TABLE available_appointments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  date_available DATE NOT NULL,  -- Fecha de disponibilidad (solo el día, sin la hora)
  time_available TIME NOT NULL,  -- Hora de disponibilidad (inicio del turno)
  duration TIME DEFAULT '01:00:00' NOT NULL,  -- Duración del turno (1 hora por cita)
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA')),  -- Estado de la disponibilidad
  day_of_week TEXT NOT NULL CHECK (day_of_week IN ('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'))  -- Día de la semana
);

-- Crear tabla citas
CREATE TABLE appointments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id UUID REFERENCES persons (id),
  vehicle_id UUID REFERENCES vehicles (id),
  date_appointment TIMESTAMP WITH TIME ZONE NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ING', 'PRO', 'FIN')),
  available_appointment_id UUID,
  CONSTRAINT fk_available_appointment
    FOREIGN KEY (available_appointment_id)
    REFERENCES available_appointments(id)
    ON DELETE CASCADE
);

-- Crear tabla servicios
CREATE TABLE services (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  description TEXT,
  cost NUMERIC(10, 2) NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

-- Crear tabla atenciones
CREATE TABLE attendances (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  appointment_id UUID REFERENCES appointments (id),
  service_id UUID REFERENCES services (id),
  start_date TIMESTAMP WITH TIME ZONE NOT NULL,
  end_date TIMESTAMP WITH TIME ZONE,
  comments TEXT,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

-- Crear tabla productos
CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  description TEXT,
  price NUMERIC(10, 2) NOT NULL,
  stock INT NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

-- Crear tabla ventas
CREATE TABLE sales (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id UUID REFERENCES persons (id),
  date TIMESTAMP WITH TIME ZONE NOT NULL,
  total NUMERIC(10, 2) NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

-- Crear tabla detalle_ventas
CREATE TABLE sales_details (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sale_id UUID REFERENCES sales (id),
  product_id UUID REFERENCES products (id),
  quantity INT NOT NULL,
  subtotal NUMERIC(10, 2) NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO'))
);

--- Crear tabla usuarios
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  username TEXT NOT NULL UNIQUE,
  identification TEXT NOT NULL UNIQUE,
  phone TEXT NOT NULL UNIQUE,
  email TEXT NOT NULL UNIQUE,
  "password" TEXT NOT NULL,
  role TEXT NOT NULL CHECK (role IN ('CLIENT', 'TECHNICIAN', 'ADMINISTRATOR')),
  status TEXT NOT NULL CHECK (status IN ('ACT', 'INA', 'BLO')),
  person_id UUID REFERENCES persons (id)
);

-- Habilitar la extensión para UUID si no está activada
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla de auditoría
CREATE TABLE audit_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    table_name TEXT NOT NULL,          -- Nombre de la tabla afectada
    operation TEXT NOT NULL CHECK (operation IN ('INSERT', 'UPDATE', 'DELETE')),  -- Tipo de operación
    record_id UUID NOT NULL,            -- ID del registro afectado
    old_data TEXT,                      -- Datos antes del cambio (como JSON)
    new_data TEXT,                      -- Datos después del cambio (como JSON)
    changed_by TEXT,                      -- Usuario que realizó el cambio
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()  -- Fecha y hora del cambio
);

-- Crear índice para mejorar búsquedas en la auditoría
CREATE INDEX idx_audit_log_table ON audit_log (table_name);
CREATE INDEX idx_audit_log_operation ON audit_log (operation);
CREATE INDEX idx_audit_log_record ON audit_log (record_id);
CREATE INDEX idx_audit_log_changed_at ON audit_log (changed_at);


INSERT INTO persons (id, firstname, lastname, status, address) VALUES
('a4f08de7-192a-461d-94c2-84477d74dcc4', 'Diego', 'Acosta', 'ACT', 'Quito, Ecuador'),
('3b3b5675-45d3-4bd4-b9a9-4cc0befbaa2f', 'Diego', 'Acosta', 'ACT', 'Cumbaya'),
('90792107-6a12-4412-be79-edc8a15179f3', 'Diego', 'Acosta', 'ACT', 'Cumbaya'),
('560311c8-bb91-4771-90c4-f33c5c5b09f9', 'Jose', 'Peres', 'ACT', 'Quito');

INSERT INTO users (id, username, identification, phone, email, password, role, status, person_id) VALUES
('f1a80ca0-8d24-41f9-9470-c0a0c4e74e78', 'dacosta22', '1717744313', '0998785850', 'email2@email.com', '$2a$10$o79hhI8pu5tOIu4W9z78fu0LWYdjSwX99wOAxTg07uKkSYS5vX4rC', 'ADMINISTRATOR', 'ACT', 'a4f08de7-192a-461d-94c2-84477d74dcc4'),
('b9d35b61-0b07-429f-902b-3f1fcb27e389', 'prueba1', '1717744311', '0999999990', 'muestra@example043.com', '$2a$10$M7vSAEDw83SCvDgzLOGSFerOWM40urqHFKS1ZdMBoMKvYTK0eYuVS', 'CLIENT', 'ACT', '3b3b5675-45d3-4bd4-b9a9-4cc0befbaa2f'),
('fbbed76f-1e1d-4a1f-a75f-54316b4e5637', 'prueba2', '1717744312', '0998348309', 'muestra@email.com', '$2a$10$hcbr9P.HJEjRYOT2msO7IuPuK0dJl.5VHJq92/pNGFfwqYXf8tIYW', 'TECHNICIAN', 'ACT', '90792107-6a12-4412-be79-edc8a15179f3'),
('5e5a32c9-d9f0-41eb-82b3-4b5ae192dc6f', 'prueba12', '1717744310', '0988888888', 'email@email.com', '$2a$10$sis9cg4WsgyIE4NwCNVYd.o7jjOKvWvOMq3jsRLA9wFjaUfiLA5LS', 'TECHNICIAN', 'ACT', '560311c8-bb91-4771-90c4-f33c5c5b09f9');


