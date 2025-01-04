package com.bftcom.backend.repository

import com.bftcom.backend.entity.Author
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class AuthorRepositoryTest {

	@Autowired
	private lateinit var authorRepository: AuthorRepository

	@Test
	fun givenAuthor_whenSave_thenCanBeFoundById() {
		val author = Author(fullName = "John Doe", pseudonym = "JD", birthDate = LocalDate.of(1980, 1, 1))
		val savedAuthor = authorRepository.create(author)
		val foundAuthor = authorRepository.findById(savedAuthor.id!!)
		assertNotNull(foundAuthor)
		assertEquals("John Doe", foundAuthor?.fullName)
	}

	@Test
	fun givenAuthor_whenDelete_thenCannotBeFoundById() {
		val author = Author(fullName = "Jane Doe", pseudonym = "JD", birthDate = LocalDate.of(1985, 5, 5))
		val savedAuthor = authorRepository.create(author)
		authorRepository.deleteById(savedAuthor.id!!)
		val foundAuthor = authorRepository.findById(savedAuthor.id!!)
		assertNull(foundAuthor)
	}

	@Test
	fun givenAuthor_whenUpdate_thenUpdatedValuesCanBeFound() {
		val author = Author(fullName = "Alice Smith", pseudonym = "AS", birthDate = LocalDate.of(1990, 3, 3))
		val savedAuthor = authorRepository.create(author)
		val updatedAuthor = savedAuthor.copy(fullName = "Alice Johnson")
		authorRepository.update(updatedAuthor)
		val foundAuthor = authorRepository.findById(savedAuthor.id!!)
		assertNotNull(foundAuthor)
		assertEquals("Alice Johnson", foundAuthor?.fullName)
	}

	@Test
	fun givenMultipleAuthors_whenFindAll_thenAllAuthorsAreReturned() {
		val author1 = Author(fullName = "Author One", pseudonym = "A1", birthDate = LocalDate.of(1970, 7, 7))
		val author2 = Author(fullName = "Author Two", pseudonym = "A2", birthDate = LocalDate.of(1975, 8, 8))
		authorRepository.create(author1)
		authorRepository.create(author2)
		val authors = authorRepository.findAll()
		assertTrue(authors.any { it.fullName == "Author One" })
		assertTrue(authors.any { it.fullName == "Author Two" })
	}

	@Test
	fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
		val nonExistentId = 9999L
		val result = runCatching { authorRepository.deleteById(nonExistentId) }
		assertTrue(result.isSuccess)
	}

	@Test
	fun givenNonExistentId_whenFindById_thenReturnsNull() {
		val nonExistentId = 9999L
		val author = authorRepository.findById(nonExistentId)
		assertNull(author)
	}

	@Test
	fun givenAuthorWithEmptyFullName_whenSave_thenThrowsException() {
		val author = Author(fullName = "", pseudonym = "JD", birthDate = LocalDate.of(1980, 1, 1))
		val exception = assertThrows<Exception> {
			authorRepository.create(author)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenAuthorWithLongFullName_whenSave_thenThrowsException() {
		val longFullName = "a".repeat(256)
		val author = Author(fullName = longFullName, pseudonym = "JD", birthDate = LocalDate.of(1980, 1, 1))
		val exception = assertThrows<Exception> {
			authorRepository.create(author)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenAuthorWithEmptyPseudonym_whenSave_thenThrowsException() {
		val author = Author(fullName = "John Doe", pseudonym = "", birthDate = LocalDate.of(1980, 1, 1))
		val exception = assertThrows<Exception> {
			authorRepository.create(author)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenAuthorWithLongPseudonym_whenSave_thenThrowsException() {
		val longPseudonym = "a".repeat(256)
		val author = Author(fullName = "John Doe", pseudonym = longPseudonym, birthDate = LocalDate.of(1980, 1, 1))
		val exception = assertThrows<Exception> {
			authorRepository.create(author)
		}
		assertNotNull(exception)
	}
}