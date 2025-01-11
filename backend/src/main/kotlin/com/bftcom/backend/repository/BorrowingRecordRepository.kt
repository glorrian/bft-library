package com.bftcom.backend.repository

import com.bftcom.backend.entity.BorrowingRecord
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BorrowingRecordRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<BorrowingRecord>(jdbcTemplate, "borrowing_records") {

	override fun mapRow(rs: ResultSet): BorrowingRecord {
		return BorrowingRecord(
			id = rs.getLong("id"),
			readerId = rs.getLong("reader_id"),
			libraryBookId = rs.getLong("library_book_id"),
			borrowDate = rs.getDate("borrow_date").toLocalDate(),
			returnDate = rs.getDate("return_date")?.toLocalDate()
		)
	}

	override fun entityToParams(entity: BorrowingRecord): Map<String, Any?> {
		return mapOf(
			"library_book_id" to entity.libraryBookId,
			"reader_id" to entity.readerId,
			"borrow_date" to entity.borrowDate,
			"return_date" to entity.returnDate
		)
	}

	override fun validate(entity: BorrowingRecord) {
		require(entity.libraryBookId > 0) { "Library book ID must be positive" }
		require(entity.readerId > 0) { "Reader ID must be positive" }
		require(
			entity.borrowDate <= (entity.returnDate ?: entity.borrowDate)
		) { "Return date must be after borrow date" }
	}

	override fun getEntityId(entity: BorrowingRecord): Long? = entity.id

	override fun setEntityId(entity: BorrowingRecord, generatedId: Long): BorrowingRecord {
		return entity.copy(id = generatedId)
	}

	fun findAllByLibraryBookIdAndReturnDateIsNull(libraryBookId: Long): List<BorrowingRecord> {
		return jdbcTemplate.query(
			"SELECT * FROM borrowing_records WHERE library_book_id = ? AND return_date IS NULL",
			{ rs, _ -> mapRow(rs) },
			libraryBookId
		)
	}

}
