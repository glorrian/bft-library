package com.bftcom.backend.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

	@Bean
	fun customOpenAPI(): OpenAPI {
		return OpenAPI()
			.info(
				Info().title("BFT Library API")
					.description("API для управления библиотечными ресурсами.")
					.version("1.0.0")
					.contact(
						Contact().name("Станислав Щербаков").email("glorrian55@yandex.ru")
					)
			)
			.externalDocs(
				ExternalDocumentation()
					.description("Дополнительная документация")
					.url("https://github.com/glorrian/bft-library")
			)
	}
}
