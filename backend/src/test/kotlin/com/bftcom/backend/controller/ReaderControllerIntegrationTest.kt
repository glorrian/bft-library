package com.bftcom.backend.controller

import com.bftcom.backend.dto.ReaderCreateDto
import com.bftcom.backend.dto.ReaderUpdateDto
import com.bftcom.backend.entity.Reader
import com.bftcom.backend.service.ReaderService
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
@Import(ReaderControllerIntegrationTest.Config::class)
class ReaderControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var readerService: ReaderService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun readerService(): ReaderService = Mockito.mock(ReaderService::class.java)
	}

	@Test
	fun givenReaders_whenGetAll_thenReturnReadersList() {
		val readers = listOf(
			Reader(id = 1, fullName = "Alice", email = "alice@example.com"),
			Reader(id = 2, fullName = "Bob", email = "bob@example.com")
		)
		given(readerService.getAllReaders()).willReturn(readers)

		mockMvc.perform(get("/readers"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(readers.size))
			.andExpect(jsonPath("$[0].id").value(readers[0].id))
			.andExpect(jsonPath("$[0].fullName").value(readers[0].fullName))
			.andExpect(jsonPath("$[0].email").value(readers[0].email))
	}

	@Test
	fun givenReaderId_whenGetReader_thenReturnReader() {
		val reader = Reader(id = 1, fullName = "Alice", email = "alice@example.com")
		given(readerService.getReaderById(1)).willReturn(reader)

		mockMvc.perform(get("/readers/1"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(reader.id))
			.andExpect(jsonPath("$.fullName").value(reader.fullName))
			.andExpect(jsonPath("$.email").value(reader.email))
	}

	@Test
	fun givenValidReaderCreateDto_whenCreateReader_thenReturnCreatedReader() {
		val dto = ReaderCreateDto(fullName = "Alice", email = "alice@example.com")
		val reader = Reader(id = 1, fullName = dto.fullName, email = dto.email)
		given(readerService.createReader(any())).willReturn(reader)

		mockMvc.perform(
			post("/readers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(reader.id))
			.andExpect(jsonPath("$.fullName").value(reader.fullName))
			.andExpect(jsonPath("$.email").value(reader.email))
	}

	@Test
	fun givenExistingReaderIdAndValidUpdateDto_whenUpdateReader_thenReturnUpdatedReader() {
		val existingReader = Reader(id = 1, fullName = "Alice", email = "alice@example.com")
		val dto = ReaderUpdateDto(fullName = "Alice Updated", email = null)
		val updatedReader = existingReader.copy(fullName = "Alice Updated")

		given(readerService.getReaderById(1)).willReturn(existingReader)
		given(readerService.updateReader(updatedReader)).willReturn(updatedReader)

		mockMvc.perform(
			put("/readers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(updatedReader.id))
			.andExpect(jsonPath("$.fullName").value("Alice Updated"))
			.andExpect(jsonPath("$.email").value(existingReader.email))
	}

	@Test
	fun givenReaderId_whenDeleteReader_thenStatusOk() {
		mockMvc.perform(delete("/readers/1"))
			.andExpect(status().isOk)
	}
}
