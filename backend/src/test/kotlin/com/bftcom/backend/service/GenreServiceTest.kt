package com.bftcom.backend.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*

//@ExtendWith(MockitoExtension::class)
//class GenreServiceTest {
//
//    @Mock
//    private lateinit var genreRepository: GenreRepository
//
//    @InjectMocks
//    private lateinit var genreService: GenreService
//
//    @Test
//    fun givenValidGenre_whenCreate_thenGenreIsCreated() {
//        val genre = Genre(id = 0L, name = "Test Genre")
//        `when`(genreRepository.save(genre)).thenReturn(1L)
//        val createdGenre = genreService.create(genre)
//        assertNotNull(createdGenre)
//        assertEquals(1L, createdGenre.id)
//        assertEquals("Test Genre", createdGenre.name)
//    }
//
//    @Test
//    fun givenNonZeroIdGenre_whenCreate_thenThrowsException() {
//        val genre = Genre(id = 1L, name = "Test Genre")
//        val exception = assertThrows<IllegalArgumentException> {
//            genreService.create(genre)
//        }
//        assertEquals("Genre id must be 0 to create a new genre", exception.message)
//    }
//
//    @Test
//    fun givenValidGenre_whenUpdate_thenGenreIsUpdated() {
//        val genre = Genre(id = 1L, name = "Updated Genre")
//        genreService.update(genre)
//        verify(genreRepository, times(1)).save(genre)
//    }
//
//    @Test
//    fun givenZeroIdGenre_whenUpdate_thenThrowsException() {
//        val genre = Genre(id = 0L, name = "Test Genre")
//        val exception = assertThrows<IllegalArgumentException> {
//            genreService.update(genre)
//        }
//        assertEquals("Genre id must not be 0 to update a genre", exception.message)
//    }
//}