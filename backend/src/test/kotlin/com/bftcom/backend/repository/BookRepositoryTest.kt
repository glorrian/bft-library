package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun givenBook_whenSaveAndFindById_thenBookIsFound() {
        val book = Book(
            title = "Test Book",
            isbn = "1234567890",
            publicationDate = LocalDate.of(2020, 1, 1)
        )
        val id = bookRepository.save(book)
        val savedBook = bookRepository.findById(id)
        assertNotNull(savedBook)
        assertEquals("Test Book", savedBook?.title)
        assertEquals("1234567890", savedBook?.isbn)
        assertEquals(LocalDate.of(2020, 1, 1), savedBook?.publicationDate)
    }

    @Test
    fun givenBook_whenDelete_thenBookIsDeleted() {
        val book = Book(
            title = "Delete Book",
            isbn = "0987654321",
            publicationDate = LocalDate.of(2021, 2, 2)
        )
        val id = bookRepository.save(book)
        bookRepository.deleteById(id)
        assertNull(bookRepository.findById(id))
    }

    @Test
    fun givenBook_whenUpdateBook_thenBookIsUpdated() {
        val book = Book(
            title = "Original Title",
            isbn = "1111111111",
            publicationDate = LocalDate.of(2019, 3, 3)
        )
        val id = bookRepository.save(book)
        val updatedBook = book.copy(id = id, title = "Updated Title")
        bookRepository.save(updatedBook)
        val foundBook = bookRepository.findById(id)
        assertNotNull(foundBook)
        assertEquals("Updated Title", foundBook?.title)
    }

    @Test
    fun givenBooks_whenFindAll_thenAllBooksAreFound() {
        val book1 = Book(
            title = "Book One",
            isbn = "2222222222",
            publicationDate = LocalDate.of(2018, 4, 4)
        )
        val book2 = Book(
            title = "Book Two",
            isbn = "3333333333",
            publicationDate = LocalDate.of(2017, 5, 5)
        )
        bookRepository.save(book1)
        bookRepository.save(book2)
        val books = bookRepository.findAll()
        assertTrue(books.any { it.title == "Book One" })
        assertTrue(books.any { it.title == "Book Two" })
    }

    @Test
    fun givenBook_whenExistsById_thenBookExists() {
        val book = Book(
            title = "Existence Check",
            isbn = "4444444444",
            publicationDate = LocalDate.of(2016, 6, 6)
        )
        val id = bookRepository.save(book)
        assertTrue(bookRepository.existsById(id))
        bookRepository.deleteById(id)
        assertFalse(bookRepository.existsById(id))
    }

    @Test
    fun givenBookWithoutIsbn_whenSave_thenBookIsSaved() {
        val book = Book(
            title = "No ISBN",
            isbn = "",
            publicationDate = LocalDate.of(2015, 7, 7)
        )
        val id = bookRepository.save(book)
        val savedBook = bookRepository.findById(id)
        assertNotNull(savedBook)
        assertEquals("No ISBN", savedBook?.title)
        assertEquals("", savedBook?.isbn)
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { bookRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenBookIsNotFound() {
        val nonExistentId = 9999L
        val book = bookRepository.findById(nonExistentId)
        assertNull(book)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenBookDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(bookRepository.existsById(nonExistentId))
    }
}