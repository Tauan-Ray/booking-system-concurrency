CREATE UNIQUE INDEX uk_reservations_slot_date_confirmed
    ON reservations (timeslot_id, reservation_date)
    WHERE status = 'CONFIRMED';
