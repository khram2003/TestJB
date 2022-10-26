package com.example.testtaskjb.dto

import com.example.testtaskjb.Author
import com.example.testtaskjb.converters.JpaConverterJson
import java.time.Year
import javax.persistence.Convert

data class BookDTO(
    var bookId: Long?,
    var title: String?,
    var description: String?,
    var isbn: String?,
    var printYear: Year?,
    var alreadyRead: Boolean?,
    @Convert(converter = JpaConverterJson::class) var author: Author?
)
