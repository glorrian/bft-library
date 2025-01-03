package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.LibraryBook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class LibraryBookRepositoryTest {

    @Autowired
    private lateinit var libraryBookRepository: LibraryBookRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun givenLibraryBook_whenSaveAndFindById_thenLibraryBookIsFound() {
        val book = Book(title = "Test Book", isbn = "1", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId)
        val id = libraryBookRepository.save(libraryBook)
        val savedLibraryBook = libraryBookRepository.findById(id)
        assertNotNull(savedLibraryBook)
        assertEquals(bookId.toInt(), savedLibraryBook?.bookId)
    }

    @Test
    fun givenLibraryBook_whenDelete_thenLibraryBookIsDeleted() {
		val book = Book(title = "Test Book", isbn = "2", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId)
        val id = libraryBookRepository.save(libraryBook)
        libraryBookRepository.deleteById(id)
        assertNull(libraryBookRepository.findById(id))
    }

    @Test
    fun givenLibraryBook_whenUpdateLibraryBook_thenLibraryBookIsUpdated() {
		val book = Book(title = "Test Book", isbn = "3", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId)
        val id = libraryBookRepository.save(libraryBook)
		val updatedBook = book.copy(id = bookId, title = "Updated Test Book")
        val updatedBookId = bookRepository.save(updatedBook)
        val updatedLibraryBook = libraryBook.copy(id = id, bookId = updatedBookId)
        libraryBookRepository.save(updatedLibraryBook)
        val foundLibraryBook = libraryBookRepository.findById(id)
        assertNotNull(foundLibraryBook)
        assertEquals(updatedBookId.toInt(), foundLibraryBook?.bookId)
    }

    @Test
    fun givenLibraryBooks_whenFindAll_thenAllLibraryBooksAreFound() {
		val book1 = Book(title = "Test Book 1", isbn = "4", publicationDate = LocalDate.now())
		val book2 = Book(title = "Test Book 2", isbn = "5", publicationDate = LocalDate.now())
        val bookId1 = bookRepository.save(book1)
        val bookId2 = bookRepository.save(book2)
        val libraryBook1 = LibraryBook(bookId = bookId1)
        val libraryBook2 = LibraryBook(bookId = bookId2)
        libraryBookRepository.save(libraryBook1)
        libraryBookRepository.save(libraryBook2)
        val libraryBooks = libraryBookRepository.findAll()
        assertTrue(libraryBooks.any { it.bookId == bookId1 })
        assertTrue(libraryBooks.any { it.bookId == bookId2 })
    }

    @Test
    fun givenLibraryBook_whenExistsById_thenLibraryBookExists() {
		val book = Book(title = "Test Book", isbn = "6", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId)
        val id = libraryBookRepository.save(libraryBook)
        assertTrue(libraryBookRepository.existsById(id))
        libraryBookRepository.deleteById(id)
        assertFalse(libraryBookRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { libraryBookRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenLibraryBookIsNotFound() {
        val nonExistentId = 9999L
        val libraryBook = libraryBookRepository.findById(nonExistentId)
        assertNull(libraryBook)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenLibraryBookDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(libraryBookRepository.existsById(nonExistentId))
    }
}