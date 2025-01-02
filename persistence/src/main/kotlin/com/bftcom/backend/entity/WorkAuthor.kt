package com.bftcom.backend.entity

data class WorkAuthor(
	override var id: Long = 0,
	val workId: Int,
	val authorId: Int
) : Entity
