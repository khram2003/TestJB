package com.example.testtaskjb.entities

import com.example.testtaskjb.Author
import com.example.testtaskjb.converters.JpaConverterJson
import com.example.testtaskjb.converters.YearAttributeConverter
import com.example.testtaskjb.dto.BookDTO
import java.time.Year
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.Convert


@Entity(name = "book")
@Table(name = "books", indexes = [Index(name = "unique_isbn_index", columnList = "isbn", unique = true)])
class Book() {

    constructor(bookDTO: BookDTO) : this() {
        title = bookDTO.title
        description = bookDTO.description
        isbn = bookDTO.isbn
        printYear = bookDTO.printYear
        alreadyRead = bookDTO.alreadyRead
        author = bookDTO.author
    }

    fun toBookDTO(): BookDTO = BookDTO(
        bookId,
        title,
        description,
        isbn,
        printYear,
        alreadyRead,
        author
    )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var bookId: Long? = null

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(256)")
    private var title: String? = null

    @Column(name = "description", nullable = true, columnDefinition = "VARCHAR(2048)")
    private var description: String? = null

    @Column(name = "isbn", nullable = false, columnDefinition = "VARCHAR(13)")
    private var isbn: String? = null

    @Column(name = "print_year", nullable = false, columnDefinition = "SMALLINT")
    @Convert(converter = YearAttributeConverter::class)
    private var printYear: Year? = null

    @Column(name = "already_read", nullable = false, columnDefinition = "BOOLEAN")
    var alreadyRead: Boolean? = false

    @Column(name = "author", nullable = false, columnDefinition = "VARCHAR(65535)")
    @Convert(converter = JpaConverterJson::class)
    var author: Author? = null

    override fun toString(): String =
        "Book(bookId=$bookId, title=$title, description=$description," +
                " isbn=$isbn, printYear=$printYear, alreadyRead=$alreadyRead)"

    fun getId() = bookId

}
