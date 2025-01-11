package com.bftcom.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.SwaggerParseResult
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Configuration
class OpenApiConfig : WebMvcConfigurer {

	@Bean
	@Throws(IOException::class)
	fun customOpenAPI(): OpenAPI {
		val resource = ClassPathResource("swagger.yaml")
		val path: Path = Paths.get(resource.uri)
		val content = String(Files.readAllBytes(path))
		val result: SwaggerParseResult = OpenAPIV3Parser().readContents(content)
		if (result.messages.isEmpty() && result.openAPI != null) {
			return result.openAPI
		} else {
			throw RuntimeException("Failed to parse OpenAPI definition: " + result.messages)
		}
	}


	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.addResourceHandler("/api-docs/**").addResourceLocations("classpath:/META-INF/resources/")
		registry.addResourceHandler("/swagger-ui/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
	}
}
