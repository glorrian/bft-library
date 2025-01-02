package com.bftcom.backend.repository

import com.bftcom.backend.entity.BookWork
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BookWorkRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<BookWork>(
    jdbcTemplate,
    "book_works",
    BookWork::class.java
) {
    override val rowMapper: RowMapper<BookWork> = RowMapper { rs: ResultSet, _: Int ->
        BookWork(
            id = rs.getLong("id"),
            bookId = rs.getLong("book_id"),
            workId = rs.getLong("work_id")
        )
    }
}