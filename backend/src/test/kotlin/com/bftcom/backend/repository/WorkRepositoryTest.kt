package com.bftcom.backend.repository

import com.bftcom.backend.entity.Genre
import com.bftcom.backend.entity.Work
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WorkRepositoryTest {

    @Autowired
    private lateinit var workRepository: WorkRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Test
    fun givenWork_whenSaveAndFindById_thenWorkIsFound() {
        val genre = Genre(name = "Fiction")
        val genreId = genreRepository.save(genre)
        val work = Work(title = "Test Work", genreId = genreId.toInt())
        val id = workRepository.save(work)
        val savedWork = workRepository.findById(id)
        assertNotNull(savedWork)
        assertEquals("Test Work", savedWork?.title)
        assertEquals(genreId.toInt(), savedWork?.genreId)
    }

    @Test
    fun givenWork_whenDelete_thenWorkIsDeleted() {
        val genre = Genre(name = "Non-Fiction")
        val genreId = genreRepository.save(genre)
        val work = Work(title = "Delete Work", genreId = genreId.toInt())
        val id = workRepository.save(work)
        workRepository.deleteById(id)
        assertNull(workRepository.findById(id))
    }

    @Test
    fun givenWork_whenUpdateWork_thenWorkIsUpdated() {
        val genre = Genre(name = "Science")
        val genreId = genreRepository.save(genre)
        val work = Work(title = "Original Title", genreId = genreId.toInt())
        val id = workRepository.save(work)
        val updatedWork = work.copy(id = id, title = "Updated Title")
        workRepository.save(updatedWork)
        val foundWork = workRepository.findById(id)
        assertNotNull(foundWork)
        assertEquals("Updated Title", foundWork?.title)
    }

    @Test
    fun givenWorks_whenFindAll_thenAllWorksAreFound() {
        val genre = Genre(name = "History")
        val genreId = genreRepository.save(genre)
        val work1 = Work(title = "Work One", genreId = genreId.toInt())
        val work2 = Work(title = "Work Two", genreId = genreId.toInt())
        workRepository.save(work1)
        workRepository.save(work2)
        val works = workRepository.findAll()
        assertTrue(works.any { it.title == "Work One" })
        assertTrue(works.any { it.title == "Work Two" })
    }

    @Test
    fun givenWork_whenExistsById_thenWorkExists() {
        val genre = Genre(name = "Adventure")
        val genreId = genreRepository.save(genre)
        val work = Work(title = "Existence Check", genreId = genreId.toInt())
        val id = workRepository.save(work)
        assertTrue(workRepository.existsById(id))
        workRepository.deleteById(id)
        assertFalse(workRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { workRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenWorkIsNotFound() {
        val nonExistentId = 9999L
        val work = workRepository.findById(nonExistentId)
        assertNull(work)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenWorkDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(workRepository.existsById(nonExistentId))
    }
}