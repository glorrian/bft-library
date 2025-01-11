package com.bftcom.backend.controller

import com.bftcom.backend.dto.IssueRequest
import com.bftcom.backend.dto.ReturnRequest
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.service.IssuingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/borrowing")
class BorrowingController(
	private val issuingService: IssuingService
) {
	@PostMapping("/issue")
	fun issueBook(@RequestBody request: IssueRequest): BorrowingRecord {
		return issuingService.issueBook(request.readerId, request.libraryBookId, request.borrowDate)
	}

	@PostMapping("/return")
	fun returnBook(@RequestBody request: ReturnRequest) {
		issuingService.queueReturnBook(request.recordId, request.returnDate)
	}
}