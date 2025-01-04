package com.bftcom.backend.repository

import com.bftcom.backend.entity.Reader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ReaderRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Reader>(jdbcTemplate, "readers") {

	override fun mapRow(rs: ResultSet): Reader {
		return Reader(
			id = rs.getLong("id"), fullName = rs.getString("full_name"), email = rs.getString("email")
		)
	}

	override fun entityToParams(entity: Reader): Map<String, Any?> {
		return mapOf(
			"full_name" to entity.fullName, "email" to entity.email
		)
	}

	override fun validate(entity: Reader) {
		require(entity.fullName.isNotBlank()) { "Full name must not be blank" }
		require(entity.fullName.length <= 255) { "Full name must not exceed 255 characters" }
		require(entity.email.isNotBlank()) { "Email must not be blank" }
		require(entity.email.length <= 255) { "Email must not exceed 255 characters" }
	}

	override fun getEntityId(entity: Reader): Long? = entity.id

	override fun setEntityId(entity: Reader, generatedId: Long): Reader {
		return entity.copy(id = generatedId)
	}
}