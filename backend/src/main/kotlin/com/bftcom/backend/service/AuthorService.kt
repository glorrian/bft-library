package com.bftcom.backend.service

import com.bftcom.backend.entity.Author
import com.bftcom.backend.repository.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(
	private val authorRepository: AuthorRepository
) {

	fun getAllAuthors(): List<Author> {
		return authorRepository.findAll()
	}

	fun getAuthorById(id: Long): Author? {
		return authorRepository.findById(id)
	}

	fun createAuthor(author: Author): Author {
		return authorRepository.create(author)
	}

	fun updateAuthor(author: Author): Author {
		return authorRepository.update(author)
	}

	fun deleteAuthor(id: Long) {
		authorRepository.deleteById(id)
	}
}