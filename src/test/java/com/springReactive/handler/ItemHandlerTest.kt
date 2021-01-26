package com.springReactive.handler

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import com.springReactive.util.FUNCTIONAL
import com.springReactive.util.ITEMS_URI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemHandlerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var itemReactive: ItemReactive

    @BeforeAll
    fun setUp() {
        itemReactive.deleteAll()
            .thenMany(Flux.fromIterable(items()))
            .flatMap { item -> itemReactive.save(item) }
            .blockFirst()
    }

    private fun items(): MutableList<Item> {
        return arrayListOf(Item(UUID.randomUUID(), "Samsung TV", 4.5F),
            Item(UUID.randomUUID(), "Apple Watch", 14.5F),
            Item(UUID.randomUUID(), "OnePlus", 24.5F),
            Item(UUID.randomUUID(), "Tata Car", 64.5F),
            Item(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d"), "Airpod", 98.9F))
    }

    @Test
    fun getAllItems() {
        webTestClient.get().uri("$FUNCTIONAL$ITEMS_URI")
            .exchange()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(5)
    }

    @Test
    fun validateIdNotNull() {
        webTestClient
            .get()
            .uri("$FUNCTIONAL$ITEMS_URI")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Item::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Item>> {
                it.responseBody.forEach { item ->
                    Assertions.assertNotNull(item.id)
                }
            }
    }

    @Test
    fun validateUsingStepVerifier() {
        val itemFlux: Flux<Item> = webTestClient
            .get()
            .uri("$FUNCTIONAL$ITEMS_URI")
            .exchange()
            .expectStatus().isOk
            .returnResult(Item::class.java)
            .responseBody

        StepVerifier.create(itemFlux)
            .expectSubscription()
            .expectNextCount(5)
            .verifyComplete()
    }
}