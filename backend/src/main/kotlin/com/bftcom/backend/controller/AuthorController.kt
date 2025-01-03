package com.bftcom.backend.controller

import com.bftcom.backend.entity.Author
import com.bftcom.backend.service.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/author")
class AuthorController @Autowired constructor(
    private val authorService: AuthorService
) {

    @GetMapping
    fun getAllAuthors(): List<Author> = authorService.findAll()

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<Author> {
        val author = authorService.findById(id)
        return if (author != null) {
            ResponseEntity.ok(author)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createAuthor(@RequestBody author: Author): Author = authorService.create(author)

    @PutMapping("/{id}")
    fun updateAuthor(@RequestBody author: Author): ResponseEntity<Author> {
        return try {
            authorService.update(author)
            ResponseEntity.ok(author)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<Void> {
        return if (authorService.deleteById(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}