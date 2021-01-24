package com.springReactive.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
class MonoControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun testMono() {
        val expected = "A"
        val responseBody = webTestClient.get().uri("/mono")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assert(expected == responseBody)
    }
}