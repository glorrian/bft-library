package com.bftcom.backend.controller

import com.bftcom.backend.dto.LibraryBookCreateDto
import com.bftcom.backend.dto.LibraryBookUpdateDto
import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.service.LibraryBookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/library-books")
class LibraryBookController(
	private val libraryBookService: LibraryBookService
) {

	@GetMapping
	fun getAllLibraryBooks(): List<LibraryBook> {
		return libraryBookService.getAllLibraryBooks()
	}

	@GetMapping("/{id}")
	fun getLibraryBook(@PathVariable id: Long): LibraryBook? {
		return libraryBookService.getLibraryBookById(id)
	}

	@PostMapping
	fun createLibraryBook(@RequestBody dto: LibraryBookCreateDto): LibraryBook {
		val newLB = LibraryBook(
			id = null,
			bookId = dto.bookId
		)
		return libraryBookService.createLibraryBook(newLB)
	}

	@PutMapping("/{id}")
	fun updateLibraryBook(@PathVariable id: Long, @RequestBody dto: LibraryBookUpdateDto): LibraryBook {
		val existing = libraryBookService.getLibraryBookById(id)
			?: throw IllegalArgumentException("LibraryBook with id=$id not found")

		val updated = existing.copy(
			bookId = dto.bookId ?: existing.bookId
		)
		return libraryBookService.updateLibraryBook(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteLibraryBook(@PathVariable id: Long) {
		libraryBookService.deleteLibraryBook(id)
	}
}