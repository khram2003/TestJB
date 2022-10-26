package com.example.testtaskjb.controllers

import com.example.testtaskjb.dto.BookDTO
import com.example.testtaskjb.services.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["api/v1/book"])
class BookController @Autowired constructor(private var bookService: BookService) {
    @GetMapping("/getAllBooks")
    fun getBooks(): List<BookDTO> = bookService.getBooks()

    @GetMapping("/findBookByISBN")
    fun findBookByISBN(@RequestParam isbn: String): BookDTO = bookService.findBookByISBN(isbn).toBookDTO()

    @GetMapping("/findBooksByAuthor")
    fun findBookByAuthor(
        @RequestParam firstName: String,
        @RequestParam lastName: String
    ): List<BookDTO> = bookService.findBookByAuthor(firstName, lastName)

    @PostMapping("/addBook")
    fun addBook(@RequestBody bookDTO: BookDTO): ResponseEntity<Long> = bookService.addBook(bookDTO)

    @PutMapping("/setBookAlreadyRead")
    fun setBookAlreadyRead(
        @RequestParam alreadyRead: Boolean,
        @RequestParam bookId: Long
    ): ResponseEntity<Unit> = bookService.setBookAlreadyRead(alreadyRead, bookId)

    @PutMapping("/updateEdition")
    fun updateEdition(
        @RequestBody bookDTO: BookDTO
    ) = bookDTO.bookId?.let { bookService.updateEdition(it, bookDTO) }

    @DeleteMapping("/deleteBookByISBN")
    fun deleteBookByISBN(@RequestParam isbn: String) = bookService.deleteBookByISBN(isbn)

}