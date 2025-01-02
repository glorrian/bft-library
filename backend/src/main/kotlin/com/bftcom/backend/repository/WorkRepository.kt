package com.bftcom.backend.repository

import com.bftcom.backend.entity.Work
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class WorkRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Work>(
    jdbcTemplate,
    "works",
    Work::class.java
) {
    override val rowMapper: RowMapper<Work> = RowMapper { rs: ResultSet, _: Int ->
        Work(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            genreId = rs.getInt("genre_id")
        )
    }
}