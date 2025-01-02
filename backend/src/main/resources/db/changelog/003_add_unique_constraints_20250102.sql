ALTER TABLE work_authors
    ADD CONSTRAINT unique_work_author UNIQUE (work_id, author_id);

ALTER TABLE book_works
    ADD CONSTRAINT unique_book_work UNIQUE (book_id, work_id);
