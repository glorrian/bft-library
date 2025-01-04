package com.bftcom.backend.repository

import com.bftcom.backend.entity.Book
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BookRepository(
	private val jdbc: JdbcTemplate
) : DefaultJdbcCrudRepository<Book>(jdbc, "books") {

	override fun mapRow(rs: ResultSet): Book {
		return Book(
			id = rs.getLong("id"),
			title = rs.getString("title"),
			isbn = rs.getString("isbn"),
			publicationDate = rs.getDate("publication_date").toLocalDate(),
			worksIds = null
		)
	}

	override fun entityToParams(entity: Book): Map<String, Any?> {
		return mapOf(
			"title" to entity.title, "isbn" to entity.isbn, "publication_date" to entity.publicationDate
		)
	}

	override fun validate(entity: Book) {
		require(entity.title.isNotBlank()) { "Book title must not be blank" }
		require(entity.title.length <= 255) { "Book title must not exceed 255 characters" }
		require(
			entity.isbn.length == 13 && entity.isbn.matches(Regex("\\d{13}"))
		) { "Book ISBN must be 13 digits length" }
	}

	override fun getEntityId(entity: Book): Long? = entity.id

	override fun setEntityId(entity: Book, generatedId: Long): Book {
		return entity.copy(id = generatedId)
	}

	override fun create(entity: Book): Book {
		val created = super.create(entity)
		created.id?.let { bookId ->
			entity.worksIds?.forEach { wId ->
				jdbc.update("INSERT INTO book_works (book_id, work_id) VALUES (?, ?)", bookId, wId)
			}
		}
		return created
	}

	override fun update(entity: Book): Book {
		val updated = super.update(entity)
		updated.id?.let { bookId ->
			jdbc.update("DELETE FROM book_works WHERE book_id = ?", bookId)
			entity.worksIds?.forEach { wId ->
				jdbc.update("INSERT INTO book_works (book_id, work_id) VALUES (?, ?)", bookId, wId)
			}
		}
		return updated
	}

	override fun deleteById(id: Long) {
		jdbc.update("DELETE FROM book_works WHERE book_id = ?", id)
		super.deleteById(id)
	}
}
