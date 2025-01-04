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

	fun findByIdEager(id: Long): Book? {
		val book = findById(id) ?: return null
		val works = loadWorksForBook(id)
		return book.copy(worksIds = works)
	}

	fun findAllEager(): List<Book> {
		val books = findAll()

		val bookIds = books.mapNotNull { it.id }
		if (bookIds.isEmpty()) {
			return books
		}

		val worksMap = loadWorksForBooks(bookIds)

		return books.map { b ->
			b.copy(worksIds = worksMap[b.id] ?: emptyList())
		}
	}

	private fun loadWorksForBook(bookId: Long): List<Long> {
		val sql = """
			SELECT work_id 
			  FROM book_works
			 WHERE book_id = ?
		""".trimIndent()

		val works = mutableListOf<Long>()
		jdbc.query(sql, { rs, _ -> rs.getLong("work_id") }, bookId).forEach { works.add(it) }
		return works
	}

	private fun loadWorksForBooks(bookIds: List<Long>): Map<Long, List<Long>> {
		val inClause = bookIds.joinToString(", ")
		val sql = """
            SELECT book_id, work_id 
              FROM book_works
             WHERE book_id IN ($inClause)
        """.trimIndent()

		val map = mutableMapOf<Long, MutableList<Long>>()
		jdbcTemplate.query(sql) { rs ->
			val bId = rs.getLong("book_id")
			val wId = rs.getLong("work_id")
			map.computeIfAbsent(bId) { mutableListOf() }.add(wId)
		}
		return map
	}
}
