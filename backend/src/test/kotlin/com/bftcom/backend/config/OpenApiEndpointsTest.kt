package com.bftcom.backend.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiEndpointsTest(@Autowired val restTemplate: TestRestTemplate) {

	@Test
	fun givenSwaggerUIEndpoint_whenGetRequest_thenStatusOKAndContainsSwaggerUI() {
		val url = "/swagger-ui/index.html"

		val response = restTemplate.getForEntity(url, String::class.java)

		assertEquals(HttpStatus.OK, response.statusCode)
		assertTrue(response.body?.contains("Swagger UI", ignoreCase = true) == true)
	}
}
