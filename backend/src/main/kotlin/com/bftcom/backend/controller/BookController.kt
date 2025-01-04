package com.bftcom.backend.controller

import com.bftcom.backend.dto.BookCreateDto
import com.bftcom.backend.dto.BookUpdateDto
import com.bftcom.backend.entity.Book
import com.bftcom.backend.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
	private val bookService: BookService
) {

	@GetMapping
	fun getAllBooks(
		@RequestParam(required = false, defaultValue = "false") eager: Boolean
	): List<Book> {
		return bookService.getAllBooks(eager)
	}

	@GetMapping("/{id}")
	fun getBook(
		@PathVariable id: Long,
		@RequestParam(required = false, defaultValue = "false") eager: Boolean
	): Book? {
		return bookService.getBookById(id, eager)
	}

	@PostMapping
	fun createBook(@RequestBody dto: BookCreateDto): Book {
		val newBook = Book(
			id = null,
			title = dto.title,
			isbn = dto.isbn,
			publicationDate = dto.publicationDate,
			worksIds = dto.workIds
		)
		return bookService.createBook(newBook)
	}

	@PutMapping("/{id}")
	fun updateBook(@PathVariable id: Long, @RequestBody dto: BookUpdateDto): Book {
		val existing =
			bookService.getBookById(id, eager = false)
				?: throw IllegalArgumentException("Book with id=$id not found")

		val updated = existing.copy(
			title = dto.title ?: existing.title,
			isbn = dto.isbn ?: existing.isbn,
			publicationDate = dto.publicationDate ?: existing.publicationDate,
			worksIds = dto.workIds ?: existing.worksIds
		)
		return bookService.updateBook(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteBook(@PathVariable id: Long) {
		bookService.deleteBook(id)
	}
}