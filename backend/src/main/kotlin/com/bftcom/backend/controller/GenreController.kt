package com.bftcom.backend.controller

import com.bftcom.backend.dto.GenreCreateDto
import com.bftcom.backend.dto.GenreUpdateDto
import com.bftcom.backend.entity.Genre
import com.bftcom.backend.service.GenreService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/genres")
class GenreController(
	private val genreService: GenreService
) {

	@GetMapping
	fun getAllGenres(): List<Genre> {
		return genreService.getAllGenres()
	}

	@GetMapping("/{id}")
	fun getGenre(@PathVariable id: Long): Genre? {
		return genreService.getGenreById(id)
	}

	@PostMapping
	fun createGenre(@RequestBody dto: GenreCreateDto): Genre {
		val newGenre = Genre(
			id = null,
			name = dto.name
		)
		return genreService.createGenre(newGenre)
	}

	@PutMapping("/{id}")
	fun updateGenre(
		@PathVariable id: Long, @RequestBody dto: GenreUpdateDto
	): Genre {
		val existing = genreService.getGenreById(id)
			?: throw IllegalArgumentException("Genre with id=$id not found")

		val updated = existing.copy(
			name = dto.name
				?: existing.name
		)
		return genreService.updateGenre(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteGenre(@PathVariable id: Long) {
		genreService.deleteGenre(id)
	}
}