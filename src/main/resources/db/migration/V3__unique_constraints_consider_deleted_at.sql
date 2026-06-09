ALTER TABLE users DROP CONSTRAINT IF EXISTS users_email_key;

CREATE UNIQUE INDEX uk_users_email_active
    ON users (email)
    WHERE deleted_at IS NULL;

ALTER TABLE calendars DROP CONSTRAINT IF EXISTS calendars_name_key;

CREATE UNIQUE INDEX uk_calendars_name_active
    ON calendars (name)
    WHERE deleted_at IS NULL;
