package com.bftcom.backend.service

import com.bftcom.backend.entity.Work
import com.bftcom.backend.repository.WorkRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class WorkServiceTest {

    @Mock
    private lateinit var workRepository: WorkRepository

    @InjectMocks
    private lateinit var workService: WorkService

    private lateinit var work: Work

    @BeforeEach
    fun setup() {
        work = Work(id = 1L, title = "Test Work", genreId = 1L, authorsIds = listOf(1L, 2L))
    }

    @Test
    fun givenEagerFalse_whenGetAllWorks_thenFindAllCalled() {
        whenever(workRepository.findAll()).thenReturn(listOf(work))
        val works = workService.getAllWorks(false)
        assertNotNull(works)
        assertEquals(1, works.size)
        verify(workRepository, times(1)).findAll()
        verify(workRepository, never()).findAllEager()
    }

    @Test
    fun givenEagerTrue_whenGetAllWorks_thenFindAllEagerCalled() {
        whenever(workRepository.findAllEager()).thenReturn(listOf(work))
        val works = workService.getAllWorks(true)
        assertNotNull(works)
        assertEquals(1, works.size)
        verify(workRepository, times(1)).findAllEager()
        verify(workRepository, never()).findAll()
    }

    @Test
    fun givenEagerFalse_whenGetWorkById_thenFindByIdCalled() {
        whenever(workRepository.findById(1L)).thenReturn(work)
        val foundWork = workService.getWorkById(1L, false)
        assertNotNull(foundWork)
        assertEquals("Test Work", foundWork?.title)
        verify(workRepository, times(1)).findById(1L)
        verify(workRepository, never()).findByIdEager(1L)
    }

    @Test
    fun givenEagerTrue_whenGetWorkById_thenFindByIdEagerCalled() {
        whenever(workRepository.findByIdEager(1L)).thenReturn(work)
        val foundWork = workService.getWorkById(1L, true)
        assertNotNull(foundWork)
        assertEquals("Test Work", foundWork?.title)
        verify(workRepository, times(1)).findByIdEager(1L)
        verify(workRepository, never()).findById(1L)
    }

    @Test
    fun givenWork_whenCreateWork_thenWorkIsCreated() {
        whenever(workRepository.create(work)).thenReturn(work)
        val createdWork = workService.createWork(work)
        assertNotNull(createdWork)
        assertEquals("Test Work", createdWork.title)
        verify(workRepository, times(1)).create(work)
    }

    @Test
    fun givenWork_whenUpdateWork_thenWorkIsUpdated() {
        whenever(workRepository.update(work)).thenReturn(work)
        val updatedWork = workService.updateWork(work)
        assertNotNull(updatedWork)
        assertEquals("Test Work", updatedWork.title)
        verify(workRepository, times(1)).update(work)
    }

    @Test
    fun givenWorkId_whenDeleteWork_thenWorkIsDeleted() {
        doNothing().whenever(workRepository).deleteById(work.id!!)
        workService.deleteWork(work.id!!)
        verify(workRepository, times(1)).deleteById(work.id!!)
    }
}