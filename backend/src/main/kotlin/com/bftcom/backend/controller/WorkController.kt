package com.bftcom.backend.controller

import com.bftcom.backend.dto.WorkCreateDto
import com.bftcom.backend.dto.WorkUpdateDto
import com.bftcom.backend.entity.Work
import com.bftcom.backend.service.WorkService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/works")
class WorkController(
	private val workService: WorkService
) {

	@GetMapping
	fun getAllWorks(
		@RequestParam(required = false, defaultValue = "false") eager: Boolean
	): List<Work> {
		return workService.getAllWorks(eager)
	}

	@GetMapping("/{id}")
	fun getWork(
		@PathVariable id: Long,
		@RequestParam(required = false, defaultValue = "false") eager: Boolean
	): Work? {
		return workService.getWorkById(id, eager)
	}

	@PostMapping
	fun createWork(@RequestBody dto: WorkCreateDto): Work {
		val newWork = Work(
			id = null,
			title = dto.title,
			genreId = dto.genreId,
			authorsIds = dto.authorIds
		)
		return workService.createWork(newWork)
	}

	@PutMapping("/{id}")
	fun updateWork(@PathVariable id: Long, @RequestBody dto: WorkUpdateDto): Work {
		val existing = workService.getWorkById(id, eager = false)
			?: throw IllegalArgumentException("Work with id=$id not found")

		val updated = existing.copy(
			title = dto.title ?: existing.title,
			genreId = dto.genreId ?: existing.genreId,
			authorsIds = dto.authorIds ?: existing.authorsIds
		)
		return workService.updateWork(updated)
	}

	@DeleteMapping("/{id}")
	fun deleteWork(@PathVariable id: Long) {
		workService.deleteWork(id)
	}
}