CREATE TABLE borrowing_logs
(
    id                  SERIAL PRIMARY KEY NOT NULL,
    borrowing_record_id INT                NOT NULL,
    action              VARCHAR(50)        NOT NULL,
    change_time         TIMESTAMP          NOT NULL
);

ALTER TABLE borrowing_logs
    ALTER COLUMN change_time SET DEFAULT CURRENT_TIMESTAMP;

CREATE OR REPLACE FUNCTION log_borrowing_changes()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO borrowing_logs (borrowing_record_id, action, change_time)
    VALUES (NEW.id, TG_OP, CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER borrowing_changes_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON borrowing_records
    FOR EACH ROW
EXECUTE FUNCTION log_borrowing_changes();
