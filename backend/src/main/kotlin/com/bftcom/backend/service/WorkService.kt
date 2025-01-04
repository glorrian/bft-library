package com.bftcom.backend.service

import com.bftcom.backend.entity.Work
import com.bftcom.backend.repository.WorkRepository
import org.springframework.stereotype.Service


@Service
class WorkService(
	private val workRepository: WorkRepository
) {

	fun getAllWorks(eager: Boolean = false): List<Work> {
		return if (eager) {
			workRepository.findAllEager()
		} else {
			workRepository.findAll()
		}
	}

	fun getWorkById(id: Long, eager: Boolean = false): Work? {
		return if (eager) {
			workRepository.findByIdEager(id)
		} else {
			workRepository.findById(id)
		}
	}

	fun createWork(work: Work): Work {
		return workRepository.create(work)
	}

	fun updateWork(work: Work): Work {
		return workRepository.update(work)
	}

	fun deleteWork(id: Long) {
		workRepository.deleteById(id)
	}
}