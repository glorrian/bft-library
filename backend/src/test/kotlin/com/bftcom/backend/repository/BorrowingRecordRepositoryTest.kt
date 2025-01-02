package com.bftcom.backend.repository

import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class BorrowingRecordRepositoryTest {

    @Autowired
    private lateinit var borrowingRecordRepository: BorrowingRecordRepository

    @Autowired
    private lateinit var libraryBookRepository: LibraryBookRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun givenBorrowingRecord_whenSaveAndFindById_thenBorrowingRecordIsFound() {
        val book = Book(title = "Test Book", isbn = "1", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId.toInt())
        val libraryBookId = libraryBookRepository.save(libraryBook)
        val borrowingRecord = BorrowingRecord(libraryBookId = libraryBookId, borrowDate = LocalDate.now())
        val id = borrowingRecordRepository.save(borrowingRecord)
        val savedBorrowingRecord = borrowingRecordRepository.findById(id)
        assertNotNull(savedBorrowingRecord)
        assertEquals(libraryBookId, savedBorrowingRecord?.libraryBookId)
        assertEquals(LocalDate.now(), savedBorrowingRecord?.borrowDate)
    }

    @Test
    fun givenBorrowingRecord_whenDelete_thenBorrowingRecordIsDeleted() {
        val book = Book(title = "Test Book", isbn = "2", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId.toInt())
        val libraryBookId = libraryBookRepository.save(libraryBook)
        val borrowingRecord = BorrowingRecord(libraryBookId = libraryBookId, borrowDate = LocalDate.now())
        val id = borrowingRecordRepository.save(borrowingRecord)
        borrowingRecordRepository.deleteById(id)
        assertNull(borrowingRecordRepository.findById(id))
    }

    @Test
    fun givenBorrowingRecord_whenUpdateBorrowingRecord_thenBorrowingRecordIsUpdated() {
        val book = Book(title = "Test Book", isbn = "3", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId.toInt())
        val libraryBookId = libraryBookRepository.save(libraryBook)
        val borrowingRecord = BorrowingRecord(libraryBookId = libraryBookId, borrowDate = LocalDate.now())
        val id = borrowingRecordRepository.save(borrowingRecord)
        val updatedBorrowingRecord = borrowingRecord.copy(id = id, borrowDate = LocalDate.now().plusDays(1))
        borrowingRecordRepository.save(updatedBorrowingRecord)
        val foundBorrowingRecord = borrowingRecordRepository.findById(id)
        assertNotNull(foundBorrowingRecord)
        assertEquals(LocalDate.now().plusDays(1), foundBorrowingRecord?.borrowDate)
    }

    @Test
    fun givenBorrowingRecords_whenFindAll_thenAllBorrowingRecordsAreFound() {
        val book1 = Book(title = "Test Book 1", isbn = "4", publicationDate = LocalDate.now())
        val book2 = Book(title = "Test Book 2", isbn = "5", publicationDate = LocalDate.now())
        val bookId1 = bookRepository.save(book1)
        val bookId2 = bookRepository.save(book2)
        val libraryBook1 = LibraryBook(bookId = bookId1.toInt())
        val libraryBook2 = LibraryBook(bookId = bookId2.toInt())
        val libraryBookId1 = libraryBookRepository.save(libraryBook1)
        val libraryBookId2 = libraryBookRepository.save(libraryBook2)
        val borrowingRecord1 = BorrowingRecord(libraryBookId = libraryBookId1, borrowDate = LocalDate.now())
        val borrowingRecord2 = BorrowingRecord(libraryBookId = libraryBookId2, borrowDate = LocalDate.now())
        borrowingRecordRepository.save(borrowingRecord1)
        borrowingRecordRepository.save(borrowingRecord2)
        val borrowingRecords = borrowingRecordRepository.findAll()
        assertTrue(borrowingRecords.any { it.libraryBookId == libraryBookId1 })
        assertTrue(borrowingRecords.any { it.libraryBookId == libraryBookId2 })
    }

    @Test
    fun givenBorrowingRecord_whenExistsById_thenBorrowingRecordExists() {
        val book = Book(title = "Test Book", isbn = "6", publicationDate = LocalDate.now())
        val bookId = bookRepository.save(book)
        val libraryBook = LibraryBook(bookId = bookId.toInt())
        val libraryBookId = libraryBookRepository.save(libraryBook)
        val borrowingRecord = BorrowingRecord(libraryBookId = libraryBookId, borrowDate = LocalDate.now())
        val id = borrowingRecordRepository.save(borrowingRecord)
        assertTrue(borrowingRecordRepository.existsById(id))
        borrowingRecordRepository.deleteById(id)
        assertFalse(borrowingRecordRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { borrowingRecordRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenBorrowingRecordIsNotFound() {
        val nonExistentId = 9999L
        val borrowingRecord = borrowingRecordRepository.findById(nonExistentId)
        assertNull(borrowingRecord)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenBorrowingRecordDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(borrowingRecordRepository.existsById(nonExistentId))
    }
}