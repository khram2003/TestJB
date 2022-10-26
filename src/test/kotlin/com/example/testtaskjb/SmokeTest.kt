package com.example.testtaskjb

import com.example.testtaskjb.controllers.BookController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SmokeTest {
    @Autowired
    private val controller: BookController? = null

    @Test
    @Throws(Exception::class)
    fun contextLoads() {
        assertThat(controller).isNotNull
    }
}
