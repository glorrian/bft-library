package com.bftcom.backend.repository

import com.bftcom.backend.entity.Reader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ReaderRepositoryTest {

	@Autowired
	private lateinit var readerRepository: ReaderRepository

	@Test
	fun givenReader_whenSave_thenCanBeFoundById() {
		val reader = Reader(fullName = "John Doe", email = "john.doe@example.com")
		val savedReader = readerRepository.create(reader)
		val foundReader = readerRepository.findById(savedReader.id!!)
		assertNotNull(foundReader)
		assertEquals("John Doe", foundReader?.fullName)
	}

	@Test
	fun givenReader_whenDelete_thenCannotBeFoundById() {
		val reader = Reader(fullName = "Jane Doe", email = "jane.doe@example.com")
		val savedReader = readerRepository.create(reader)
		readerRepository.deleteById(savedReader.id!!)
		val foundReader = readerRepository.findById(savedReader.id!!)
		assertNull(foundReader)
	}

	@Test
	fun givenReader_whenUpdate_thenUpdatedValuesCanBeFound() {
		val reader = Reader(fullName = "Alice Smith", email = "alice.smith@example.com")
		val savedReader = readerRepository.create(reader)
		val updatedReader = savedReader.copy(fullName = "Alice Johnson")
		readerRepository.update(updatedReader)
		val foundReader = readerRepository.findById(savedReader.id!!)
		assertNotNull(foundReader)
		assertEquals("Alice Johnson", foundReader?.fullName)
	}

	@Test
	fun givenMultipleReaders_whenFindAll_thenAllReadersAreReturned() {
		val reader1 = Reader(fullName = "Reader One", email = "reader.one@example.com")
		val reader2 = Reader(fullName = "Reader Two", email = "reader.two@example.com")
		readerRepository.create(reader1)
		readerRepository.create(reader2)
		val readers = readerRepository.findAll()
		assertTrue(readers.any { it.fullName == "Reader One" })
		assertTrue(readers.any { it.fullName == "Reader Two" })
	}

	@Test
	fun givenNonExistentId_whenDelete_thenNoExceptionThrown() {
		val nonExistentId = 9999L
		val result = runCatching { readerRepository.deleteById(nonExistentId) }
		assertTrue(result.isSuccess)
	}

	@Test
	fun givenNonExistentId_whenFindById_thenReturnsNull() {
		val nonExistentId = 9999L
		val reader = readerRepository.findById(nonExistentId)
		assertNull(reader)
	}

	@Test
	fun givenReaderWithEmptyFullName_whenSave_thenThrowsException() {
		val reader = Reader(fullName = "", email = "john.doe@example.com")
		val exception = assertThrows<Exception> {
			readerRepository.create(reader)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenReaderWithLongFullName_whenSave_thenThrowsException() {
		val longFullName = "a".repeat(256)
		val reader = Reader(fullName = longFullName, email = "john.doe@example.com")
		val exception = assertThrows<Exception> {
			readerRepository.create(reader)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenReaderWithEmptyEmail_whenSave_thenThrowsException() {
		val reader = Reader(fullName = "John Doe", email = "")
		val exception = assertThrows<Exception> {
			readerRepository.create(reader)
		}
		assertNotNull(exception)
	}

	@Test
	fun givenReaderWithLongEmail_whenSave_thenThrowsException() {
		val longEmail = "a".repeat(256)
		val reader = Reader(fullName = "John Doe", email = longEmail)
		val exception = assertThrows<Exception> {
			readerRepository.create(reader)
		}
		assertNotNull(exception)
	}
}