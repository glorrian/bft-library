package com.bftcom.backend.repository

import com.bftcom.backend.entity.Genre
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class GenreRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Genre>(jdbcTemplate, "genres") {

	override fun mapRow(rs: ResultSet): Genre {
		return Genre(
			id = rs.getLong("id"), name = rs.getString("name")
		)
	}

	override fun entityToParams(entity: Genre): Map<String, Any?> {
		return mapOf("name" to entity.name)
	}

	override fun validate(entity: Genre) {
		require(entity.name.isNotBlank()) { "Genre name must not be blank" }
		require(entity.name.length <= 255) { "Genre name must not exceed 255 characters" }
	}

	override fun getEntityId(entity: Genre): Long? = entity.id

	override fun setEntityId(entity: Genre, generatedId: Long): Genre {
		return entity.copy(id = generatedId)
	}
}
