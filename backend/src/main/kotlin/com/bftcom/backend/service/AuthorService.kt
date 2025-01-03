package com.bftcom.backend.service

import com.bftcom.backend.entity.Author
import com.bftcom.backend.repository.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorService @Autowired constructor(
	private val authorRepository: AuthorRepository
) {
	fun findAll() = authorRepository.findAll()

	fun findById(id: Long) = authorRepository.findById(id)

	fun deleteById(id: Long) = authorRepository.deleteById(id)

	fun create(author: Author): Author {
		if (author.id != 0L) {
			throw IllegalArgumentException("Author id must be 0 to create a new author")
		}
		val authorId = authorRepository.save(author)
		return author.copy(id = authorId)
	}

	fun update(author: Author) {
		if (author.id == 0L) {
			throw IllegalArgumentException("Author id must not be 0 to update an author")
		}
		authorRepository.save(author)
	}
}