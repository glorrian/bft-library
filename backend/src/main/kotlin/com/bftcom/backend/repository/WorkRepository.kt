package com.bftcom.backend.repository

import com.bftcom.backend.entity.Work
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class WorkRepository(
	private val jdbc: JdbcTemplate
) : DefaultJdbcCrudRepository<Work>(jdbc, "works") {

	override fun mapRow(rs: ResultSet): Work {
		return Work(
			id = rs.getLong("id"),
			title = rs.getString("title"),
			genreId = rs.getLong("genre_id").takeIf { it != 0L },
			authorsIds = null
		)
	}

	override fun entityToParams(entity: Work): Map<String, Any?> {
		return mapOf(
			"title" to entity.title, "genre_id" to entity.genreId
		)
	}

	override fun validate(entity: Work) {
		require(entity.title.isNotBlank()) { "Work title must not be blank" }
		require(entity.title.length <= 255) { "Work title must not exceed 255 characters" }
	}

	override fun getEntityId(entity: Work): Long? = entity.id

	override fun setEntityId(entity: Work, generatedId: Long): Work {
		return entity.copy(id = generatedId)
	}

	override fun create(entity: Work): Work {
		val created = super.create(entity)
		val workId = created.id ?: error("Work ID is null after create")

		entity.authorsIds?.forEach { authorId ->
			val sql = "INSERT INTO work_authors (work_id, author_id) VALUES (?, ?)"
			jdbc.update(sql, workId, authorId)
		}
		return created
	}

	override fun update(entity: Work): Work {
		val updated = super.update(entity)
		val workId = updated.id ?: error("Work ID is null in update")

		jdbc.update("DELETE FROM work_authors WHERE work_id = ?", workId)
		entity.authorsIds?.forEach { authorId ->
			val sql = "INSERT INTO work_authors (work_id, author_id) VALUES (?, ?)"
			jdbc.update(sql, workId, authorId)
		}
		return updated
	}

	override fun deleteById(id: Long) {
		jdbc.update("DELETE FROM work_authors WHERE work_id = ?", id)
		super.deleteById(id)
	}

	/**
	 * Retrieves all works eagerly loading associated author IDs.
	 *
	 * This method fetches all [Work] entities and then loads the associated author IDs
	 * for each work from the `work_authors` table, populating the [Work.authorsIds] property.
	 *
	 * @return A list of all [Work] entities with their associated author IDs populated.
	 */
	fun findAllEager(): List<Work> {
		val works = findAll()
		val workIds = works.mapNotNull { it.id }
		val mapAuthors = loadAuthorsIdsForWorks(workIds)
		return works.map { w ->
			w.copy(
				authorsIds = mapAuthors[w.id] ?: emptyList()
			)
		}
	}

	/**
	 * Retrieves a work by its ID, eagerly loading associated author IDs.
	 *
	 * This method finds a [Work] entity by ID and loads its associated author IDs
	 * from the "work_authors" table, setting the [Work.authorsIds] property.
	 *
	 * @param id The identifier of the work.
	 * @return The [Work] entity with its [Work.authorsIds] populated, or null if not found.
	 */
	fun findByIdEager(id: Long): Work? {
		val w = findById(id) ?: return null
		val mapAuthors = loadAuthorsIdsForWorks(listOf(id))
		val aIds = mapAuthors[id] ?: emptyList()
		return w.copy(authorsIds = aIds)
	}

	private fun loadAuthorsIdsForWorks(workIds: List<Long>): Map<Long, List<Long>> {
		if (workIds.isEmpty()) return emptyMap()
		val inClause = workIds.joinToString(", ")
		val sql = """
            SELECT work_id, author_id
            FROM work_authors
            WHERE work_id IN ($inClause)
        """.trimIndent()

		val result = mutableMapOf<Long, MutableList<Long>>()
		jdbc.query(sql) { rs ->
			val wId = rs.getLong("work_id")
			val aId = rs.getLong("author_id")
			result.computeIfAbsent(wId) { mutableListOf() }.add(aId)
		}
		return result
	}
}