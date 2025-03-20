-- 1. Permitir que start_date sea NULL
ALTER TABLE attendances ALTER COLUMN start_date DROP NOT NULL;
