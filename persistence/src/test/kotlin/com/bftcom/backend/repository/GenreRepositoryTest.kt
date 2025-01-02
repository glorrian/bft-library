package com.bftcom.backend.repository

import com.bftcom.backend.entity.Genre
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
class GenreRepositoryTest {

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenGenre_whenSaveAndFindById_thenGenreIsFound() {
        val genre = Genre(name = "Science Fiction")
        val id = genreRepository.save(genre)
        val savedGenre = genreRepository.findById(id)
        assertNotNull(savedGenre)
        assertEquals("Science Fiction", savedGenre?.name)
    }

    @Test
    fun givenGenre_whenDelete_thenGenreIsDeleted() {
        val genre = Genre(name = "Fantasy")
        val id = genreRepository.save(genre)
        genreRepository.deleteById(id)
        assertNull(genreRepository.findById(id))
    }

    @Test
    fun givenGenre_whenUpdateGenre_thenGenreIsUpdated() {
        val genre = Genre(name = "Mystery")
        val id = genreRepository.save(genre)
        val updatedGenre = genre.copy(id = id, name = "Thriller")
        genreRepository.save(updatedGenre)
        val foundGenre = genreRepository.findById(id)
        assertNotNull(foundGenre)
        assertEquals("Thriller", foundGenre?.name)
    }

    @Test
    fun givenGenres_whenFindAll_thenAllGenresAreFound() {
        val genre1 = Genre(name = "Romance")
        val genre2 = Genre(name = "Horror")
        genreRepository.save(genre1)
        genreRepository.save(genre2)
        val genres = genreRepository.findAll()
        assertTrue(genres.any { it.name == "Romance" })
        assertTrue(genres.any { it.name == "Horror" })
    }

    @Test
    fun givenGenre_whenExistsById_thenGenreExists() {
        val genre = Genre(name = "Adventure")
        val id = genreRepository.save(genre)
        assertTrue(genreRepository.existsById(id))
        genreRepository.deleteById(id)
        assertFalse(genreRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { genreRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenGenreIsNotFound() {
        val nonExistentId = 9999L
        val genre = genreRepository.findById(nonExistentId)
        assertNull(genre)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenGenreDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(genreRepository.existsById(nonExistentId))
    }

    @Test
    fun givenDuplicateGenreName_whenSave_thenDataIntegrityViolationExceptionThrown() {
        val genre1 = Genre(name = "Duplicate")
        val genre2 = Genre(name = "Duplicate")
        genreRepository.save(genre1)
        val exception = assertThrows<DataIntegrityViolationException> {
            genreRepository.save(genre2)
        }
        assertNotNull(exception)
    }
}