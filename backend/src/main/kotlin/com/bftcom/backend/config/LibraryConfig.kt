package com.bftcom.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Configuration properties for library-related settings.
 *
 * Maps properties prefixed with "library" from the application's configuration.
 *
 * @property maxBorrowingDays The maximum number of days a book can be borrowed.
 * @property emailTemplate Template configuration for email notifications.
 */
@Configuration
@ConfigurationProperties(prefix = "library")
class LibraryConfig {
	var maxBorrowingDays: Long = 0
	lateinit var emailTemplate: EmailTemplate

	/**
	 * Encapsulates email template details used for return notifications.
	 *
	 * @property returnMailTemplate Path or content of the email template for returns.
	 */
	class EmailTemplate {
		lateinit var returnMailTemplate: String
		lateinit var remindMailTemplate: String
	}
}