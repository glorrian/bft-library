package com.bftcom.backend.controller

import com.bftcom.backend.dto.GenreCreateDto
import com.bftcom.backend.dto.GenreUpdateDto
import com.bftcom.backend.entity.Genre
import com.bftcom.backend.service.GenreService
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
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
@AutoConfigureMockMvc
@Import(GenreControllerIntegrationTest.Config::class)
class GenreControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var genreService: GenreService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun genreService(): GenreService = Mockito.mock(GenreService::class.java)
	}

	@Test
	fun givenGenres_whenGetAll_thenReturnGenresList() {
		val genres = listOf(
			Genre(id = 1, name = "Fiction"),
			Genre(id = 2, name = "Non-fiction")
		)
		given(genreService.getAllGenres()).willReturn(genres)

		mockMvc.perform(get("/genres"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(genres.size))
			.andExpect(jsonPath("$[0].id").value(genres[0].id))
			.andExpect(jsonPath("$[0].name").value(genres[0].name))
	}

	@Test
	fun givenGenreId_whenGetGenre_thenReturnGenre() {
		val genre = Genre(id = 1, name = "Fiction")
		given(genreService.getGenreById(1)).willReturn(genre)

		mockMvc.perform(get("/genres/1"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(genre.id))
			.andExpect(jsonPath("$.name").value(genre.name))
	}

	@Test
	fun givenValidGenreCreateDto_whenCreateGenre_thenReturnCreatedGenre() {
		val dto = GenreCreateDto(name = "Fantasy")
		val createdGenre = Genre(id = 1, name = dto.name)
		given(genreService.createGenre(any())).willReturn(createdGenre)

		mockMvc.perform(
			post("/genres")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(createdGenre.id))
			.andExpect(jsonPath("$.name").value(createdGenre.name))
	}

	@Test
	fun givenExistingGenreIdAndValidUpdateDto_whenUpdateGenre_thenReturnUpdatedGenre() {
		val existingGenre = Genre(id = 1, name = "Fantasy")
		val dto = GenreUpdateDto(name = "Epic Fantasy")
		val updatedGenre = existingGenre.copy(name = "Epic Fantasy")

		given(genreService.getGenreById(1)).willReturn(existingGenre)
		given(genreService.updateGenre(updatedGenre)).willReturn(updatedGenre)

		mockMvc.perform(
			put("/genres/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(updatedGenre.id))
			.andExpect(jsonPath("$.name").value("Epic Fantasy"))
	}

	@Test
	fun givenGenreId_whenDeleteGenre_thenStatusOk() {
		mockMvc.perform(delete("/genres/1"))
			.andExpect(status().isOk)
	}
}
