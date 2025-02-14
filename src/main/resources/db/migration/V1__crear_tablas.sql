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
  plate TEXT NOT NULL
);

-- Crear tabla citas
CREATE TABLE appointments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id UUID REFERENCES persons (id),
  vehicle_id UUID REFERENCES vehicles (id),
  date_appointment TIMESTAMP WITH TIME ZONE NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('ING', 'PRO', 'FIN'))
);

-- Crear tabla servicios
CREATE TABLE services (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  description TEXT,
  cost NUMERIC(10, 2) NOT NULL
);

-- Crear tabla atenciones
CREATE TABLE attendances (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  appointment_id UUID REFERENCES appointments (id),
  service_id UUID REFERENCES services (id),
  start_date TIMESTAMP WITH TIME ZONE NOT NULL,
  end_date TIMESTAMP WITH TIME ZONE,
  comments TEXT
);

-- Crear tabla productos
CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  description TEXT,
  price NUMERIC(10, 2) NOT NULL,
  stock INT NOT NULL
);

-- Crear tabla ventas
CREATE TABLE sales (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  client_id UUID REFERENCES persons (id),
  date TIMESTAMP WITH TIME ZONE NOT NULL,
  total NUMERIC(10, 2) NOT NULL
);

-- Crear tabla detalle_ventas
CREATE TABLE sales_details (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sale_id UUID REFERENCES sales (id),
  product_id UUID REFERENCES products (id),
  quantity INT NOT NULL,
  subtotal NUMERIC(10, 2) NOT NULL
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

