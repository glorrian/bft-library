package com.bftcom.backend.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import java.sql.ResultSet

@Suppress("SqlSourceToSinkFlow")
abstract class DefaultJdbcCrudRepository<T>(
	protected val jdbcTemplate: JdbcTemplate,
	protected val tableName: String, private val idColumnName: String = "id"
) : CrudRepository<T, Long> {

	protected abstract fun mapRow(rs: ResultSet): T

	protected abstract fun entityToParams(entity: T): Map<String, Any?>

	protected abstract fun validate(entity: T)

	protected abstract fun getEntityId(entity: T): Long?

	protected abstract fun setEntityId(entity: T, generatedId: Long): T

	override fun findAll(): List<T> {
		val sql = "SELECT * FROM $tableName"
		return jdbcTemplate.query(sql) { rs, _ -> mapRow(rs) }
	}

	override fun findById(id: Long): T? {
		val sql = "SELECT * FROM $tableName WHERE $idColumnName = ?"
		return jdbcTemplate.query(sql, { rs, _ -> mapRow(rs) }, id).firstOrNull()
	}

	override fun create(entity: T): T {
		val insert = SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns(idColumnName)
		validate(entity)
		val params = entityToParams(entity)
		val generatedId = insert.executeAndReturnKey(params).toLong()
		return setEntityId(entity, generatedId)
	}

	override fun update(entity: T): T {
		val entityId = getEntityId(entity) ?: throw IllegalArgumentException("Cannot update entity without ID")

		val params = entityToParams(entity)
		if (params.isEmpty()) {
			return entity
		}
		val setClause = params.keys.joinToString { "$it = ?" }
		val sql = "UPDATE $tableName SET $setClause WHERE $idColumnName = ?"

		val values = params.values.toList() + entityId
		jdbcTemplate.update(sql, *values.toTypedArray())

		return entity
	}

	override fun deleteById(id: Long) {
		val sql = "DELETE FROM $tableName WHERE $idColumnName = ?"
		jdbcTemplate.update(sql, id)
	}
}