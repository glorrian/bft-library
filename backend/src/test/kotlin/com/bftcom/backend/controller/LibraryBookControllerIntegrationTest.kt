package com.bftcom.backend.controller

import com.bftcom.backend.dto.LibraryBookCreateDto
import com.bftcom.backend.dto.LibraryBookUpdateDto
import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.service.LibraryBookService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@Import(LibraryBookControllerIntegrationTest.Config::class)
class LibraryBookControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var libraryBookService: LibraryBookService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun libraryBookService(): LibraryBookService = Mockito.mock(LibraryBookService::class.java)
	}

	@Test
	fun givenLibraryBooks_whenGetAll_thenReturnLibraryBooksList() {
		val libraryBooks = listOf(
			LibraryBook(id = 1, bookId = 101),
			LibraryBook(id = 2, bookId = 102)
		)
		given(libraryBookService.getAllLibraryBooks()).willReturn(libraryBooks)

		mockMvc.perform(get("/library-books"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(libraryBooks.size))
			.andExpect(jsonPath("$[0].id").value(libraryBooks[0].id))
			.andExpect(jsonPath("$[0].bookId").value(libraryBooks[0].bookId))
	}

	@Test
	fun givenLibraryBookId_whenGetLibraryBook_thenReturnLibraryBook() {
		val libraryBook = LibraryBook(id = 1, bookId = 101)
		given(libraryBookService.getLibraryBookById(1)).willReturn(libraryBook)

		mockMvc.perform(get("/library-books/1"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(libraryBook.id))
			.andExpect(jsonPath("$.bookId").value(libraryBook.bookId))
	}

	@Test
	fun givenValidLibraryBookCreateDto_whenCreateLibraryBook_thenReturnCreatedLibraryBook() {
		val dto = LibraryBookCreateDto(bookId = 101)
		val libraryBook = LibraryBook(id = 1, bookId = dto.bookId)
		given(libraryBookService.createLibraryBook(any())).willReturn(libraryBook)

		mockMvc.perform(
			post("/library-books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(libraryBook.id))
			.andExpect(jsonPath("$.bookId").value(libraryBook.bookId))
	}

	@Test
	fun givenExistingLibraryBookIdAndValidUpdateDto_whenUpdateLibraryBook_thenReturnUpdatedLibraryBook() {
		val existingLB = LibraryBook(id = 1, bookId = 101)
		val dto = LibraryBookUpdateDto(bookId = 202)
		val updatedLB = existingLB.copy(bookId = 202)

		given(libraryBookService.getLibraryBookById(1)).willReturn(existingLB)
		given(libraryBookService.updateLibraryBook(updatedLB)).willReturn(updatedLB)

		mockMvc.perform(
			put("/library-books/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(updatedLB.id))
			.andExpect(jsonPath("$.bookId").value(updatedLB.bookId))
	}

	@Test
	fun givenLibraryBookId_whenDeleteLibraryBook_thenStatusOk() {
		mockMvc.perform(delete("/library-books/1"))
			.andExpect(status().isOk)
	}
}
