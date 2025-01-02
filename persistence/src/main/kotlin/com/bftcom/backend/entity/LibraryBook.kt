package com.bftcom.backend.entity

data class LibraryBook(
	override var id: Long = 0,
	val bookId: Int
) : Entity