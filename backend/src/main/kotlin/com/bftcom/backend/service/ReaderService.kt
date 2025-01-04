package com.bftcom.backend.service

import com.bftcom.backend.entity.Reader
import com.bftcom.backend.repository.ReaderRepository
import org.springframework.stereotype.Service

@Service
class ReaderService(
	private val readerRepository: ReaderRepository
) {

	fun getAllReaders(): List<Reader> {
		return readerRepository.findAll()
	}

	fun getReaderById(id: Long): Reader? {
		return readerRepository.findById(id)
	}

	fun createReader(reader: Reader): Reader {
		return readerRepository.create(reader)
	}

	fun updateReader(reader: Reader): Reader {
		return readerRepository.update(reader)
	}

	fun deleteReader(id: Long) {
		readerRepository.deleteById(id)
	}
}