ALTER TABLE books
    ADD CONSTRAINT chk_isbn_format CHECK (isbn ~ '^[0-9]{13}$');
