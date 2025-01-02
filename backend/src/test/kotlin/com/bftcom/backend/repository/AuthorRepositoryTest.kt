package com.bftcom.backend.repository

import com.bftcom.backend.entity.Author
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class AuthorRepositoryTest {

	@Autowired
	private lateinit var authorRepository: AuthorRepositoryDefault

	@Test
	fun givenAuthor_whenSave_thenCanBeFoundById() {
		val author = Author(
			fullName = "John Doe",
			pseudonym = "JD",
			birthDate = LocalDate.of(1980, 1, 1)
		)
		val id = authorRepository.save(author)
		val savedAuthor = authorRepository.findById(id)
		assertNotNull(savedAuthor)
		assertEquals("John Doe", savedAuthor?.fullName)
		assertEquals("JD", savedAuthor?.pseudonym)
		assertEquals(LocalDate.of(1980, 1, 1), savedAuthor?.birthDate)
	}

	@Test
	fun givenAuthor_whenDelete_thenCannotBeFoundById() {
		val author = Author(
			fullName = "Jane Doe",
			pseudonym = "JD",
			birthDate = LocalDate.of(1990, 2, 2)
		)
		val id = authorRepository.save(author)
		authorRepository.deleteById(id)
		assertNull(authorRepository.findById(id))
	}

	@Test
	fun givenAuthor_whenUpdate_thenUpdatedValuesCanBeFound() {
		val author = Author(
			fullName = "Alice Smith",
			pseudonym = "AS",
			birthDate = LocalDate.of(1975, 5, 5)
		)
		val id = authorRepository.save(author)
		val updatedAuthor = author.copy(id = id, fullName = "Alice Johnson")
		authorRepository.save(updatedAuthor)
		val foundAuthor = authorRepository.findById(id)
		assertNotNull(foundAuthor)
		assertEquals("Alice Johnson", foundAuthor?.fullName)
	}

	@Test
	fun givenMultipleAuthors_whenFindAll_thenAllAuthorsAreReturned() {
		val author1 = Author(
			fullName = "Bob Brown",
			pseudonym = "BB",
			birthDate = LocalDate.of(1965, 6, 6)
		)
		val author2 = Author(
			fullName = "Charlie Black",
			pseudonym = "CB",
			birthDate = LocalDate.of(1955, 7, 7)
		)
		authorRepository.save(author1)
		authorRepository.save(author2)
		val authors = authorRepository.findAll()
		assertTrue(authors.any { it.fullName == "Bob Brown" })
		assertTrue(authors.any { it.fullName == "Charlie Black" })
	}

	@Test
	fun givenAuthor_whenExistsById_thenReturnsTrue() {
		val author = Author(
			fullName = "David White",
			pseudonym = "DW",
			birthDate = LocalDate.of(1985, 8, 8)
		)
		val id = authorRepository.save(author)
		assertTrue(authorRepository.existsById(id))
		authorRepository.deleteById(id)
		assertFalse(authorRepository.existsById(id))
	}

	@Test
	fun givenAuthorWithoutPseudonym_whenSave_thenCanBeFoundWithNullPseudonym() {
		val author = Author(
			fullName = "No Pseudonym",
			pseudonym = null,
			birthDate = LocalDate.of(2000, 1, 1)
		)
		val id = authorRepository.save(author)
		val savedAuthor = authorRepository.findById(id)
		assertNotNull(savedAuthor)
		assertEquals("No Pseudonym", savedAuthor?.fullName)
		assertNull(savedAuthor?.pseudonym)
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
	fun givenNonExistentId_whenExistsById_thenReturnsFalse() {
		val nonExistentId = 9999L
		assertFalse(authorRepository.existsById(nonExistentId))
	}
}
