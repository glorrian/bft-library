package com.bftcom.backend.entity

data class Genre(
	override var id: Long = 0,
	val name: String
) : Entity