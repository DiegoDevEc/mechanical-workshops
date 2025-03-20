-- 1. Eliminar las restricciones CHECK existentes
ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_status_check;
ALTER TABLE attendances DROP CONSTRAINT IF EXISTS attendances_status_check;

-- 2. Asegurar que todos los registros en attendances y appointments cumplen con la nueva restricción
UPDATE attendances
SET status = 'ASSIGN'
WHERE status NOT IN ('ASSIGN', 'PROGRESS', 'NOTIFIED', 'FINISH');

UPDATE appointments
SET status = 'INGRESS'
WHERE status NOT IN ('INGRESS', 'PROGRESS', 'FINISH');

-- 3. Agregar la nueva restricción CHECK con los valores permitidos
ALTER TABLE attendances ADD CONSTRAINT attendances_status_check
CHECK (status IN ('ASSIGN', 'PROGRESS', 'NOTIFIED', 'FINISH'));

ALTER TABLE appointments ADD CONSTRAINT appointments_status_check
CHECK (status IN ('INGRESS', 'PROGRESS', 'FINISH'));
