package com.bftcom.backend.repository

/**
 * A generic repository interface providing basic CRUD operations for entities.
 *
 * @param T  The type of the entity.
 * @param ID The type of the entity identifier.
 */
interface CrudRepository<T, ID> {
	/**
	 * Retrieves all entities of type [T].
	 *
	 * @return A list of all entities.
	 */
	fun findAll(): List<T>

	/**
	 * Finds an entity by its identifier.
	 *
	 * @param id The identifier of the entity.
	 * @return The entity if found, or null otherwise.
	 */
	fun findById(id: ID): T?

	/**
	 * Persists a new entity.
	 *
	 * @param entity The entity to create.
	 * @return The created entity, typically with an assigned identifier.
	 */
	fun create(entity: T): T

	/**
	 * Updates an existing entity.
	 *
	 * @param entity The entity containing updated data.
	 * @return The updated entity.
	 */
	fun update(entity: T): T

	/**
	 * Deletes an entity by its identifier.
	 *
	 * @param id The identifier of the entity to delete.
	 */
	fun deleteById(id: ID)
}
