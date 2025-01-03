package com.bftcom.backend.service

import com.bftcom.backend.entity.Genre
import com.bftcom.backend.repository.GenreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GenreService @Autowired constructor(
    private val genreRepository: GenreRepository
) {
    fun findAll(): List<Genre> = genreRepository.findAll()

    fun findById(id: Long) = genreRepository.findById(id)

	fun deleteById(id: Long): Boolean = genreRepository.deleteById(id)

    fun create(genre: Genre): Genre {
		if (genre.id != 0L) {
			throw IllegalArgumentException("Genre id must be 0 to create a new genre")
		}
		val genreId = genreRepository.save(genre)
		return genre.copy(id = genreId)
	}

    fun update(genre: Genre) {
		if (genre.id == 0L) {
			throw IllegalArgumentException("Genre id must not be 0 to update a genre")
		}
		genreRepository.save(genre)
    }
}