package com.bftcom.backend.service

import com.bftcom.backend.entity.LibraryBook
import com.bftcom.backend.repository.LibraryBookRepository
import org.springframework.stereotype.Service

@Service
class LibraryBookService(
	private val libraryBookRepository: LibraryBookRepository
) {

	fun getAllLibraryBooks(): List<LibraryBook> {
		return libraryBookRepository.findAll()
	}

	fun getLibraryBookById(id: Long): LibraryBook? {
		return libraryBookRepository.findById(id)
	}

	fun createLibraryBook(libraryBook: LibraryBook): LibraryBook {
		return libraryBookRepository.create(libraryBook)
	}

	fun updateLibraryBook(libraryBook: LibraryBook): LibraryBook {
		return libraryBookRepository.update(libraryBook)
	}

	fun deleteLibraryBook(id: Long) {
		libraryBookRepository.deleteById(id)
	}
}