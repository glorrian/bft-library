package com.bftcom.backend.service

import com.bftcom.backend.entity.Author
import com.bftcom.backend.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class AuthorServiceTest {

	@Mock
	private lateinit var authorRepository: AuthorRepository

	@InjectMocks
	private lateinit var authorService: AuthorService

	private lateinit var author: Author

	@BeforeEach
	fun setup() {
		author =
			Author(id = 1L, fullName = "Test Author", pseudonym = "T. Author", birthDate = LocalDate.of(1980, 1, 1))
	}

	@Test
	fun givenAuthor_whenCreate_thenAuthorIsCreated() {
		`when`(authorRepository.create(author)).thenReturn(author)
		val createdAuthor = authorService.createAuthor(author)
		assertNotNull(createdAuthor)
		assertEquals("Test Author", createdAuthor.fullName)
	}

	@Test
	fun givenAuthor_whenUpdate_thenAuthorIsUpdated() {
		`when`(authorRepository.update(author)).thenReturn(author)
		val updatedAuthor = authorService.updateAuthor(author)
		assertNotNull(updatedAuthor)
		assertEquals("Test Author", updatedAuthor.fullName)
	}

	@Test
	fun givenAuthorId_whenDelete_thenAuthorIsDeleted() {
		doNothing().`when`(authorRepository).deleteById(author.id!!)
		authorService.deleteAuthor(author.id!!)
		verify(authorRepository, times(1)).deleteById(author.id!!)
	}

	@Test
	fun givenAuthorId_whenGetById_thenAuthorIsReturned() {
		`when`(authorRepository.findById(author.id!!)).thenReturn(author)
		val foundAuthor = authorService.getAuthorById(author.id!!)
		assertNotNull(foundAuthor)
		assertEquals("Test Author", foundAuthor?.fullName)
	}

	@Test
	fun whenGetAllAuthors_thenAllAuthorsAreReturned() {
		val authors = listOf(author)
		`when`(authorRepository.findAll()).thenReturn(authors)
		val foundAuthors = authorService.getAllAuthors()
		assertNotNull(foundAuthors)
		assertEquals(1, foundAuthors.size)
		assertEquals("Test Author", foundAuthors[0].fullName)
	}
}