package com.example.testtaskjb.repositories

import com.example.testtaskjb.Author
import com.example.testtaskjb.entities.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Year

@Repository
interface BookRepository : JpaRepository<Book, Long> {

    @Query("SELECT b FROM book b WHERE b.isbn = :isbn")
    fun findBookByIsbn(@Param("isbn") isbn: String): Book?

    @Modifying
    @Query("UPDATE book b SET b.alreadyRead = :alreadyRead WHERE b.bookId = :bookId")
    fun updateAlreadyRead(@Param("alreadyRead") alreadyRead: Boolean, @Param("bookId") bookId: Long)

    fun findBookByAuthor(author: Author): List<Book>

    fun existsByIsbn(isbn: String): Boolean

    @Modifying
    @Query("DELETE FROM book b WHERE b.isbn=:isbn")
    fun deleteByIsbn(@Param("isbn") isbn: String)

    //to increase scalability it could be useful to update each book field separately
    @Modifying
    @Query(
        "UPDATE book b SET b.alreadyRead = :alreadyRead, b.description = :description, b.author = :author," +
                " b.isbn = :isbn, b.printYear = :printYear, b.title = :title WHERE b.bookId = :bookId"
    )
    fun updateEdition(
        @Param("alreadyRead") alreadyRead: Boolean?,
        @Param("description") description: String?,
        @Param("author") author: Author?,
        @Param("isbn") isbn: String?,
        @Param("printYear") printYear: Year?,
        @Param("title") title: String?,
        @Param("bookId") bookId: Long?
    )
}
