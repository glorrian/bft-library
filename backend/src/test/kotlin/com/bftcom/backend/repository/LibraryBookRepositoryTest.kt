package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.LibraryBook
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class LibraryBookRepositoryTest {

	@Autowired
	private lateinit var libraryBookRepository: LibraryBookRepository

	@Autowired
	private lateinit var bookRepository: BookRepository

	private var libraryBookId: Long = 0

	companion object {
		private var libraryBook: LibraryBook? = null

		@Synchronized
		fun createLibraryBook(
			bookRepository: BookRepository,
			libraryBookRepository: LibraryBookRepository,
			title: String,
			isbn: String,
			publicationDate: LocalDate
		): LibraryBook {
			if (libraryBook == null) {
				val book = bookRepository.create(Book(title = title, isbn = isbn, publicationDate = publicationDate))
				libraryBook = libraryBookRepository.create(LibraryBook(bookId = book.id!!))
			}
			return libraryBook!!
		}

		fun createNewBook(
			bookRepository: BookRepository,
			title: String,
			isbn: String,
			publicationDate: LocalDate
		): Book {
			return bookRepository.create(Book(title = title, isbn = isbn, publicationDate = publicationDate))
		}
	}

	@BeforeEach
	fun setup() {
		libraryBookId = createLibraryBook(
			bookRepository,
			libraryBookRepository,
			"Test Book",
			"1234567890133",
			LocalDate.of(2020, 1, 1)
		).id!!
	}

	@Test
	fun givenLibraryBook_whenSave_thenCanBeFoundById() {
		val savedLibraryBook = libraryBookRepository.create(LibraryBook(bookId = libraryBookId))
		val foundLibraryBook = libraryBookRepository.findById(savedLibraryBook.id!!)
		assertNotNull(foundLibraryBook)
		assertEquals(libraryBookId, foundLibraryBook?.bookId)
	}

	@Test
	fun givenLibraryBook_whenDelete_thenCannotBeFoundById() {
		val savedLibraryBook = libraryBookRepository.create(LibraryBook(bookId = libraryBookId))
		libraryBookRepository.deleteById(savedLibraryBook.id!!)
		val foundLibraryBook = libraryBookRepository.findById(savedLibraryBook.id!!)
		assertNull(foundLibraryBook)
	}

	@Test
	fun givenLibraryBook_whenUpdate_thenUpdatedValuesCanBeFound() {
		val savedLibraryBook = libraryBookRepository.create(LibraryBook(bookId = libraryBookId))
		val newBook = createNewBook(bookRepository, "Test Book 2", "1234567890135", LocalDate.of(2020, 2, 1))
		val updatedLibraryBook = savedLibraryBook.copy(bookId = newBook.id!!)
		libraryBookRepository.update(updatedLibraryBook)
		val foundLibraryBook = libraryBookRepository.findById(savedLibraryBook.id!!)
		assertNotNull(foundLibraryBook)
		assertEquals(newBook.id, foundLibraryBook?.bookId)
	}

	@Test
	fun givenMultipleLibraryBooks_whenFindAll_thenAllLibraryBooksAreReturned() {
		val libraryBook1 = LibraryBook(bookId = libraryBookId)
		val newBook = createNewBook(bookRepository, "Test Book 2", "1234567890134", LocalDate.of(2020, 2, 1))
		val libraryBook2 = LibraryBook(bookId = newBook.id!!)
		libraryBookRepository.create(libraryBook1)
		libraryBookRepository.create(libraryBook2)
		val libraryBooks = libraryBookRepository.findAll()
		assertTrue(libraryBooks.any { it.bookId == libraryBookId })
		assertTrue(libraryBooks.any { it.bookId == newBook.id })
	}

	@Test
	fun givenLibraryBookWithInvalidBookId_whenSave_thenThrowsException() {
		val invalidLibraryBook = LibraryBook(bookId = -1L)
		val exception = assertThrows<IllegalArgumentException> {
			libraryBookRepository.create(invalidLibraryBook)
		}
		assertNotNull(exception)
	}
}