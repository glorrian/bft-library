package com.bftcom.backend.repository

import com.bftcom.backend.entity.WorkAuthor
import com.bftcom.backend.entity.Work
import com.bftcom.backend.entity.Author
import com.bftcom.backend.entity.Genre
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class WorkAuthorRepositoryTest {

	@Autowired
	private lateinit var workAuthorRepository: WorkAuthorRepository

	@Autowired
	private lateinit var workRepository: WorkRepository

	@Autowired
	private lateinit var authorRepository: AuthorRepository

	@Autowired
	private lateinit var genreRepository: GenreRepository

	companion object {
		private var genreId: Long = 0L
	}

	@Test
	fun givenWorkAuthor_whenSaveAndFindById_thenWorkAuthorIsFound() {
		val workAuthor = createWorkAuthor("Test Work 1", "Test Author 1")
		val id = workAuthorRepository.save(workAuthor)
		val savedWorkAuthor = workAuthorRepository.findById(id)
		assertNotNull(savedWorkAuthor)
		assertEquals(workAuthor.workId, savedWorkAuthor?.workId)
		assertEquals(workAuthor.authorId, savedWorkAuthor?.authorId)
	}

	@Test
	fun givenWorkAuthor_whenDelete_thenWorkAuthorIsDeleted() {
		val workAuthor = createWorkAuthor("Test Work 2", "Test Author 2")
		val id = workAuthorRepository.save(workAuthor)
		workAuthorRepository.deleteById(id)
		assertNull(workAuthorRepository.findById(id))
	}

	@Test
	fun givenWorkAuthor_whenUpdateWorkAuthor_thenWorkAuthorIsUpdated() {
		val workAuthor = createWorkAuthor("Test Work 3", "Test Author 3")
		val id = workAuthorRepository.save(workAuthor)
		val updatedWorkAuthor = createWorkAuthor("Updated Work 3", "Updated Author 3").copy(id = id)
		workAuthorRepository.save(updatedWorkAuthor)
		val foundWorkAuthor = workAuthorRepository.findById(id)
		assertNotNull(foundWorkAuthor)
		assertEquals(updatedWorkAuthor.workId, foundWorkAuthor?.workId)
		assertEquals(updatedWorkAuthor.authorId, foundWorkAuthor?.authorId)
	}

	@Test
	fun givenWorkAuthors_whenFindAll_thenAllWorkAuthorsAreFound() {
		var workAuthor1 = createWorkAuthor("Test Work 4", "Test Author 4")
		var workAuthor2 = createWorkAuthor("Test Work 5", "Test Author 5")
		workAuthor1 = workAuthor1.copy(id = workAuthorRepository.save(workAuthor1))
		workAuthor2 = workAuthor2.copy(id = workAuthorRepository.save(workAuthor2))
		val workAuthors = workAuthorRepository.findAll()

		assertTrue(workAuthors.contains(workAuthor1))
		assertTrue(workAuthors.contains(workAuthor2))
	}

	@Test
	fun givenWorkAuthor_whenExistsById_thenWorkAuthorExists() {
		val workAuthor = createWorkAuthor("Test Work 6", "Test Author 6")
		val id = workAuthorRepository.save(workAuthor)
		assertTrue(workAuthorRepository.existsById(id))
		workAuthorRepository.deleteById(id)
		assertFalse(workAuthorRepository.existsById(id))
	}

	@Test
	fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
		val nonExistentId = 9999L
		val result = runCatching { workAuthorRepository.deleteById(nonExistentId) }
		assertTrue(result.isSuccess)
	}

	@Test
	fun givenNonExistentId_whenFindById_thenWorkAuthorIsNotFound() {
		val nonExistentId = 9999L
		val workAuthor = workAuthorRepository.findById(nonExistentId)
		assertNull(workAuthor)
	}

	@Test
	fun givenNonExistentId_whenExistsById_thenWorkAuthorDoesNotExist() {
		val nonExistentId = 9999L
		assertFalse(workAuthorRepository.existsById(nonExistentId))
	}

	private fun createWorkAuthor(title: String, fullName: String): WorkAuthor {
		if (genreId == 0L) {
			synchronized(this) {
				if (genreId == 0L) {
					val genre = Genre(name = "Science Fiction")
					genreId = genreRepository.save(genre)
				}
			}
		}
		val work = Work(title = title, genreId = genreId)
		val workId = workRepository.save(work)
		val author = Author(fullName = fullName, birthDate = LocalDate.of(1990, 3, 3))
		val authorId = authorRepository.save(author)
		return WorkAuthor(workId = workId, authorId = authorId)
	}
}
