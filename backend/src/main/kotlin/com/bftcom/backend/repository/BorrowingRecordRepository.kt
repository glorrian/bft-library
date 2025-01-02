package com.bftcom.backend.repository

import com.bftcom.backend.entity.BorrowingRecord
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BorrowingRecordRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<BorrowingRecord>(
    jdbcTemplate,
    "borrowing_records",
    BorrowingRecord::class.java
) {
    override val rowMapper: RowMapper<BorrowingRecord> = RowMapper { rs: ResultSet, _: Int ->
        BorrowingRecord(
            id = rs.getLong("id"),
            libraryBookId = rs.getLong("library_book_id"),
            borrowDate = rs.getDate("borrow_date").toLocalDate()
        )
    }
}