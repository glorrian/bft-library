package com.bftcom.backend.repository

interface CrudRepository<T, ID> {
	fun save(entity: T): ID
	fun findById(id: ID): T?
	fun findAll(): List<T>
	fun deleteById(id: ID): Boolean
	fun existsById(id: Long): Boolean
}
