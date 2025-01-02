package com.bftcom.backend.entity

data class Reader(
	override var id: Long = 0,
	val fullName: String,
	val email: String
) : Entity