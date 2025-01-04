package com.bftcom.backend.service

import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.repository.LibraryBookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class LibraryBookServiceTest {

    @Mock
    private lateinit var libraryBookRepository: LibraryBookRepository

    @InjectMocks
    private lateinit var libraryBookService: LibraryBookService

    private lateinit var libraryBook: LibraryBook

    @BeforeEach
    fun setup() {
        libraryBook = LibraryBook(id = 1L, bookId = 1L)
    }

    @Test
    fun givenLibraryBook_whenCreate_thenLibraryBookIsCreated() {
        `when`(libraryBookRepository.create(libraryBook)).thenReturn(libraryBook)
        val createdLibraryBook = libraryBookService.createLibraryBook(libraryBook)
        assertNotNull(createdLibraryBook)
        assertEquals(1L, createdLibraryBook.bookId)
    }

    @Test
    fun givenLibraryBook_whenUpdate_thenLibraryBookIsUpdated() {
        `when`(libraryBookRepository.update(libraryBook)).thenReturn(libraryBook)
        val updatedLibraryBook = libraryBookService.updateLibraryBook(libraryBook)
        assertNotNull(updatedLibraryBook)
        assertEquals(1L, updatedLibraryBook.bookId)
    }

    @Test
    fun givenLibraryBookId_whenDelete_thenLibraryBookIsDeleted() {
        doNothing().`when`(libraryBookRepository).deleteById(libraryBook.id!!)
        libraryBookService.deleteLibraryBook(libraryBook.id!!)
        verify(libraryBookRepository, times(1)).deleteById(libraryBook.id!!)
    }

    @Test
    fun givenLibraryBookId_whenGetById_thenLibraryBookIsReturned() {
        `when`(libraryBookRepository.findById(libraryBook.id!!)).thenReturn(libraryBook)
        val foundLibraryBook = libraryBookService.getLibraryBookById(libraryBook.id!!)
        assertNotNull(foundLibraryBook)
        assertEquals(1L, foundLibraryBook?.bookId)
    }

    @Test
    fun whenGetAllLibraryBooks_thenAllLibraryBooksAreReturned() {
        val libraryBooks = listOf(libraryBook)
        `when`(libraryBookRepository.findAll()).thenReturn(libraryBooks)
        val foundLibraryBooks = libraryBookService.getAllLibraryBooks()
        assertNotNull(foundLibraryBooks)
        assertEquals(1, foundLibraryBooks.size)
        assertEquals(1L, foundLibraryBooks[0].bookId)
    }
}