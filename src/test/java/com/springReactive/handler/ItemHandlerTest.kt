package com.springReactive.handler

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import com.springReactive.util.*
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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    @Test
    fun getOneItem() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8d"
        webTestClient.get().uri("$FUNCTIONAL$ID_URI", id)
            .exchange()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(1)
    }

    @Test
    fun getOneItemValidatePrice() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8d"
        webTestClient.get().uri("$FUNCTIONAL$ID_URI", id)
            .exchange()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.price", 98.9F)
    }

    @Test
    fun getOneItemValidateNotFound() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8e"
        webTestClient.get().uri("$FUNCTIONAL$ID_URI", id)
            .exchange()
            .expectBody()
            .isEmpty
        //.expectStatus().isNotFound
    }

    @Test
    fun createItemTest() {
        val item = Item(UUID.randomUUID(), "Tesla Car", 64.5F)
        webTestClient.post().uri("$FUNCTIONAL$ADD_ITEM")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isAccepted
            .expectBody(Item::class.java)
            .value<Nothing> { it.price == 64.5F }
    }

    @Test
    fun deleteItem() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8d"
        webTestClient.delete().uri("$FUNCTIONAL$DELETE_ITEM$ID_URI", id)
            .exchange()
            .expectStatus().isOk

        webTestClient.get().uri("$FUNCTIONAL$ITEMS_URI")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(items().size - 1)
    }
}