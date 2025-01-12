package com.bftcom.backend.listener

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReminderMessageDto
import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.Reader
import com.bftcom.backend.repository.BookRepository
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.ReaderRepository
import com.bftcom.backend.service.EmailService
import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.jms.core.JmsTemplate
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Import(ReminderListenerIntegrationTest.Config::class)
class ReminderListenerIntegrationTest {

	@Autowired
	lateinit var jmsTemplate: JmsTemplate

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Autowired
	lateinit var emailService: EmailService

	@Autowired
	lateinit var borrowingRecordRepository: BorrowingRecordRepository

	@Autowired
	lateinit var readerRepository: ReaderRepository

	@Autowired
	lateinit var bookRepository: BookRepository

	@Autowired
	lateinit var artemisConfig: ArtemisConfig

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun emailService(): EmailService = mock(EmailService::class.java)

		@Bean
		@Primary
		fun borrowingRecordRepository(): BorrowingRecordRepository = mock(BorrowingRecordRepository::class.java)

		@Bean
		@Primary
		fun readerRepository(): ReaderRepository = mock(ReaderRepository::class.java)

		@Bean
		@Primary
		fun bookRepository(): BookRepository = mock(BookRepository::class.java)
	}

	@Test
	fun givenValidReminderMessage_whenReceiveMessage_thenProcessAndSendEmail() {
		val borrowingRecordId = 1L
		val reminderMessageDto = ReminderMessageDto(borrowingRecordId)

		val borrowingRecord = BorrowingRecord(
			id = borrowingRecordId,
			libraryBookId = 100L,
			readerId = 10L,
			borrowDate = LocalDate.of(2023, 1, 1),
			returnDate = null
		)
		val reader = Reader(id = 10L, fullName = "John Doe", email = "john@example.com")
		val book = Book(id = 100L, title = "Test Book", isbn = "1234567890123", publicationDate = LocalDate.of(2020, 1, 1), worksIds = emptyList())

		whenever(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(borrowingRecord)
		whenever(readerRepository.findById(borrowingRecord.readerId)).thenReturn(reader)
		whenever(bookRepository.findById(borrowingRecord.libraryBookId)).thenReturn(book)

		val messageContent = objectMapper.writeValueAsString(reminderMessageDto)
		val queueName = artemisConfig.reminderQueueName
		jmsTemplate.convertAndSend(queueName, messageContent)

		Thread.sleep(1000)

		val expectedSubject = "Напоминание о задолженности в библиотеке"
		val expectedBody = """
                Уважаемый(ая) ${reader.fullName},
                
                Вы взяли в библиотеке книгу "${book.title}" ${borrowingRecord.borrowDate}.
                Напоминаем, что срок возврата книги истёк. Пожалуйста, верните книгу как можно скорее.
                
                С уважением,
                Ваша Библиотека
            """.trimIndent()

		Mockito.verify(emailService).sendSimpleMessage(reader.email, expectedSubject, expectedBody)
	}
}