package com.bftcom.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Configuration properties for Artemis-related settings.
 *
 * Maps properties prefixed with "library.artemis" from the application's configuration.
 *
 * @property reminderQueueName The JMS queue name used for reminder messages.
 * @property returnQueueName The JMS queue name used for return messages.
 */
@Configuration
@ConfigurationProperties(prefix = "library.artemis")
class ArtemisConfig {
	lateinit var reminderQueueName: String
	lateinit var returnQueueName: String
}