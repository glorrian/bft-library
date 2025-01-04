package com.bftcom.backend.repository

import com.bftcom.backend.entity.Author
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDate

@Repository
class AuthorRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Author>(jdbcTemplate, "authors") {

	override fun mapRow(rs: ResultSet): Author {
		return Author(
			id = rs.getLong("id"),
			fullName = rs.getString("full_name"),
			pseudonym = rs.getString("pseudonym"),
			birthDate = rs.getObject("birth_date", LocalDate::class.java)
		)
	}

	override fun entityToParams(entity: Author): Map<String, Any?> {
		return mapOf(
			"full_name" to entity.fullName, "pseudonym" to entity.pseudonym, "birth_date" to entity.birthDate
		)
	}

	override fun validate(entity: Author) {
		require(entity.fullName.isNotBlank()) { "Author full name must not be blank" }
		require(entity.fullName.length <= 255) { "Author full name must not exceed 255 characters" }
		if (entity.pseudonym != null) {
			require(entity.pseudonym.isNotBlank()) { "Author pseudonym must not be blank" }
			require(entity.pseudonym.length <= 255) { "Author pseudonym must not exceed 255 characters" }
		}
	}

	override fun getEntityId(entity: Author): Long? = entity.id

	override fun setEntityId(entity: Author, generatedId: Long): Author {
		return entity.copy(id = generatedId)
	}
}