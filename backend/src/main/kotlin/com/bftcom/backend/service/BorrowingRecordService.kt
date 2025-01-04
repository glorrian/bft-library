package com.bftcom.backend.service

import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.repository.BorrowingRecordRepository
import org.springframework.stereotype.Service

@Service
class BorrowingRecordService(
	private val borrowingRecordRepository: BorrowingRecordRepository
) {

	fun getAllRecords(): List<BorrowingRecord> {
		return borrowingRecordRepository.findAll()
	}

	fun getRecordById(id: Long): BorrowingRecord? {
		return borrowingRecordRepository.findById(id)
	}

	fun createRecord(record: BorrowingRecord): BorrowingRecord {
		return borrowingRecordRepository.create(record)
	}

	fun updateRecord(record: BorrowingRecord): BorrowingRecord {
		return borrowingRecordRepository.update(record)
	}

	fun deleteRecord(id: Long) {
		borrowingRecordRepository.deleteById(id)
	}
}
