package com.bftcom.backend.service

import com.bftcom.backend.entity.Reader
import com.bftcom.backend.repository.ReaderRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class ReaderServiceTest {

    @Mock
    private lateinit var readerRepository: ReaderRepository

    @InjectMocks
    private lateinit var readerService: ReaderService

    private lateinit var reader: Reader

    @BeforeEach
    fun setup() {
        reader = Reader(id = 1L, fullName = "John Doe", email = "john.doe@example.com")
    }

    @Test
    fun givenReader_whenCreate_thenReaderIsCreated() {
        `when`(readerRepository.create(reader)).thenReturn(reader)
        val createdReader = readerService.createReader(reader)
        assertNotNull(createdReader)
        assertEquals("John Doe", createdReader.fullName)
    }

    @Test
    fun givenReader_whenUpdate_thenReaderIsUpdated() {
        `when`(readerRepository.update(reader)).thenReturn(reader)
        val updatedReader = readerService.updateReader(reader)
        assertNotNull(updatedReader)
        assertEquals("John Doe", updatedReader.fullName)
    }

    @Test
    fun givenReaderId_whenDelete_thenReaderIsDeleted() {
        doNothing().`when`(readerRepository).deleteById(reader.id!!)
        readerService.deleteReader(reader.id!!)
        verify(readerRepository, times(1)).deleteById(reader.id!!)
    }

    @Test
    fun givenReaderId_whenGetById_thenReaderIsReturned() {
        `when`(readerRepository.findById(reader.id!!)).thenReturn(reader)
        val foundReader = readerService.getReaderById(reader.id!!)
        assertNotNull(foundReader)
        assertEquals("John Doe", foundReader?.fullName)
    }

    @Test
    fun whenGetAllReaders_thenAllReadersAreReturned() {
        val readers = listOf(reader)
        `when`(readerRepository.findAll()).thenReturn(readers)
        val foundReaders = readerService.getAllReaders()
        assertNotNull(foundReaders)
        assertEquals(1, foundReaders.size)
        assertEquals("John Doe", foundReaders[0].fullName)
    }
}