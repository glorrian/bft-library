package com.bftcom.backend.repository

import com.bftcom.backend.entity.LibraryBook
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class LibraryBookRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<LibraryBook>(
    jdbcTemplate,
    "library_books",
    LibraryBook::class.java
) {
    override val rowMapper: RowMapper<LibraryBook> = RowMapper { rs: ResultSet, _: Int ->
        LibraryBook(
            id = rs.getLong("id"),
            bookId = rs.getLong("book_id")
        )
    }
}