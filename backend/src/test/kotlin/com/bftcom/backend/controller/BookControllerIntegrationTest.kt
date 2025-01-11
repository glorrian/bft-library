package com.bftcom.backend.controller

import com.bftcom.backend.dto.BookCreateDto
import com.bftcom.backend.dto.BookUpdateDto
import com.bftcom.backend.entity.Book
import com.bftcom.backend.service.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
@AutoConfigureMockMvc
@Import(BookControllerIntegrationTest.Config::class)
class BookControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var bookService: BookService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun bookService(): BookService = Mockito.mock(BookService::class.java)
	}

	@Test
	fun givenBooks_whenGetAll_thenReturnBooksList() {
		val books = listOf(
			Book(id = 1, title = "Book One", isbn = "ISBN1", publicationDate = LocalDate.of(2020, 1, 1), worksIds = emptyList()),
			Book(id = 2, title = "Book Two", isbn = "ISBN2", publicationDate = LocalDate.of(2021, 5, 15), worksIds = emptyList())
		)
		given(bookService.getAllBooks(false)).willReturn(books)

		mockMvc.perform(get("/books").param("eager", "false"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(books.size))
			.andExpect(jsonPath("$[0].id").value(books[0].id))
			.andExpect(jsonPath("$[0].title").value(books[0].title))
	}

	@Test
	fun givenBookId_whenGetBook_thenReturnBook() {
		val book = Book(id = 1, title = "Book One", isbn = "ISBN1", publicationDate = LocalDate.of(2020, 1, 1), worksIds = emptyList())
		given(bookService.getBookById(1, false)).willReturn(book)

		mockMvc.perform(get("/books/1").param("eager", "false"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(book.id))
			.andExpect(jsonPath("$.title").value(book.title))
	}

	@Test
	fun givenValidBookCreateDto_whenCreateBook_thenReturnCreatedBook() {
		val dto = BookCreateDto(
			title = "New Book",
			isbn = "ISBNNEW",
			publicationDate = LocalDate.of(2022, 1, 1),
			workIds = emptyList()
		)
		val book = Book(id = 1, title = dto.title, isbn = dto.isbn, publicationDate = dto.publicationDate, worksIds = dto.workIds)
		given(bookService.createBook(any())).willReturn(book)

		mockMvc.perform(
			post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(book.id))
			.andExpect(jsonPath("$.title").value(book.title))
	}

	@Test
	fun givenExistingBookIdAndValidUpdateDto_whenUpdateBook_thenReturnUpdatedBook() {
		val existingBook = Book(id = 1, title = "Old Title", isbn = "ISBNOLD", publicationDate = LocalDate.of(2020, 1, 1), worksIds = emptyList())
		val dto = BookUpdateDto(
			title = "New Title",
			isbn = null,
			publicationDate = null,
			workIds = null
		)
		val updatedBook = existingBook.copy(title = "New Title")

		given(bookService.getBookById(1, false)).willReturn(existingBook)
		given(bookService.updateBook(updatedBook)).willReturn(updatedBook)

		mockMvc.perform(
			put("/books/1")
				.param("eager", "false")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(updatedBook.id))
			.andExpect(jsonPath("$.title").value("New Title"))
	}

	@Test
	fun givenBookId_whenDeleteBook_thenStatusOk() {
		mockMvc.perform(delete("/books/1"))
			.andExpect(status().isOk)
	}
}
