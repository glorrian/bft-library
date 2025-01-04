package com.bftcom.backend.service

import com.bftcom.backend.entity.Book
import com.bftcom.backend.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class BookServiceTest {

    @Mock
    private lateinit var bookRepository: BookRepository

    @InjectMocks
    private lateinit var bookService: BookService

    private lateinit var book: Book

    @BeforeEach
    fun setup() {
        book = Book(id = 1L, title = "Test Book", isbn = "1234567890123", publicationDate = LocalDate.of(2020, 1, 1))
    }

    @Test
    fun givenBook_whenCreate_thenBookIsCreated() {
        `when`(bookRepository.create(book)).thenReturn(book)
        val createdBook = bookService.createBook(book)
        assertNotNull(createdBook)
        assertEquals("Test Book", createdBook.title)
    }

    @Test
    fun givenBook_whenUpdate_thenBookIsUpdated() {
        `when`(bookRepository.update(book)).thenReturn(book)
        val updatedBook = bookService.updateBook(book)
        assertNotNull(updatedBook)
        assertEquals("Test Book", updatedBook.title)
    }

    @Test
    fun givenBookId_whenDelete_thenBookIsDeleted() {
        doNothing().`when`(bookRepository).deleteById(book.id!!)
        bookService.deleteBook(book.id!!)
        verify(bookRepository, times(1)).deleteById(book.id!!)
    }

    @Test
    fun givenBookId_whenGetById_thenBookIsReturned() {
        `when`(bookRepository.findById(book.id!!)).thenReturn(book)
        val foundBook = bookService.getBookById(book.id!!)
        assertNotNull(foundBook)
        assertEquals("Test Book", foundBook?.title)
    }

    @Test
    fun whenGetAllBooks_thenAllBooksAreReturned() {
        val books = listOf(book)
        `when`(bookRepository.findAll()).thenReturn(books)
        val foundBooks = bookService.getAllBooks()
        assertNotNull(foundBooks)
        assertEquals(1, foundBooks.size)
        assertEquals("Test Book", foundBooks[0].title)
    }

    @Test
    fun whenGetAllBooksEager_thenAllBooksAreReturned() {
        val books = listOf(book)
        `when`(bookRepository.findAllEager()).thenReturn(books)
        val foundBooks = bookService.getAllBooks(eager = true)
        assertNotNull(foundBooks)
        assertEquals(1, foundBooks.size)
        assertEquals("Test Book", foundBooks[0].title)
    }

    @Test
    fun givenBookId_whenGetByIdEager_thenBookIsReturned() {
        `when`(bookRepository.findByIdEager(book.id!!)).thenReturn(book)
        val foundBook = bookService.getBookById(book.id!!, eager = true)
        assertNotNull(foundBook)
        assertEquals("Test Book", foundBook?.title)
    }
}