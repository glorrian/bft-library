package com.bftcom.backend.repository

import com.bftcom.backend.entity.LibraryBook
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class LibraryBookRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<LibraryBook>(jdbcTemplate, "library_books") {

	override fun mapRow(rs: ResultSet): LibraryBook {
		return LibraryBook(
			id = rs.getLong("id"), bookId = rs.getLong("book_id")
		)
	}

	override fun entityToParams(entity: LibraryBook): Map<String, Any?> {
		return mapOf(
			"book_id" to entity.bookId
		)
	}

	override fun validate(entity: LibraryBook) {
		require(entity.bookId > 0) { "Book ID must be positive" }
	}

	override fun getEntityId(entity: LibraryBook): Long? = entity.id

	override fun setEntityId(entity: LibraryBook, generatedId: Long): LibraryBook {
		return entity.copy(id = generatedId)
	}
}