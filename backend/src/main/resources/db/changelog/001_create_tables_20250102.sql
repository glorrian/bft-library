CREATE TABLE authors
(
    id         SERIAL PRIMARY KEY NOT NULL,
    full_name  VARCHAR(255)       NOT NULL,
    pseudonym  VARCHAR(255),
    birth_date DATE               NOT NULL
);

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255)       NOT NULL UNIQUE
);

CREATE TABLE works
(
    id       SERIAL PRIMARY KEY NOT NULL,
    title    VARCHAR(255)       NOT NULL,
    genre_id INT                NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);

CREATE TABLE work_authors
(
    id        SERIAL PRIMARY KEY NOT NULL,
    work_id   INT                NOT NULL,
    author_id INT                NOT NULL,
    FOREIGN KEY (work_id) REFERENCES works (id),
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE books
(
    id               SERIAL PRIMARY KEY NOT NULL,
    title            VARCHAR(255)       NOT NULL,
    isbn             VARCHAR(13)        NOT NULL UNIQUE,
    publication_date DATE               NOT NULL
);

CREATE TABLE book_works
(
    id      SERIAL PRIMARY KEY NOT NULL,
    book_id INT                NOT NULL,
    work_id INT                NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (work_id) REFERENCES works (id)
);

CREATE TABLE readers
(
    id        SERIAL PRIMARY KEY NOT NULL,
    full_name VARCHAR(255)       NOT NULL,
    email     VARCHAR(255)       NOT NULL UNIQUE
);

CREATE TABLE library_books
(
    id      SERIAL PRIMARY KEY NOT NULL,
    book_id INT                NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE TABLE borrowing_records
(
    id              SERIAL PRIMARY KEY NOT NULL,
    reader_id       INT                NOT NULL,
    library_book_id INT                NOT NULL,
    borrow_date     DATE               NOT NULL,
    return_date     DATE, -- NULL means the book is not returned yet
    FOREIGN KEY (library_book_id) REFERENCES library_books (id),
    FOREIGN KEY (reader_id) REFERENCES readers (id)
);
