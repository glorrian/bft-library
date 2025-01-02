package com.bftcom.backend.repository

import com.bftcom.backend.entity.Reader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ReaderRepository(
    jdbcTemplate: JdbcTemplate
) : DefaultJdbcCrudRepository<Reader>(
    jdbcTemplate,
    "readers",
    Reader::class.java
) {
    override val rowMapper: RowMapper<Reader> = RowMapper { rs: ResultSet, _: Int ->
        Reader(
            id = rs.getLong("id"),
            fullName = rs.getString("full_name"),
            email = rs.getString("email")
        )
    }
}