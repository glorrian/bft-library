package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BookRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Book>(
    jdbcTemplate,
    "books",
    Book::class.java
) {
    override val rowMapper: RowMapper<Book> = RowMapper { rs: ResultSet, _: Int ->
        Book(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            isbn = rs.getString("isbn"),
            publicationDate = rs.getDate("publication_date").toLocalDate()
        )
    }
}