package com.bftcom.backend.repository

import com.bftcom.backend.entity.BookWork
import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.Genre
import com.bftcom.backend.entity.Work
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class BookWorkRepositoryTest {

    @Autowired
    private lateinit var bookWorkRepository: BookWorkRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var workRepository: WorkRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    companion object {
        var genreId: Long = 0
    }

    @Test
    fun givenBookWork_whenSaveAndFindById_thenBookWorkIsFound() {
        val bookWork = createBookWork("Test Book", "1234567890", "Test Work")
        val id = bookWorkRepository.save(bookWork)
        val savedBookWork = bookWorkRepository.findById(id)
        assertNotNull(savedBookWork)
        assertEquals(bookWork.bookId, savedBookWork?.bookId)
        assertEquals(bookWork.workId, savedBookWork?.workId)
    }

    @Test
    fun givenBookWork_whenDelete_thenBookWorkIsDeleted() {
        val bookWork = createBookWork("Test Book", "1234567895", "Test Work")
        val id = bookWorkRepository.save(bookWork)
        bookWorkRepository.deleteById(id)
        assertNull(bookWorkRepository.findById(id))
    }

    @Test
    fun givenBookWork_whenUpdateBookWork_thenBookWorkIsUpdated() {
        val bookWork = createBookWork("Test Book", "1234567898", "Test Work")
        val id = bookWorkRepository.save(bookWork)
        val updatedBookWork = bookWork.copy(id = id, workId = 2)
        bookWorkRepository.save(updatedBookWork)
        val foundBookWork = bookWorkRepository.findById(id)
        assertNotNull(foundBookWork)
        assertEquals(2, foundBookWork?.workId)
    }

    @Test
    fun givenBookWorks_whenFindAll_thenAllBookWorksAreFound() {
        val bookWork1 = createBookWork("Test Book 1", "1234567891", "Test Work 1")
        val bookWork2 = createBookWork("Test Book 2", "1234567892", "Test Work 2")
        bookWorkRepository.save(bookWork1)
        bookWorkRepository.save(bookWork2)
        val bookWorks = bookWorkRepository.findAll()
        assertTrue(bookWorks.any { it.bookId == bookWork1.bookId && it.workId == bookWork1.workId })
        assertTrue(bookWorks.any { it.bookId == bookWork2.bookId && it.workId == bookWork2.workId })
    }

    @Test
    fun givenBookWork_whenExistsById_thenBookWorkExists() {
        val bookWork = createBookWork("Test Book", "1234567897", "Test Work")
        val id = bookWorkRepository.save(bookWork)
        assertTrue(bookWorkRepository.existsById(id))
        bookWorkRepository.deleteById(id)
        assertFalse(bookWorkRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { bookWorkRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenBookWorkIsNotFound() {
        val nonExistentId = 9999L
        val bookWork = bookWorkRepository.findById(nonExistentId)
        assertNull(bookWork)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenBookWorkDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(bookWorkRepository.existsById(nonExistentId))
    }

    private fun createBookWork(bookTitle: String, isbn: String, workTitle: String): BookWork {
        if (genreId == 0L) {
            synchronized(this) {
                if (genreId == 0L) {
                    val genre = Genre(name = "Test Genre")
                    genreId = genreRepository.save(genre)
                }
            }
        }
        val book = Book(title = bookTitle, isbn = isbn, publicationDate = LocalDate.now())
        val work = Work(title = workTitle, genreId = genreId)
        return BookWork(bookId = bookRepository.save(book), workId = workRepository.save(work))
    }
}