ALTER TABLE public.users
ADD COLUMN must_change_password BOOLEAN DEFAULT TRUE NOT NULL;
