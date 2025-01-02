package com.bftcom.backend.repository

import com.bftcom.backend.entity.WorkAuthor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class WorkAuthorRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<WorkAuthor>(
    jdbcTemplate,
    "work_authors",
    WorkAuthor::class.java
) {
    override val rowMapper: RowMapper<WorkAuthor> = RowMapper { rs: ResultSet, _: Int ->
        WorkAuthor(
            id = rs.getLong("id"),
            workId = rs.getLong("work_id"),
            authorId = rs.getLong("author_id")
        )
    }
}