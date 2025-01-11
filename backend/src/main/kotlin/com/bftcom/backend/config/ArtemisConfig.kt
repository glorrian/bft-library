package com.bftcom.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "library.artemis")
class ArtemisConfig {
	lateinit var reminderQueueName: String
	lateinit var returnQueueName: String
}