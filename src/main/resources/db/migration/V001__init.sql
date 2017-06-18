CREATE TABLE expenses (
  id SERIAL PRIMARY KEY,
  date DATE NOT NULL,
  amount NUMERIC (12,2) NOT NULL,
  reason TEXT
);