package com.bftcom.backend.repository

import com.bftcom.backend.entity.Author
import com.bftcom.backend.entity.Genre
import com.bftcom.backend.entity.Work
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class WorkRepositoryTest {

	@Autowired
	private lateinit var workRepository: WorkRepository

	@Autowired
	private lateinit var genreRepository: GenreRepository

	@Autowired
	private lateinit var authorRepository: AuthorRepository

	private var genreId: Long = 0

	companion object {
		private var genre: Genre? = null

		@Synchronized
		fun createGenre(genreRepository: GenreRepository, name: String): Genre {
			if (genre == null) {
				genre = genreRepository.create(Genre(name = name))
			}
			return genre!!
		}
	}

	@BeforeEach
	fun setup() {
		genreId = createGenre(genreRepository, "Test Genre").id!!
	}

	@Test
	fun givenWork_whenSave_thenCanBeFoundById() {
		val work = Work(title = "Test Work", genreId = genreId, authorsIds = listOf())
		val savedWork = workRepository.create(work)
		val foundWork = workRepository.findByIdEager(savedWork.id!!)
		assertNotNull(foundWork)
		assertEquals("Test Work", foundWork?.title)
	}

	@Test
	fun givenWork_whenDelete_thenCannotBeFoundById() {
		val work = Work(title = "Test Work", genreId = genreId, authorsIds = listOf())
		val savedWork = workRepository.create(work)
		workRepository.deleteById(savedWork.id!!)
		val foundWork = workRepository.findByIdEager(savedWork.id!!)
		assertNull(foundWork)
	}

	@Test
	fun givenWork_whenUpdate_thenUpdatedValuesCanBeFound() {
		val work = Work(title = "Test Work", genreId = genreId, authorsIds = listOf())
		val savedWork = workRepository.create(work)
		val updatedWork = savedWork.copy(title = "Updated Work")
		workRepository.update(updatedWork)
		val foundWork = workRepository.findByIdEager(savedWork.id!!)
		assertNotNull(foundWork)
		assertEquals("Updated Work", foundWork?.title)
	}

	@Test
	fun givenMultipleWorks_whenFindAll_thenAllWorksAreReturned() {
		val work1 = Work(title = "Work One", genreId = genreId, authorsIds = listOf())
		val work2 = Work(title = "Work Two", genreId = genreId, authorsIds = listOf())
		workRepository.create(work1)
		workRepository.create(work2)
		val works = workRepository.findAllEager()
		assertTrue(works.any { it.title == "Work One" })
		assertTrue(works.any { it.title == "Work Two" })
	}

	@Test
	fun givenWorkWithAuthors_whenSave_thenAuthorsAreLinked() {
		val author1 = authorRepository.create(
			Author(
				fullName = "Author One",
				pseudonym = "A1",
				birthDate = LocalDate.of(1970, 1, 1)
			)
		)
		val author2 = authorRepository.create(
			Author(
				fullName = "Author Two",
				pseudonym = "A2",
				birthDate = LocalDate.of(1975, 2, 2)
			)
		)
		val work = Work(title = "Work with Authors", genreId = genreId, authorsIds = listOf(author1.id!!, author2.id!!))
		val savedWork = workRepository.create(work)
		val foundWork = workRepository.findByIdEager(savedWork.id!!)
		assertNotNull(foundWork)
		assertEquals(2, foundWork?.authorsIds?.size)
		assertTrue(foundWork?.authorsIds?.contains(author1.id!!) == true)
		assertTrue(foundWork?.authorsIds?.contains(author2.id!!) == true)
	}

	@Test
	fun givenWorkWithInvalidTitle_whenSave_thenThrowsException() {
		val work = Work(title = "", genreId = genreId, authorsIds = listOf())
		val exception = assertThrows<Exception> {
			workRepository.create(work)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenWorkWithLongTitle_whenSave_thenThrowsException() {
		val longTitle = "a".repeat(256)
		val work = Work(title = longTitle, genreId = genreId, authorsIds = listOf())
		val exception = assertThrows<Exception> {
			workRepository.create(work)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenDuplicateGenre_whenSave_thenThrowsException() {
		val exception = assertThrows<Exception> {
			genreRepository.create(Genre(name = "Test Genre"))
		}
		assertNotNull(exception)
	}
}