CREATE TABLE timeslots (
   id UUID PRIMARY KEY,
   calendar_id UUID NOT NULL,
   start_time TIMESTAMP NOT NULL,
   end_time TIMESTAMP NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP,

   CONSTRAINT fk_timeslots_calendar
       FOREIGN KEY (calendar_id)
           REFERENCES calendars(id)
);