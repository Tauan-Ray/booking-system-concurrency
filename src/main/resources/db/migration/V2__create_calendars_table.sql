CREATE TABLE calendars (
   id UUID PRIMARY KEY,
   name VARCHAR(255) NOT NULL UNIQUE,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP
);