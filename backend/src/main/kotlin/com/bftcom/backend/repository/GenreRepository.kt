package com.bftcom.backend.repository

import com.bftcom.backend.entity.Genre
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class GenreRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Genre>(
    jdbcTemplate,
    "genres",
    Genre::class.java
) {
    override val rowMapper: RowMapper<Genre> = RowMapper { rs: ResultSet, _: Int ->
        Genre(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
    }
}