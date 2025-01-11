package com.bftcom.backend.config

import com.bftcom.backend.dto.TestDateDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest(classes = [JacksonConfig::class])
class JacksonConfigTest {

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Test
	fun givenJacksonConfig_whenObjectMapperInjected_thenJavaTimeModuleRegistered() {
		assertNotNull(objectMapper)

		val hasJavaTimeModule = objectMapper
			.registeredModuleIds
			.any { it == JavaTimeModule().typeId }
		assertTrue(hasJavaTimeModule, "JavaTimeModule should be registered")
	}

	@Test
	fun givenLocalDate_whenSerializeAndDeserialize_thenCorrect() {
		val original = TestDateDto(id = 1L, date = LocalDate.of(2025, 1, 7))

		val json = objectMapper.writeValueAsString(original)
		val deserialized = objectMapper.readValue(json, TestDateDto::class.java)

		assertEquals(original, deserialized)
	}
}
