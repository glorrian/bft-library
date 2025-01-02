package com.bftcom.backend.repository

import com.bftcom.backend.entity.Author
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class AuthorRepository(
	jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Author>(
	jdbcTemplate,
	"authors",
	Author::class.java
) {
	override val rowMapper: RowMapper<Author> = RowMapper { rs: ResultSet, _: Int ->
		Author(
			id = rs.getLong("id"),
			fullName = rs.getString("full_name"),
			pseudonym = rs.getString("pseudonym"),
			birthDate = rs.getDate("birth_date").toLocalDate()
		)
	}
}