package com.example.testtaskjb

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(
    value = HttpStatus.NOT_FOUND,
    reason = "Requested book does not exist"
)
class BookNotFoundException(t: Throwable?) : RuntimeException(t)
