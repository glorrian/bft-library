package com.bftcom.backend.repository

import com.bftcom.backend.entity.Reader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
class ReaderRepositoryTest {

    @Autowired
    private lateinit var readerRepository: ReaderRepository

    @Test
    fun givenReader_whenSaveAndFindById_thenReaderIsFound() {
        val reader = Reader(fullName = "John Smith", email = "john.smith@example.com")
        val id = readerRepository.save(reader)
        val savedReader = readerRepository.findById(id)
        assertNotNull(savedReader)
        assertEquals("John Smith", savedReader?.fullName)
        assertEquals("john.smith@example.com", savedReader?.email)
    }

    @Test
    fun givenReader_whenDelete_thenReaderIsDeleted() {
        val reader = Reader(fullName = "Jane Doe", email = "jane.doe@example.com")
        val id = readerRepository.save(reader)
        readerRepository.deleteById(id)
        assertNull(readerRepository.findById(id))
    }

    @Test
    fun givenReader_whenUpdateReader_thenReaderIsUpdated() {
        val reader = Reader(fullName = "Alice Brown", email = "alice.brown@example.com")
        val id = readerRepository.save(reader)
        val updatedReader = reader.copy(id = id, fullName = "Alice Johnson")
        readerRepository.save(updatedReader)
        val foundReader = readerRepository.findById(id)
        assertNotNull(foundReader)
        assertEquals("Alice Johnson", foundReader?.fullName)
    }

    @Test
    fun givenReaders_whenFindAll_thenAllReadersAreFound() {
        val reader1 = Reader(fullName = "Bob White", email = "bob.white@example.com")
        val reader2 = Reader(fullName = "Charlie Green", email = "charlie.green@example.com")
        readerRepository.save(reader1)
        readerRepository.save(reader2)
        val readers = readerRepository.findAll()
        assertTrue(readers.any { it.fullName == "Bob White" })
        assertTrue(readers.any { it.fullName == "Charlie Green" })
    }

    @Test
    fun givenReader_whenExistsById_thenReaderExists() {
        val reader = Reader(fullName = "David Black", email = "david.black@example.com")
        val id = readerRepository.save(reader)
        assertTrue(readerRepository.existsById(id))
        readerRepository.deleteById(id)
        assertFalse(readerRepository.existsById(id))
    }

    @Test
    fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
        val nonExistentId = 9999L
        val result = runCatching { readerRepository.deleteById(nonExistentId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun givenNonExistentId_whenFindById_thenReaderIsNotFound() {
        val nonExistentId = 9999L
        val reader = readerRepository.findById(nonExistentId)
        assertNull(reader)
    }

    @Test
    fun givenNonExistentId_whenExistsById_thenReaderDoesNotExist() {
        val nonExistentId = 9999L
        assertFalse(readerRepository.existsById(nonExistentId))
    }

    @Test
    fun givenDuplicateReaderEmail_whenSave_thenDataIntegrityViolationExceptionThrown() {
        val reader1 = Reader(fullName = "Duplicate Email", email = "duplicate@example.com")
        val reader2 = Reader(fullName = "Duplicate Email", email = "duplicate@example.com")
        readerRepository.save(reader1)
        val exception = assertThrows<DataIntegrityViolationException> {
            readerRepository.save(reader2)
        }
        assertNotNull(exception)
    }
}