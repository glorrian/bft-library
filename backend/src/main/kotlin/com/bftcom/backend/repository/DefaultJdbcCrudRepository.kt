package com.bftcom.backend.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import java.sql.ResultSet

/**
 * An abstract JDBC-based implementation of the {@link CrudRepository} interface, providing basic CRUD operations
 * for a specified database table. Subclasses need to implement entity-specific behavior such as mapping rows,
 * validating entities, and handling entity IDs.
 *
 * <p>This class uses Spring's {@link JdbcTemplate} and {@link SimpleJdbcInsert} to perform database operations.
 * It assumes the table has a primary key column, typically named "id", which is auto-generated on insert.</p>
 *
 * @param T The type of the entity managed by this repository.
 * @property jdbcTemplate Template for executing SQL queries and updates.
 * @property tableName The name of the database table for CRUD operations.
 * @property idColumnName The name of the ID column in the table (default is "id").
 */
@Suppress("SqlSourceToSinkFlow")
abstract class DefaultJdbcCrudRepository<T>(
	protected val jdbcTemplate: JdbcTemplate,
	protected val tableName: String,
	private val idColumnName: String = "id"
) : CrudRepository<T, Long> {

	/**
	 * Maps a row from the ResultSet to an instance of entity type [T].
	 *
	 * @param rs The ResultSet containing a row of data.
	 * @return An entity of type [T] mapped from the given row.
	 */
	protected abstract fun mapRow(rs: ResultSet): T

	/**
	 * Validates the given entity before persistence operations.
	 *
	 * @param entity The entity to validate.
	 * @throws IllegalArgumentException If the entity is invalid.
	 */
	protected abstract fun entityToParams(entity: T): Map<String, Any?>

	/**
	 * Validates the given entity before persistence operations.
	 *
	 * @param entity The entity to validate.
	 * @throws IllegalArgumentException If the entity is invalid.
	 */
	protected abstract fun validate(entity: T)

	/**
	 * Extracts the identifier from the given entity.
	 *
	 * @param entity The entity from which to extract the ID.
	 * @return The ID of the entity, or null if the entity has no ID assigned.
	 */
	protected abstract fun getEntityId(entity: T): Long?

	/**
	 * Assigns a generated identifier to the given entity.
	 *
	 * @param entity The entity for which to set the ID.
	 * @param generatedId The generated ID to assign.
	 * @return A new instance of the entity with the assigned ID.
	 */
	protected abstract fun setEntityId(entity: T, generatedId: Long): T

	override fun findAll(): List<T> {
		val sql = "SELECT * FROM $tableName"
		return jdbcTemplate.query(sql) { rs, _ -> mapRow(rs) }
	}

	override fun findById(id: Long): T? {
		val sql = "SELECT * FROM $tableName WHERE $idColumnName = ?"
		return jdbcTemplate.query(sql, { rs, _ -> mapRow(rs) }, id).firstOrNull()
	}

	/**
	 * Creates a new entity in the database.
	 *
	 * <p>This method validates the entity, inserts it into the database using {@link SimpleJdbcInsert},
	 * retrieves the generated key, and returns a new instance of the entity with the assigned ID.</p>
	 *
	 * @param entity The entity to create.
	 * @return The created entity with an assigned ID.
	 */
	override fun create(entity: T): T {
		val insert = SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns(idColumnName)
		validate(entity)
		val params = entityToParams(entity)
		val generatedId = insert.executeAndReturnKey(params).toLong()
		return setEntityId(entity, generatedId)
	}

	/**
	 * Updates an existing entity in the database.
	 *
	 * <p>The method first retrieves the entity's ID. If the ID is null, it throws an exception.
	 * It then converts the entity to a parameter map and constructs an SQL UPDATE statement
	 * dynamically based on the non-null fields. Finally, it executes the update.</p>
	 *
	 * @param entity The entity with updated data.
	 * @return The updated entity.
	 * @throws IllegalArgumentException if the entity does not have an ID.
	 */
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