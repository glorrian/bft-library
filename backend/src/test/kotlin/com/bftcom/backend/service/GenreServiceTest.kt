package com.bftcom.backend.service

import com.bftcom.backend.entity.Genre
import com.bftcom.backend.repository.GenreRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class GenreServiceTest {

    @Mock
    private lateinit var genreRepository: GenreRepository

    @InjectMocks
    private lateinit var genreService: GenreService

    private lateinit var genre: Genre

    @BeforeEach
    fun setup() {
        genre = Genre(id = 1L, name = "Science Fiction")
    }

    @Test
    fun givenGenre_whenCreate_thenGenreIsCreated() {
        `when`(genreRepository.create(genre)).thenReturn(genre)
        val createdGenre = genreService.createGenre(genre)
        assertNotNull(createdGenre)
        assertEquals("Science Fiction", createdGenre.name)
    }

    @Test
    fun givenGenre_whenUpdate_thenGenreIsUpdated() {
        `when`(genreRepository.update(genre)).thenReturn(genre)
        val updatedGenre = genreService.updateGenre(genre)
        assertNotNull(updatedGenre)
        assertEquals("Science Fiction", updatedGenre.name)
    }

    @Test
    fun givenGenreId_whenDelete_thenGenreIsDeleted() {
        doNothing().`when`(genreRepository).deleteById(genre.id!!)
        genreService.deleteGenre(genre.id!!)
        verify(genreRepository, times(1)).deleteById(genre.id!!)
    }

    @Test
    fun givenGenreId_whenGetById_thenGenreIsReturned() {
        `when`(genreRepository.findById(genre.id!!)).thenReturn(genre)
        val foundGenre = genreService.getGenreById(genre.id!!)
        assertNotNull(foundGenre)
        assertEquals("Science Fiction", foundGenre?.name)
    }

    @Test
    fun whenGetAllGenres_thenAllGenresAreReturned() {
        val genres = listOf(genre)
        `when`(genreRepository.findAll()).thenReturn(genres)
        val foundGenres = genreService.getAllGenres()
        assertNotNull(foundGenres)
        assertEquals(1, foundGenres.size)
        assertEquals("Science Fiction", foundGenres[0].name)
    }
}