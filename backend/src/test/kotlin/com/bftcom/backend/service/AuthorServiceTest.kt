package com.bftcom.backend.service

import com.bftcom.backend.entity.Author
import com.bftcom.backend.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class AuthorServiceTest {

    @Mock
    private lateinit var authorRepository: AuthorRepository

    @InjectMocks
    private lateinit var authorService: AuthorService

    @Test
    fun givenValidAuthor_whenCreate_thenAuthorIsCreated() {
        val author = Author(id = 0L, fullName = "Test Author", birthDate = LocalDate.now())
        `when`(authorRepository.save(author)).thenReturn(1L)
        val createdAuthor = authorService.create(author)
        assertNotNull(createdAuthor)
        assertEquals(1L, createdAuthor.id)
        assertEquals("Test Author", createdAuthor.fullName)
    }

    @Test
    fun givenNonZeroIdAuthor_whenCreate_thenThrowsException() {
        val author = Author(id = 1L, fullName = "Test Author", birthDate = LocalDate.now())
        val exception = assertThrows<IllegalArgumentException> {
            authorService.create(author)
        }
        assertEquals("Author id must be 0 to create a new author", exception.message)
    }

    @Test
    fun givenValidAuthor_whenUpdate_thenAuthorIsUpdated() {
        val author = Author(id = 1L, fullName = "Updated Author", birthDate = LocalDate.now())
        authorService.update(author)
        verify(authorRepository, times(1)).save(author)
    }

    @Test
    fun givenZeroIdAuthor_whenUpdate_thenThrowsException() {
        val author = Author(id = 0L, fullName = "Test Author", birthDate = LocalDate.now())
        val exception = assertThrows<IllegalArgumentException> {
            authorService.update(author)
        }
        assertEquals("Author id must not be 0 to update an author", exception.message)
    }
}