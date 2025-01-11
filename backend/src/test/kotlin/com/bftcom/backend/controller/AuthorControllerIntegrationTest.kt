package com.bftcom.backend.controller

import com.bftcom.backend.dto.AuthorCreateDto
import com.bftcom.backend.dto.AuthorUpdateDto
import com.bftcom.backend.entity.Author
import com.bftcom.backend.service.AuthorService
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
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthorControllerIntegrationTest.Config::class)
class AuthorControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var authorService: AuthorService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun authorService(): AuthorService = Mockito.mock(AuthorService::class.java)
	}

	@Test
	fun givenAuthors_whenGetAll_thenReturnAuthorsList() {
		val authors = listOf(
			Author(id = 1, fullName = "John Doe", pseudonym = "JD", birthDate = LocalDate.of(1990, 1, 1)),
			Author(id = 2, fullName = "Jane Smith", pseudonym = "JS", birthDate = LocalDate.of(1985, 5, 15))
		)
		given(authorService.getAllAuthors()).willReturn(authors)

		mockMvc.perform(get("/authors"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(authors.size))
			.andExpect(jsonPath("$[0].id").value(authors[0].id))
			.andExpect(jsonPath("$[0].fullName").value(authors[0].fullName))
	}

	@Test
	fun givenAuthorId_whenGetAuthor_thenReturnAuthor() {
		val author = Author(id = 1, fullName = "John Doe", pseudonym = "JD", birthDate = LocalDate.of(1990, 1, 1))
		given(authorService.getAuthorById(1)).willReturn(author)

		mockMvc.perform(get("/authors/1"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(author.id))
			.andExpect(jsonPath("$.fullName").value(author.fullName))
	}

	@Test
	fun givenValidAuthorCreateDto_whenCreateAuthor_thenReturnCreatedAuthor() {
		val dto = AuthorCreateDto(fullName = "John Doe", pseudonym = "JD", birthDate = LocalDate.of(1990, 1, 1))
		val author = Author(id = 1, fullName = dto.fullName, pseudonym = dto.pseudonym, birthDate = dto.birthDate)
		given(authorService.createAuthor(any())).willReturn(author)

		mockMvc.perform(
			post("/authors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(author.id))
			.andExpect(jsonPath("$.fullName").value(author.fullName))
	}

	@Test
	fun givenExistingAuthorIdAndAuthorUpdateDto_whenUpdateAuthor_thenReturnUpdatedAuthor() {
		val existingAuthor =
			Author(id = 1, fullName = "John Doe", pseudonym = "JD", birthDate = LocalDate.of(1990, 1, 1))
		val dto = AuthorUpdateDto(fullName = "Johnny Doe", pseudonym = null, birthDate = null)
		val updatedAuthor = existingAuthor.copy(fullName = "Johnny Doe")

		given(authorService.getAuthorById(1)).willReturn(existingAuthor)
		given(authorService.updateAuthor(updatedAuthor)).willReturn(updatedAuthor)

		mockMvc.perform(
			put("/authors/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(updatedAuthor.id))
			.andExpect(jsonPath("$.fullName").value("Johnny Doe"))
	}

	@Test
	fun givenAuthorId_whenDeleteAuthor_thenStatusOk() {
		mockMvc.perform(delete("/authors/1"))
			.andExpect(status().isOk)
	}
}
