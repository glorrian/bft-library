package com.bftcom.backend.repository

import com.bftcom.backend.entity.Genre
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GenreRepositoryTest {

	@Autowired
	private lateinit var genreRepository: GenreRepository

	@Test
	fun givenGenre_whenSave_thenCanBeFoundById() {
		val genre = Genre(name = "Science Fiction")
		val savedGenre = genreRepository.create(genre)
		val foundGenre = genreRepository.findById(savedGenre.id!!)
		assertNotNull(foundGenre)
		assertEquals("Science Fiction", foundGenre?.name)
	}

	@Test
	fun givenGenre_whenDelete_thenCannotBeFoundById() {
		val genre = Genre(name = "Fantasy")
		val savedGenre = genreRepository.create(genre)
		genreRepository.deleteById(savedGenre.id!!)
		val foundGenre = genreRepository.findById(savedGenre.id!!)
		assertNull(foundGenre)
	}

	@Test
	fun givenGenre_whenUpdate_thenUpdatedValuesCanBeFound() {
		val genre = Genre(name = "Mystery")
		val savedGenre = genreRepository.create(genre)
		val updatedGenre = savedGenre.copy(name = "Thriller")
		genreRepository.update(updatedGenre)
		val foundGenre = genreRepository.findById(savedGenre.id!!)
		assertNotNull(foundGenre)
		assertEquals("Thriller", foundGenre?.name)
	}

	@Test
	fun givenMultipleGenres_whenFindAll_thenAllGenresAreReturned() {
		val genre1 = Genre(name = "Horror")
		val genre2 = Genre(name = "Romance")
		genreRepository.create(genre1)
		genreRepository.create(genre2)
		val genres = genreRepository.findAll()
		assertTrue(genres.any { it.name == "Horror" })
		assertTrue(genres.any { it.name == "Romance" })
	}

	@Test
	fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
		val nonExistentId = 9999L
		val result = runCatching { genreRepository.deleteById(nonExistentId) }
		assertTrue(result.isSuccess)
	}

	@Test
	fun givenNonExistentId_whenFindById_thenReturnsNull() {
		val nonExistentId = 9999L
		val genre = genreRepository.findById(nonExistentId)
		assertNull(genre)
	}

	@Test
	fun givenGenreWithEmptyName_whenSave_thenThrowsException() {
		val genre = Genre(name = "")
		val exception = assertThrows<Exception> {
			genreRepository.create(genre)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenGenreWithLongName_whenSave_thenThrowsException() {
		val longName = "a".repeat(256)
		val genre = Genre(name = longName)
		val exception = assertThrows<Exception> {
			genreRepository.create(genre)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenGenreWithSpecialCharactersInName_whenSave_thenCanBeFoundById() {
		val specialName = "Sci-Fi & Fantasy"
		val genre = Genre(name = specialName)
		val savedGenre = genreRepository.create(genre)
		val foundGenre = genreRepository.findById(savedGenre.id!!)
		assertNotNull(foundGenre)
		assertEquals(specialName, foundGenre?.name)
	}
}