package com.example.testtaskjb.services

import com.example.testtaskjb.Author
import com.example.testtaskjb.BookNotFoundException
import com.example.testtaskjb.dto.BookDTO
import com.example.testtaskjb.entities.Book
import com.example.testtaskjb.repositories.BookRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class BookService(private var bookRepository: BookRepository) {
    fun getBooks(): List<BookDTO> = bookRepository.findAll().map { it.toBookDTO() }.toList()

    fun addBook(bookDTO: BookDTO): ResponseEntity<Long> {
        if (bookRepository.existsByIsbn(bookDTO.isbn!!)) return ResponseEntity(HttpStatus.BAD_REQUEST)
        val newBook = Book(bookDTO)
        bookRepository.save(newBook)
        return ResponseEntity(newBook.getId(), HttpStatus.CREATED)
    }

    fun findBookByISBN(isbn: String): Book =
        bookRepository.findBookByIsbn(isbn)
            ?: throw BookNotFoundException(Exception("There is no book with ISBN: $isbn"))

    @Transactional
    fun setBookAlreadyRead(alreadyRead: Boolean, bookId: Long): ResponseEntity<Unit>{
        if (!bookRepository.existsById(bookId)) return ResponseEntity(HttpStatus.NOT_FOUND)
        bookRepository.updateAlreadyRead(alreadyRead, bookId)
        return ResponseEntity(HttpStatus.OK)
    }

    @Transactional
    fun updateEdition(bookId: Long, newBook: BookDTO): ResponseEntity<Unit> {
        if(!bookRepository.existsById(bookId)) return ResponseEntity(HttpStatus.NOT_FOUND)
        bookRepository.updateEdition(
            newBook.alreadyRead,
            newBook.description,
            newBook.author,
            newBook.isbn,
            newBook.printYear,
            newBook.title,
            newBook.bookId
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @Transactional
    fun deleteBookByISBN(isbn: String): ResponseEntity<Unit> {
        if (!bookRepository.existsByIsbn(isbn)) return ResponseEntity(HttpStatus.NOT_FOUND)
        bookRepository.deleteByIsbn(isbn)
        return ResponseEntity(HttpStatus.OK)
    }

    fun findBookByAuthor(firstName: String, lastName: String): List<BookDTO> =
        bookRepository.findBookByAuthor(Author(firstName, lastName)).map { it.toBookDTO() }.toList()
}