package com.bftcom.backend.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LibraryConfigTest {

	@Autowired
	lateinit var libraryConfig: LibraryConfig

	@Test
	fun givenPropertySet_whenLibraryConfigLoaded_thenMaxBorrowingDaysIsCorrect() {
		assertEquals(30, libraryConfig.maxBorrowingDays)
	}
}
