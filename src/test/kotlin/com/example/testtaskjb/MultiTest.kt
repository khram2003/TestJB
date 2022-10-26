package com.example.testtaskjb

import com.example.testtaskjb.dto.BookDTO
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.Year
import java.util.LinkedList
import kotlin.jvm.Throws

const val MAXLENGTH = 100

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MultiTest {

    @LocalServerPort
    private val port = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null


    fun getHundredOfRandomBooks(): List<BookDTO> {
        val bookDTOs: MutableList<BookDTO> = LinkedList()
        val rand137 = kotlin.random.Random(137)
        val rand129 = kotlin.random.Random(129)
        ( 1..100).forEach { _ ->
            bookDTOs.add(
                BookDTO(
                    null,
                    RandomStringUtils.randomAlphanumeric(MAXLENGTH),
                    RandomStringUtils.randomAlphanumeric(MAXLENGTH),
                    RandomStringUtils.randomAlphanumeric(13),
                    Year.of(rand137.nextInt(0, 3000)),
                    rand129.nextInt() % 2 != 0,
                    Author(
                        RandomStringUtils.randomAlphanumeric(MAXLENGTH),
                        RandomStringUtils.randomAlphanumeric(MAXLENGTH)
                    )
                )
            )
        }
        return bookDTOs
    }

    @Test
    @Throws(Exception::class)
    fun crazyTest() {
        val booksToAddList = getHundredOfRandomBooks()
        var currentId: Long = 0
        val existingIds: MutableList<Long> = ArrayList()
        for (bookToAdd in booksToAddList) {
            val response = restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                bookToAdd,
                String::class.java
            )
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            currentId++
            assert(currentId == (response.body?.toLong() ?: -1))
            existingIds.add(currentId)
        }

        val isbnList = booksToAddList.map { it.isbn }.toList()
        for (isbn in isbnList) {
            val response = restTemplate!!.getForEntity(
                "http://localhost:$port/api/v1/book/findBookByISBN?isbn=$isbn",
                BookDTO::class.java
            )
            assert(response.body?.isbn.equals(isbn))
        }

        val authorList = booksToAddList.map { it.author }.toList()
        for (author in authorList) {
            val response = restTemplate!!.getForEntity(
                "http://localhost:$port/api/v1/book/findBooksByAuthor?" +
                        "firstName=${author?.firstName}&lastName=${author?.lastName}",
                Array<BookDTO>::class.java
            )
            for (book in response.body!!) {
                assert(book.author?.firstName.equals(author?.firstName))
                assert(book.author?.lastName.equals(author?.lastName))
            }
        }
    }
}