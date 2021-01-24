package com.springReactive.handler

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun sampleHandlerApproach_1() {
        val response = webTestClient.get()
            .uri("/functional/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            //.returnResult()
    }
}