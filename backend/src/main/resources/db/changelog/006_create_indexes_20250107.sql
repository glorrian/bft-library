CREATE INDEX idx_work_authors_work_id ON work_authors (work_id);
CREATE INDEX idx_work_authors_author_id ON work_authors (author_id);

CREATE INDEX idx_book_works_book_id ON book_works (book_id);
CREATE INDEX idx_book_works_work_id ON book_works (work_id);

CREATE INDEX idx_library_books_book_id ON library_books (book_id);

CREATE INDEX idx_borrowing_records_reader_id ON borrowing_records (reader_id);

CREATE INDEX idx_borrowing_records_library_book_id ON borrowing_records (library_book_id);

CREATE INDEX idx_authors_full_name ON authors (full_name);

CREATE INDEX idx_books_title ON books (title);

CREATE INDEX idx_borrowing_records_borrow_date ON borrowing_records (borrow_date);
CREATE INDEX idx_borrowing_records_return_date ON borrowing_records (return_date);
CREATE INDEX idx_borrowing_records_borrow_return_date ON borrowing_records (return_date, borrow_date);
