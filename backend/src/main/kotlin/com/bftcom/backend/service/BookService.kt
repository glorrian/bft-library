package com.bftcom.backend.service

import com.bftcom.backend.entity.Book
import com.bftcom.backend.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
	private val bookRepository: BookRepository
) {

	fun getAllBooks(eager: Boolean = false): List<Book> {
		return if (eager) {
			bookRepository.findAllEager()
		} else {
			bookRepository.findAll()
		}
	}

	fun getBookById(id: Long, eager: Boolean = false): Book? {
		return if (eager) {
			bookRepository.findByIdEager(id)
		} else {
			bookRepository.findById(id)
		}
	}

	fun createBook(book: Book): Book {
		return bookRepository.create(book)
	}

	fun updateBook(book: Book): Book {
		return bookRepository.update(book)
	}

	fun deleteBook(id: Long) {
		bookRepository.deleteById(id)
	}
}