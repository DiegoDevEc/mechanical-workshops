-- 1. Agregar las nuevas columnas en la tabla attendances
ALTER TABLE attendances ADD COLUMN received_by UUID NULL; -- Puede ser NULL
ALTER TABLE attendances ADD COLUMN technician_id UUID NOT NULL; -- No puede ser NULL

-- 2. Establecer las claves foráneas con la tabla persons
ALTER TABLE attendances ADD CONSTRAINT fk_attendances_received_by
FOREIGN KEY (received_by) REFERENCES persons(id);

ALTER TABLE attendances ADD CONSTRAINT fk_attendances_technician
FOREIGN KEY (technician_id) REFERENCES persons(id);
