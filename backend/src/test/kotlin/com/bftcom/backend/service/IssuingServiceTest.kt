package com.bftcom.backend.service

import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.LibraryBookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class IssuingServiceTest {

    @Mock
    lateinit var libraryBookRepository: LibraryBookRepository

    @Mock
    lateinit var borrowingRecordRepository: BorrowingRecordRepository

    @InjectMocks
    lateinit var issuingService: IssuingService

    private val now = LocalDate.of(2023, 12, 1)

    @Test
    fun givenLibraryBookWhenIssueBookThenBorrowingRecordIsCreated() {
        val fakeLibraryBook = LibraryBook(id = 100, bookId = 42)
        given(libraryBookRepository.findById(100L)).willReturn(fakeLibraryBook)
        given(borrowingRecordRepository.findAll()).willReturn(emptyList())

        val recordToCreate = BorrowingRecord(null, 100L, now, null)
        val createdRecord = recordToCreate.copy(id = 999)

        given(borrowingRecordRepository.create(any())).willReturn(createdRecord)

        val result = issuingService.issueBook(100L, now)

        assertNotNull(result.id)
        assertEquals(999, result.id)
        assertEquals(100L, result.libraryBookId)
        assertEquals(now, result.borrowDate)
        assertNull(result.returnDate)

        verify(libraryBookRepository, times(1)).findById(100L)
        verify(borrowingRecordRepository, times(1)).findAll()
        verify(borrowingRecordRepository, times(1)).create(any())
    }

    @Test
    fun givenLibraryBookNotFoundWhenIssueBookThenThrowException() {
        given(libraryBookRepository.findById(anyLong())).willReturn(null)

        val ex = assertThrows<IllegalStateException> {
            issuingService.issueBook(200L, now)
        }
        assertEquals("LibraryBook not found", ex.message)

        verify(libraryBookRepository, times(1)).findById(200L)
        verify(borrowingRecordRepository, never()).findAll()
        verify(borrowingRecordRepository, never()).create(any())
    }

    @Test
    fun givenBookAlreadyIssuedWhenIssueBookThenThrowException() {
        val fakeLibraryBook = LibraryBook(id = 300, bookId = 42)
        given(libraryBookRepository.findById(300L)).willReturn(fakeLibraryBook)

        val existingRecord = BorrowingRecord(10L, 300L, now.minusDays(2), null)
        given(borrowingRecordRepository.findAll()).willReturn(listOf(existingRecord))

        val ex = assertThrows<IllegalStateException> {
            issuingService.issueBook(300L, now)
        }
        assertEquals("This book is already issued!", ex.message)

        verify(libraryBookRepository, times(1)).findById(300L)
        verify(borrowingRecordRepository, times(1)).findAll()
        verify(borrowingRecordRepository, never()).create(any())
    }

    @Test
    fun givenBorrowingRecordWhenReturnBookThenUpdateBorrowingRecord() {
        val existing = BorrowingRecord(id = 999, libraryBookId = 400, borrowDate = now.minusDays(5), returnDate = null)
        given(borrowingRecordRepository.findById(999L)).willReturn(existing)

        issuingService.returnBook(999L, now)

        val updated = existing.copy(returnDate = now)
        verify(borrowingRecordRepository, times(1)).update(updated)
    }

    @Test
    fun givenBorrowingRecordNotFoundWhenReturnBookThenThrowException() {
        given(borrowingRecordRepository.findById(888L)).willReturn(null)

        val ex = assertThrows<IllegalStateException> {
            issuingService.returnBook(888L, now)
        }
        assertEquals("BorrowingRecord not found!", ex.message)

        verify(borrowingRecordRepository, never()).update(any())
    }

    @Test
    fun givenBookAlreadyReturnedWhenReturnBookThenThrowException() {
        val existing = BorrowingRecord(id = 777, libraryBookId = 400, borrowDate = now.minusDays(5), returnDate = now.minusDays(1))
        given(borrowingRecordRepository.findById(777L)).willReturn(existing)

        val ex = assertThrows<IllegalStateException> {
            issuingService.returnBook(777L, now)
        }
        assertEquals("Book already returned!", ex.message)

        verify(borrowingRecordRepository, never()).update(any())
    }
}