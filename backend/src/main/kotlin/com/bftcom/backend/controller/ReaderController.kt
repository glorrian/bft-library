package com.bftcom.backend.controller

import com.bftcom.backend.dto.ReaderCreateDto
import com.bftcom.backend.dto.ReaderUpdateDto
import com.bftcom.backend.entity.Reader
import com.bftcom.backend.service.ReaderService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/readers")
class ReaderController(
	private val readerService: ReaderService
) {

	@GetMapping
	fun getAllReaders(): List<Reader> {
		return readerService.getAllReaders()
	}

	@GetMapping("/{id}")
	fun getReader(@PathVariable id: Long): Reader? {
		return readerService.getReaderById(id)
	}

	@PostMapping
	fun createReader(@RequestBody dto: ReaderCreateDto): Reader {
		val newReader = Reader(
			id = null,
			fullName = dto.fullName,
			email = dto.email
		)
		return readerService.createReader(newReader)
	}

	@PutMapping("/{id}")
	fun updateReader(@PathVariable id: Long, @RequestBody dto: ReaderUpdateDto): Reader {
		val existing = readerService.getReaderById(id)
			?: throw IllegalArgumentException("Reader with id=$id not found")

		val updated = existing.copy(
			fullName = dto.fullName ?: existing.fullName,
			email = dto.email ?: existing.email
		)
		return readerService.updateReader(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteReader(@PathVariable id: Long) {
		readerService.deleteReader(id)
	}
}
