package com.bftcom.backend.service

import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.Reader
import com.bftcom.backend.repository.BorrowingRecordRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class BorrowingRecordServiceTest {

    @Mock
    private lateinit var borrowingRecordRepository: BorrowingRecordRepository

	@InjectMocks
    private lateinit var borrowingRecordService: BorrowingRecordService

    private lateinit var borrowingRecord: BorrowingRecord
    private lateinit var reader: Reader

    @BeforeEach
    fun setup() {
        reader = Reader(id = 1L, fullName = "John Doe", email = "john.doe@example.com")
        borrowingRecord = BorrowingRecord(id = 1L, libraryBookId = 1L, readerId = reader.id!!, borrowDate = LocalDate.of(2023, 1, 1), returnDate = null)
    }

    @Test
    fun whenGetAllRecords_thenAllRecordsAreReturned() {
        whenever(borrowingRecordRepository.findAll()).thenReturn(listOf(borrowingRecord))
        val records = borrowingRecordService.getAllRecords()
        assertNotNull(records)
        assertEquals(1, records.size)
        assertEquals(borrowingRecord, records[0])
    }

    @Test
    fun givenRecordId_whenGetRecordById_thenRecordIsReturned() {
        whenever(borrowingRecordRepository.findById(1L)).thenReturn(borrowingRecord)
        val foundRecord = borrowingRecordService.getRecordById(1L)
        assertNotNull(foundRecord)
        assertEquals(borrowingRecord, foundRecord)
    }

    @Test
    fun givenRecord_whenCreateRecord_thenRecordIsCreated() {
        whenever(borrowingRecordRepository.create(borrowingRecord)).thenReturn(borrowingRecord)
        val createdRecord = borrowingRecordService.createRecord(borrowingRecord)
        assertNotNull(createdRecord)
        assertEquals(borrowingRecord, createdRecord)
    }

    @Test
    fun givenRecord_whenUpdateRecord_thenRecordIsUpdated() {
		whenever(borrowingRecordRepository.update(borrowingRecord)).thenReturn(borrowingRecord)
        val updatedRecord = borrowingRecordService.updateRecord(borrowingRecord)
        assertNotNull(updatedRecord)
        assertEquals(borrowingRecord, updatedRecord)
    }

    @Test
    fun givenRecordId_whenDeleteRecord_thenRecordIsDeleted() {
        doNothing().whenever(borrowingRecordRepository).deleteById(borrowingRecord.id!!)
        borrowingRecordService.deleteRecord(borrowingRecord.id!!)
        verify(borrowingRecordRepository, times(1)).deleteById(borrowingRecord.id!!)
    }
}