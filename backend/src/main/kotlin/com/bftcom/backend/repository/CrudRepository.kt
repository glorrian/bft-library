package com.bftcom.backend.repository

interface CrudRepository<T, ID> {
	fun findAll(): List<T>
	fun findById(id: ID): T?
	fun create(entity: T): T
	fun update(entity: T): T
	fun deleteById(id: ID)
}