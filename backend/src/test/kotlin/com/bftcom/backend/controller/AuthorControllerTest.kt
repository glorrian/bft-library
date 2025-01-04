package com.bftcom.backend.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate

//class AuthorControllerTest {
//
//	private lateinit var mockMvc: MockMvc
//	private lateinit var authorService: AuthorService
//	private lateinit var authorController: AuthorController
//
//	@BeforeEach
//	fun setup() {
//		authorService = mock(AuthorService::class.java)
//		authorController = AuthorController(authorService)
//		mockMvc = MockMvcBuilders.standaloneSetup(authorController).build()
//	}
//
//	@Test
//	fun `getAllAuthors should return list of authors`() {
//		val authors = listOf(
//			Author(id = 1L, fullName = "Author One", birthDate = LocalDate.now()),
//			Author(id = 2L, fullName = "Author Two", birthDate = LocalDate.now())
//		)
//		`when`(authorService.findAll()).thenReturn(authors)
//
//		mockMvc.perform(get("/author"))
//			.andExpect(status().isOk)
//			.andExpect(jsonPath("$.length()").value(authors.size))
//			.andExpect(jsonPath("$[0].fullName").value("Author One"))
//			.andExpect(jsonPath("$[1].fullName").value("Author Two"))
//	}
//
//	@Test
//	fun `getAuthorById should return author when found`() {
//		val author = Author(id = 1L, fullName = "Author One", birthDate = LocalDate.now())
//		`when`(authorService.findById(1L)).thenReturn(author)
//
//		mockMvc.perform(get("/author/1"))
//			.andExpect(status().isOk)
//			.andExpect(jsonPath("$.fullName").value("Author One"))
//	}
//
//	@Test
//	fun `getAuthorById should return 404 when not found`() {
//		`when`(authorService.findById(1L)).thenReturn(null)
//
//		mockMvc.perform(get("/author/1"))
//			.andExpect(status().isNotFound)
//	}
//
//	@Test
//	fun `createAuthor should return created author`() {
//		val author = Author(id = 0L, fullName = "New Author", birthDate = LocalDate.now())
//		val createdAuthor = author.copy(id = 1L)
//		`when`(authorService.create(author)).thenReturn(createdAuthor)
//
//		mockMvc.perform(post("/author")
//			.contentType(MediaType.APPLICATION_JSON)
//			.content("""{"id":0,"fullName":"New Author","birthDate":"${LocalDate.now()}"}"""))
//			.andExpect(status().isOk)
//			.andExpect(jsonPath("$.id").value(1L))
//			.andExpect(jsonPath("$.fullName").value("New Author"))
//	}
//
//	@Test
//	fun `updateAuthor should return updated author`() {
//		val author = Author(id = 1L, fullName = "Updated Author", birthDate = LocalDate.now())
//		doNothing().`when`(authorService).update(author)
//
//		mockMvc.perform(put("/author/1")
//			.contentType(MediaType.APPLICATION_JSON)
//			.content("""{"id":1,"fullName":"Updated Author","birthDate":"${LocalDate.now()}"}"""))
//			.andExpect(status().isOk)
//	}
//
//	@Test
//	fun `updateAuthor should return 400 when id is 0`() {
//		val author = Author(id = 0L, fullName = "Invalid Author", birthDate = LocalDate.now())
//		doThrow(IllegalArgumentException("Author id must not be 0 to update an author")).`when`(authorService).update(author)
//		mockMvc.perform(put("/author/0")
//			.contentType(MediaType.APPLICATION_JSON)
//			.content("""{"id":0,"fullName":"Invalid Author","birthDate":"${LocalDate.now()}"}"""))
//			.andExpect(status().isBadRequest)
//	}
//
//	@Test
//	fun `deleteAuthor should return 204 when successful`() {
//		`when`(authorService.deleteById(1L)).thenReturn(true)
//
//		mockMvc.perform(delete("/author/1"))
//			.andExpect(status().isNoContent)
//	}
//
//	@Test
//	fun `deleteAuthor should return 404 when not found`() {
//		`when`(authorService.deleteById(1L)).thenReturn(false)
//
//		mockMvc.perform(delete("/author/1"))
//			.andExpect(status().isNotFound)
//	}
//}
