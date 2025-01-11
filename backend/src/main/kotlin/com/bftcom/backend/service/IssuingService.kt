// src/main/kotlin/com/bftcom/backend/service/IssuingService.kt
package com.bftcom.backend.service

import com.bftcom.backend.config.LibraryConfig
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.producer.ReminderProducer
import com.bftcom.backend.producer.ReturnProducer
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.LibraryBookRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class IssuingService(
	private val libraryBookRepository: LibraryBookRepository,
	private val borrowingRecordRepository: BorrowingRecordRepository,
	private val reminderProducer: ReminderProducer,
	private val libraryConfig: LibraryConfig,
	private val returnProducer: ReturnProducer
) {

	fun issueBook(readerId: Long, libraryBookId: Long, borrowDate: LocalDate): BorrowingRecord {
		libraryBookRepository.findById(libraryBookId)
			?: throw IllegalStateException("LibraryBook not found")

		val current = borrowingRecordRepository.findAllByLibraryBookIdAndReturnDateIsNull(libraryBookId)
		if (current.isNotEmpty()) {
			throw IllegalStateException("This book is already issued!")
		}

		val record = BorrowingRecord(
			id = null,
			libraryBookId = libraryBookId,
			borrowDate = borrowDate,
			readerId = readerId,
			returnDate = null
		)
		val savedRecord = borrowingRecordRepository.create(record)

		reminderProducer.sendReminderMessage(savedRecord.id!!, libraryConfig.maxBorrowingDays)

		return savedRecord
	}

	fun queueReturnBook(recordId: Long, returnDate: LocalDate) {
		val record = borrowingRecordRepository.findById(recordId)
			?: throw IllegalStateException("BorrowingRecord not found")
		if (record.returnDate != null) {
			throw IllegalStateException("Book already returned!")
		}

		returnProducer.sendReturnMessage(recordId, returnDate.toString())
	}
}