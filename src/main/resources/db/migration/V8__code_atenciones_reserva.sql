ALTER TABLE available_appointments ADD code TEXT NOT NULL;
ALTER TABLE attendances ADD code TEXT NOT NULL;
ALTER TABLE public.available_appointments DROP CONSTRAINT available_appointments_day_of_week_check;
