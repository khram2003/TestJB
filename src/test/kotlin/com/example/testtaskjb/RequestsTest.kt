package com.example.testtaskjb

import com.example.testtaskjb.dto.BookDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.test.annotation.DirtiesContext
import java.net.URI
import java.time.Year
import kotlin.jvm.Throws


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RequestsTest {

    val newBookDTO = BookDTO(
        null,
        "To Have and Have Not",
        "Cool book!",
        "9780020518808",
        Year.of(1937),
        false,
        Author("Ernest", "Hemingway")
    )

    @LocalServerPort
    private val port = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Test
    @Throws(Exception::class)
    fun firstGetBooksShouldReturnEmptyList() {
        assertThat(
            restTemplate!!.getForEntity(
                "http://localhost:$port/api/v1/book/getAllBooks",
                Array<BookDTO>::class.java
            ).body
        ).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun findBookByNotExistingISBNShouldReturnNotFound() {
        for (i in 1..100) assertThat(
            restTemplate!!.getForEntity(
                "http://localhost:$port/api/v1/book/findBookByISBN?isbn=$i",
                BookDTO::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Throws(Exception::class)
    fun findBookByNotExistingAuthorShouldReturnEmptyList() {
        for (i in 'a' downTo 'Z') assertThat(
            restTemplate!!.getForEntity(
                "http://localhost:$port/api/v1/book/findBooksByAuthor?firstName=$i&lastName=$i",
                Array<BookDTO>::class.java
            ).body
        ).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun addNewBookShouldReturnCreated() {
        assertThat(
            restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    @Throws(Exception::class)
    fun addNewBookWithSameISBNShouldReturnBadRequest() {


        assertThat(
            restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.CREATED)

        assertThat(
            restTemplate.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Throws(Exception::class)
    fun setExisistingBookAlreadyReadShouldReturnOK() {

        assertThat(
            restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.CREATED)


        val requestEntity: RequestEntity<Unit> = RequestEntity(
            Unit,
            HttpMethod.PUT,
            URI("http://localhost:$port/api/v1/book/setBookAlreadyRead?bookId=1&alreadyRead=true")
        )
        assertThat(
            restTemplate.exchange(
                requestEntity,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Throws(Exception::class)
    fun setBookAlreadyReadWithNotExistingIdShouldReturnNotFound() {
        val requestEntity: RequestEntity<Unit> = RequestEntity(
            Unit,
            HttpMethod.PUT,
            URI("http://localhost:$port/api/v1/book/setBookAlreadyRead?bookId=1&alreadyRead=true")
        )
        assertThat(
            restTemplate?.exchange(
                requestEntity,
                String::class.java
            )?.statusCode ?: -1
        ).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Throws(Exception::class)
    fun updateExistingBookShouldReturnOK() {

        assertThat(
            restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.CREATED)


        val myNewBookDTO = BookDTO(
            1,
            "To Have and Have Not 2",
            "Coolest book!",
            "77777777",
            Year.of(2022),
            true,
            Author("Nikita", "Khramov")
        )

        val requestEntity: RequestEntity<BookDTO> = RequestEntity(
            myNewBookDTO,
            HttpMethod.PUT,
            URI("http://localhost:$port/api/v1/book/updateEdition")
        )
        assertThat(
            restTemplate.exchange(
                requestEntity,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Throws(Exception::class)
    fun updateExistingBookShouldReturnNotFound() {
        val myNewBookDTO = BookDTO(
            1,
            "To Have and Have Not 2",
            "Coolest book!",
            "77777777",
            Year.of(2022),
            true,
            Author("Nikita", "Khramov")
        )

        val requestEntity: RequestEntity<BookDTO> = RequestEntity(
            myNewBookDTO,
            HttpMethod.PUT,
            URI("http://localhost:$port/api/v1/book/updateEdition")
        )
        assertThat(
            restTemplate!!.exchange(
                requestEntity,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Throws(Exception::class)
    fun deleteExistingBookByISBNShouldReturnOK() {
        assertThat(
            restTemplate!!.postForEntity(
                "http://localhost:$port/api/v1/book/addBook",
                newBookDTO,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.CREATED)

        val requestEntity: RequestEntity<BookDTO> = RequestEntity(
            null,
            HttpMethod.DELETE,
            URI("http://localhost:$port/api/v1/book/deleteBookByISBN?isbn=${newBookDTO.isbn}")
        )

        assertThat(
            restTemplate.exchange(
                requestEntity,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNotExistingBookByISBNShouldReturnOK() {

        val requestEntity: RequestEntity<BookDTO> = RequestEntity(
            null,
            HttpMethod.DELETE,
            URI("http://localhost:$port/api/v1/book/deleteBookByISBN?isbn=${newBookDTO.isbn}")
        )

        assertThat(
            restTemplate!!.exchange(
                requestEntity,
                String::class.java
            ).statusCode
        ).isEqualTo(HttpStatus.NOT_FOUND)
    }
}