package com.bftcom.backend.controller

import com.bftcom.backend.dto.IssueRequest
import com.bftcom.backend.dto.ReturnRequest
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.service.IssuingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
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
@Import(BorrowingControllerIntegrationTest.Config::class)
class BorrowingControllerIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var issuingService: IssuingService

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun issuingService(): IssuingService = Mockito.mock(IssuingService::class.java)
	}

	@Test
	fun givenValidIssueRequest_whenPostIssue_thenReturnBorrowingRecord() {
		val issueRequest = IssueRequest(readerId = 1L, libraryBookId = 100L, borrowDate = LocalDate.of(2023, 8, 1))
		val expectedRecord = BorrowingRecord(
			id = 1L,
			readerId = issueRequest.readerId,
			libraryBookId = issueRequest.libraryBookId,
			borrowDate = issueRequest.borrowDate,
			returnDate = null
		)
		given(issuingService.issueBook(issueRequest.readerId, issueRequest.libraryBookId, issueRequest.borrowDate))
			.willReturn(expectedRecord)

		mockMvc.perform(
			post("/borrowing/issue")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(issueRequest))
		)
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(expectedRecord.id))
			.andExpect(jsonPath("$.readerId").value(expectedRecord.readerId))
			.andExpect(jsonPath("$.libraryBookId").value(expectedRecord.libraryBookId))
	}

	@Test
	fun givenValidReturnRequest_whenPostReturn_thenStatusOk() {
		val returnRequest = ReturnRequest(recordId = 1L, returnDate = LocalDate.of(2023, 8, 5))

		mockMvc.perform(
			post("/borrowing/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(returnRequest))
		)
			.andExpect(status().isOk)

		Mockito.verify(issuingService).queueReturnBook(returnRequest.recordId, returnRequest.returnDate)
	}
}
