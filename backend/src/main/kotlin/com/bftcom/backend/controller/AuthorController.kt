package com.bftcom.backend.controller

import com.bftcom.backend.dto.AuthorCreateDto
import com.bftcom.backend.dto.AuthorUpdateDto
import com.bftcom.backend.entity.Author
import com.bftcom.backend.service.AuthorService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/authors")
class AuthorController(
	private val authorService: AuthorService
) {

	@GetMapping
	fun getAllAuthors(): List<Author> {
		return authorService.getAllAuthors()
	}

	@GetMapping("/{id}")
	fun getAuthor(@PathVariable id: Long): Author? {
		return authorService.getAuthorById(id)
	}

	@PostMapping
	fun createAuthor(@RequestBody dto: AuthorCreateDto): Author {
		val newAuthor = Author(
			id = null, fullName = dto.fullName, pseudonym = dto.pseudonym, birthDate = dto.birthDate
		)
		return authorService.createAuthor(newAuthor)
	}

	@PutMapping("/{id}")
	fun updateAuthor(@PathVariable id: Long, @RequestBody dto: AuthorUpdateDto): Author {
		val existing = authorService.getAuthorById(id)
			?: throw IllegalArgumentException("Author with id=$id not found")

		val updated = existing.copy(
			fullName = dto.fullName ?: existing.fullName,
			pseudonym = dto.pseudonym ?: existing.pseudonym,
			birthDate = dto.birthDate ?: existing.birthDate
		)
		return authorService.updateAuthor(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteAuthor(@PathVariable id: Long) {
		authorService.deleteAuthor(id)
	}
}