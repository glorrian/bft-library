// src/test/kotlin/com/bftcom/backend/repository/BorrowingRecordRepositoryTest.kt
package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.entity.Reader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
class BorrowingRecordRepositoryTest {

	@Autowired
	private lateinit var borrowingRecordRepository: BorrowingRecordRepository

	@Autowired
	private lateinit var libraryBookRepository: LibraryBookRepository

	@Autowired
	private lateinit var bookRepository: BookRepository

	@Autowired
	private lateinit var readerRepository: ReaderRepository

	private var libraryBookId: Long = 0
	private var readerId: Long = 0

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

		fun createReader(readerRepository: ReaderRepository, fullName: String, email: String): Reader {
			return readerRepository.create(Reader(fullName = fullName, email = email))
		}
	}

	@BeforeEach
	fun setup() {
		libraryBookId = createLibraryBook(bookRepository, libraryBookRepository, "Test Book", "1234567890123", LocalDate.of(2020, 1, 1)).id!!
		readerId = createUniqueReader("John Doe").id!!
	}

	/**
	 * Создание читателя с уникальным email
	 */
	private fun createUniqueReader(fullName: String): Reader {
		val uniqueEmail = "user-${UUID.randomUUID()}@example.com"
		return createReader(readerRepository, fullName, uniqueEmail)
	}

	@Test
	fun givenBorrowingRecord_whenSave_thenCanBeFoundById() {
		val borrowingRecord = BorrowingRecord(
			libraryBookId = libraryBookId, readerId = readerId, borrowDate = LocalDate.of(2023, 1, 1), returnDate = LocalDate.of(2023, 1, 10)
		)
		val savedRecord = borrowingRecordRepository.create(borrowingRecord)
		val foundRecord = borrowingRecordRepository.findById(savedRecord.id!!)
		assertNotNull(foundRecord)
		assertEquals(libraryBookId, foundRecord?.libraryBookId)
		assertEquals(readerId, foundRecord?.readerId)
	}

	@Test
	fun givenBorrowingRecord_whenDelete_thenCannotBeFoundById() {
		val borrowingRecord = BorrowingRecord(
			libraryBookId = libraryBookId, readerId = readerId, borrowDate = LocalDate.of(2023, 2, 1), returnDate = LocalDate.of(2023, 2, 10)
		)
		val savedRecord = borrowingRecordRepository.create(borrowingRecord)
		borrowingRecordRepository.deleteById(savedRecord.id!!)
		val foundRecord = borrowingRecordRepository.findById(savedRecord.id!!)
		assertNull(foundRecord)
	}

	@Test
	fun givenBorrowingRecord_whenUpdate_thenUpdatedValuesCanBeFound() {
		val borrowingRecord = BorrowingRecord(
			libraryBookId = libraryBookId, readerId = readerId, borrowDate = LocalDate.of(2023, 3, 1), returnDate = LocalDate.of(2023, 3, 10)
		)
		val savedRecord = borrowingRecordRepository.create(borrowingRecord)
		val newBook = createNewBook(bookRepository, "Test Book 2", "1234567890125", LocalDate.of(2020, 2, 1))
		val newLibraryBook = libraryBookRepository.create(LibraryBook(bookId = newBook.id!!))
		val updatedRecord = savedRecord.copy(libraryBookId = newLibraryBook.id!!)
		borrowingRecordRepository.update(updatedRecord)
		val foundRecord = borrowingRecordRepository.findById(savedRecord.id!!)
		assertNotNull(foundRecord)
		assertEquals(newLibraryBook.id, foundRecord?.libraryBookId)
	}

	@Test
	fun givenMultipleBorrowingRecords_whenFindAll_thenAllRecordsAreReturned() {
		val record1 = BorrowingRecord(
			libraryBookId = libraryBookId, readerId = readerId, borrowDate = LocalDate.of(2023, 4, 1), returnDate = LocalDate.of(2023, 4, 10)
		)
		val newBook = createNewBook(bookRepository, "Test Book 2", "1234567890124", LocalDate.of(2020, 2, 1))
		val newLibraryBook = libraryBookRepository.create(LibraryBook(bookId = newBook.id!!))
		val newReader = createUniqueReader("Jane Doe")
		val record2 = BorrowingRecord(
			libraryBookId = newLibraryBook.id!!, readerId = newReader.id!!, borrowDate = LocalDate.of(2023, 5, 1), returnDate = LocalDate.of(2023, 5, 10)
		)
		borrowingRecordRepository.create(record1)
		borrowingRecordRepository.create(record2)
		val records = borrowingRecordRepository.findAll()
		assertTrue(records.any { it.libraryBookId == libraryBookId })
		assertTrue(records.any { it.libraryBookId == newLibraryBook.id })
	}

	@Test
	fun givenBorrowingRecordWithInvalidLibraryBookId_whenSave_thenThrowsException() {
		val borrowingRecord = BorrowingRecord(
			libraryBookId = -1L, readerId = readerId, borrowDate = LocalDate.of(2023, 6, 1), returnDate = LocalDate.of(2023, 6, 10)
		)
		val exception = assertThrows<Exception> {
			borrowingRecordRepository.create(borrowingRecord)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenBorrowingRecordWithInvalidDates_whenSave_thenThrowsException() {
		val borrowingRecord = BorrowingRecord(
			libraryBookId = libraryBookId, readerId = readerId, borrowDate = LocalDate.of(2023, 7, 10), returnDate = LocalDate.of(2023, 7, 1)
		)
		val exception = assertThrows<Exception> {
			borrowingRecordRepository.create(borrowingRecord)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenDuplicateEmail_whenCreateReader_thenThrowsException() {
		// Arrange
		val fullName = "Duplicate Email User"
		val email = "duplicate@example.com"
		createReader(readerRepository, fullName, email)

		// Act & Assert
		val exception = assertThrows<Exception> {
			createReader(readerRepository, "Another User", email)
		}
		assertNotNull(exception)
	}
}
