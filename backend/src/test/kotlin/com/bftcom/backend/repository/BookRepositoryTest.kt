package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class BookRepositoryTest {

	@Autowired
	private lateinit var bookRepository: BookRepository

	@Test
	fun givenBook_whenSave_thenCanBeFoundById() {
		val book = Book(title = "Test Book", isbn = "1234567890123", publicationDate = LocalDate.now())
		val savedBook = bookRepository.create(book)
		val foundBook = bookRepository.findById(savedBook.id!!)
		assertNotNull(foundBook)
		assertEquals("Test Book", foundBook?.title)
	}

	@Test
	fun givenBook_whenDelete_thenCannotBeFoundById() {
		val book = Book(title = "Test Book", isbn = "1234567890124", publicationDate = LocalDate.now())
		val savedBook = bookRepository.create(book)
		bookRepository.deleteById(savedBook.id!!)
		val foundBook = bookRepository.findById(savedBook.id!!)
		assertNull(foundBook)
	}

	@Test
	fun givenBook_whenUpdate_thenUpdatedValuesCanBeFound() {
		val book = Book(title = "Test Book", isbn = "1234567890125", publicationDate = LocalDate.now())
		val savedBook = bookRepository.create(book)
		val updatedBook = savedBook.copy(title = "Updated Book")
		bookRepository.update(updatedBook)
		val foundBook = bookRepository.findById(savedBook.id!!)
		assertNotNull(foundBook)
		assertEquals("Updated Book", foundBook?.title)
	}

	@Test
	fun givenMultipleBooks_whenFindAll_thenAllBooksAreReturned() {
		val book1 = Book(title = "Book One", isbn = "1234567890126", publicationDate = LocalDate.now())
		val book2 = Book(title = "Book Two", isbn = "1234567890127", publicationDate = LocalDate.now())
		bookRepository.create(book1)
		bookRepository.create(book2)
		val books = bookRepository.findAll()
		assertTrue(books.any { it.title == "Book One" })
		assertTrue(books.any { it.title == "Book Two" })
	}

	@Test
	fun givenBookWithInvalidTitle_whenSave_thenThrowsException() {
		val invalidBook = Book(title = "", isbn = "1234567890128", publicationDate = LocalDate.now())
		val exception = assertThrows<IllegalArgumentException> {
			bookRepository.create(invalidBook)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenBookWithInvalidIsbn_whenSave_thenThrowsException() {
		val invalidBook = Book(title = "Test Book", isbn = "invalid_isbn", publicationDate = LocalDate.now())
		val exception = assertThrows<IllegalArgumentException> {
			bookRepository.create(invalidBook)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenDuplicateIsbn_whenSave_thenThrowsException() {
		val book1 = Book(title = "Test Book 1", isbn = "1234567890129", publicationDate = LocalDate.now())
		val book2 = Book(title = "Test Book 2", isbn = "1234567890129", publicationDate = LocalDate.now())
		bookRepository.create(book1)
		val exception = assertThrows<Exception> {
			bookRepository.create(book2)
		}
		assertNotNull(exception)
	}
}