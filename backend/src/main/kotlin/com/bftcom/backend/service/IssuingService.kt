package com.bftcom.backend.service

import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.LibraryBookRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class IssuingService(
	private val libraryBookRepository: LibraryBookRepository,
	private val borrowingRecordRepository: BorrowingRecordRepository
) {

	fun issueBook(libraryBookId: Long, borrowDate: LocalDate): BorrowingRecord {
		val libraryBook = libraryBookRepository.findById(libraryBookId)
			?: throw IllegalStateException("LibraryBook not found")

		val current = borrowingRecordRepository.findAll()
			.filter { it.libraryBookId == libraryBookId && it.returnDate == null }
		if (current.isNotEmpty()) {
			throw IllegalStateException("This book is already issued!")
		}

		val record = BorrowingRecord(
			id = null,
			libraryBookId = libraryBookId,
			borrowDate = borrowDate,
			returnDate = null
		)
		return borrowingRecordRepository.create(record)
	}

	fun returnBook(recordId: Long, returnDate: LocalDate) {
		val record = borrowingRecordRepository.findById(recordId)
			?: throw IllegalStateException("BorrowingRecord not found!")

		if (record.returnDate != null) {
			throw IllegalStateException("Book already returned!")
		}

		val updated = record.copy(returnDate = returnDate)
		borrowingRecordRepository.update(updated)
	}
}
