INSERT INTO authors (full_name, pseudonym, birth_date)
VALUES ('Лев Николаевич Толстой', NULL, '1828-09-09'),
       ('Фёдор Михайлович Достоевский', NULL, '1821-11-11'),
       ('Александр Сергеевич Пушкин', NULL, '1799-06-06'),
       ('Антон Павлович Чехов', NULL, '1860-01-29'),
       ('Михаил Булгаков', NULL, '1891-05-15');

INSERT INTO genres (name)
VALUES ('Роман'),
       ('Поэма'),
       ('Повесть'),
       ('Драма'),
       ('Повесть');

INSERT INTO works (title, genre_id)
VALUES ('Война и мир', 1),
       ('Преступление и наказание', 1),
       ('Евгений Онегин', 2),
       ('Чайка', 4),
       ('Мастер и Маргарита', 1);

-- Война и мир — Толстой (автор id = 1, work id = 1)
INSERT INTO work_authors (work_id, author_id)
VALUES (1, 1);
-- Преступление и наказание — Достоевский (автор id = 2, work id = 2)
INSERT INTO work_authors (work_id, author_id)
VALUES (2, 2);
-- Евгений Онегин — Пушкин (автор id = 3, work id = 3)
INSERT INTO work_authors (work_id, author_id)
VALUES (3, 3);
-- Чайка — Чехов (автор id = 4, work id = 4)
INSERT INTO work_authors (work_id, author_id)
VALUES (4, 4);
-- Мастер и Маргарита — Булгаков (автор id = 5, work id = 5)
INSERT INTO work_authors (work_id, author_id)
VALUES (5, 5);

INSERT INTO books (title, isbn, publication_date)
VALUES ('Война и мир (т.1)', '9783161484100', '2005-05-01'),
       ('Преступление и наказание', '9783161484101', '2003-03-15'),
       ('Евгений Онегин', '9783161484102', '2010-07-21'),
       ('Чайка', '9783161484103', '2015-11-11'),
       ('Мастер и Маргарита', '9783161484104', '1990-12-25');

-- Привязка книги "Война и мир (т.1)" к произведению "Война и мир" (book id = 1, work id = 1)
INSERT INTO book_works (book_id, work_id)
VALUES (1, 1);
-- Привязка книги "Преступление и наказание" к произведению (book id = 2, work id = 2)
INSERT INTO book_works (book_id, work_id)
VALUES (2, 2);
-- Привязка книги "Евгений Онегин" (book id = 3, work id = 3)
INSERT INTO book_works (book_id, work_id)
VALUES (3, 3);
-- Привязка книги "Чайка" (book id = 4, work id = 4)
INSERT INTO book_works (book_id, work_id)
VALUES (4, 4);
-- Привязка книги "Мастер и Маргарита" (book id = 5, work id = 5)
INSERT INTO book_works (book_id, work_id)
VALUES (5, 5);

INSERT INTO readers (full_name, email)
VALUES ('Анна Иванова', 'anna.ivanova@example.com'),
       ('Пётр Петров', 'petr.petrov@example.com'),
       ('Светлана Смирнова', 'svetlana.smirnova@example.com');

INSERT INTO library_books (book_id)
VALUES (1), -- Книга "Война и мир (т.1)"
       (2), -- "Преступление и наказание"
       (3), -- "Евгений Онегин"
       (4), -- "Чайка"
       (5); -- "Мастер и Маргарита"

INSERT INTO borrowing_records (reader_id, library_book_id, borrow_date, return_date)
VALUES (1, 1, '2023-07-01', NULL),         -- Анна Иванова взяла "Война и мир (т.1)" 1 июля 2023, не вернулась
       (2, 2, '2023-06-15', NULL), -- Пётр Петров взял и вернул "Преступление и наказание"
       (3, 3, '2023-07-20', NULL); -- Светлана Смирнова взяла "Евгений Онегин", не вернула

-- Обновление записи для установки даты возврата
UPDATE borrowing_records
SET return_date = '2023-07-15'
WHERE id = 2;
