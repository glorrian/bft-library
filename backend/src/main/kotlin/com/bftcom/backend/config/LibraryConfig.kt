package com.bftcom.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class LibraryConfig {
	@Value("\${library.max-borrowing-days}")
	var maxBorrowingDays: Long = 0

	@Value("\${library.templates.return}")
	var returnTemplatePath: String = ""
}