CREATE TABLE reservations (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  timeslot_id UUID NOT NULL,
  reservation_date DATE NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP,

  CONSTRAINT fk_reservations_user
      FOREIGN KEY (user_id)
          REFERENCES users(id),

  CONSTRAINT fk_reservations_timeslot
      FOREIGN KEY (timeslot_id)
          REFERENCES timeslots(id)
);

CREATE INDEX idx_reservations_user_id
    ON reservations(user_id);

CREATE INDEX idx_reservations_timeslot_id
    ON reservations(timeslot_id);

CREATE INDEX idx_reservations_date
    ON reservations(reservation_date);

CREATE INDEX idx_reservations_slot_date_status
    ON reservations(timeslot_id, reservation_date, status);