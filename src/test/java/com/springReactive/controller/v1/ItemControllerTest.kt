package com.springReactive.controller.v1

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ItemControllerTest {
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
    fun testAllItems() {
        webTestClient.get().uri("/v1/items")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Item::class.java)
            .hasSize(5)
    }

    @Test
    fun testAllItemsCheckId() {
        webTestClient.get().uri("/v1/items")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Item::class.java)
            .hasSize(5)
            .consumeWith<WebTestClient.ListBodySpec<Item>> {
                it.responseBody
                    .forEach { item -> Assertions.assertTrue(item.id != null) }
            }
    }

    @Test
    fun testAllItemsUsingStepVerifier() {
        val itemFlux : Flux<Item> = webTestClient.get().uri("/v1/items")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .returnResult(Item::class.java)
            .responseBody

        /*StepVerifier.create(itemFlux.log("Test log "))
            .expectSubscription()
            .assertNext{ it.id != null}
            .assertNext{ it.id != null}
            .assertNext{ it.id != null}
            .assertNext{ it.id != null}
            .assertNext{ it.id != null}
            .verifyComplete()*/

        StepVerifier.create(itemFlux)
            .expectSubscription()
            .expectNextCount(5)
            .verifyComplete()
    }
}