package com.bftcom.backend.entity

data class BookWork(
	override var id: Long = 0,
	val bookId: Int,
	val workId: Int
) : Entity