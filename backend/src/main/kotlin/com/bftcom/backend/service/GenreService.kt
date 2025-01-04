package com.bftcom.backend.service

import com.bftcom.backend.entity.Genre
import com.bftcom.backend.repository.GenreRepository
import org.springframework.stereotype.Service

@Service
class GenreService(
	private val genreRepository: GenreRepository
) {

	fun getAllGenres(): List<Genre> {
		return genreRepository.findAll()
	}

	fun getGenreById(id: Long): Genre? {
		return genreRepository.findById(id)
	}

	fun createGenre(genre: Genre): Genre {
		return genreRepository.create(genre)
	}

	fun updateGenre(genre: Genre): Genre {
		return genreRepository.update(genre)
	}

	fun deleteGenre(id: Long) {
		genreRepository.deleteById(id)
	}
}
