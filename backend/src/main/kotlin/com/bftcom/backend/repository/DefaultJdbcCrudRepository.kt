package com.bftcom.backend.repository

import com.bftcom.backend.entity.Entity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.sql.PreparedStatement
import kotlin.reflect.full.memberProperties
@Suppress("SqlSourceToSinkFlow")
abstract class DefaultJdbcCrudRepository<T : Entity>(
	private val jdbcTemplate: JdbcTemplate,
	private val tableName: String,
	private val clazz: Class<T>
) : CrudRepository<T, Long> {

	protected abstract val rowMapper: RowMapper<T>

	override fun save(entity: T): Long {
		return if (entity.id == 0L) {
			insert(entity)
		} else {
			update(entity)
			entity.id
		}
	}

	override fun findById(id: Long): T? {
		val sql = "SELECT * FROM $tableName WHERE id = ?"
		return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
	}

	override fun findAll(): List<T> {
		val sql = "SELECT * FROM $tableName"
		return jdbcTemplate.query(sql, rowMapper)
	}

	override fun deleteById(id: Long): Boolean {
		val sql = "DELETE FROM $tableName WHERE id = ?"
		val affectedRows = jdbcTemplate.update(sql, id)
		return affectedRows > 0
	}

	override fun existsById(id: Long): Boolean {
		val sql = "SELECT COUNT(*) FROM $tableName WHERE id = ?"
		val res = jdbcTemplate.queryForObject(sql, Int::class.java, id)
		return res != null && res > 0
	}


	private fun getColumns(entity: T): Map<String, Any?> {
		return clazz.kotlin.memberProperties
			.filter { it.name != "id" }
			.associate { it.name.toSnakeCase() to it.get(entity) }
	}

	private fun insert(entity: T): Long {
		val columns = getColumns(entity).keys.joinToString(", ")
		val placeholders = getColumns(entity).keys.joinToString(", ") { "?" }
		val sql = "INSERT INTO $tableName ($columns) VALUES ($placeholders)"

		val values = getColumns(entity).values.toTypedArray()

		val keyHolder: KeyHolder = GeneratedKeyHolder()

		jdbcTemplate.update({ connection ->
			val ps: PreparedStatement = connection.prepareStatement(sql, arrayOf("id"))
			for (i in values.indices) {
				ps.setObject(i + 1, values[i])
			}
			ps
		}, keyHolder)

		return keyHolder.key?.toLong()
			?: throw RuntimeException("Failed to retrieve id of the inserted entity")
	}

	private fun update(entity: T) {
		val columns = getColumns(entity).keys.joinToString(", ") { "$it = ?" }
		val sql = "UPDATE $tableName SET $columns WHERE id = ?"

		val values = getColumns(entity).values.toMutableList()
		values.add(entity.id)

		jdbcTemplate.update(sql, *values.toTypedArray())
	}

	private fun String.toSnakeCase(): String {
		return this.fold(StringBuilder()) { acc, c ->
			if (c.isUpperCase()) {
				if (acc.isNotEmpty()) acc.append("_")
				acc.append(c.lowercaseChar())
			} else {
				acc.append(c)
			}
			acc
		}.toString()
	}
}
