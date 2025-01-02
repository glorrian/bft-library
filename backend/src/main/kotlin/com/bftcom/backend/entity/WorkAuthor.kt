package com.bftcom.backend.entity

data class WorkAuthor(
	override var id: Long = 0,
	val workId: Long,
	val authorId: Long
) : Entity
