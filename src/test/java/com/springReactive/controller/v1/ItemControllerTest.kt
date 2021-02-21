package com.springReactive.controller.v1

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import com.springReactive.util.ADD_ITEM
import com.springReactive.util.ID_URI
import com.springReactive.util.ITEMS_URI
import com.springReactive.util.V1_URI
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
import reactor.core.publisher.Mono
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
        webTestClient.get().uri("$V1_URI$ITEMS_URI")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Item::class.java)
            .hasSize(5)
    }

    @Test
    fun testAllItemsCheckId() {
        webTestClient.get().uri("$V1_URI$ITEMS_URI")
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
        val itemFlux : Flux<Item> = webTestClient.get().uri("$V1_URI$ITEMS_URI")
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

    @Test
    fun getOneItemTest() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8d"
        webTestClient.get().uri("$V1_URI$ID_URI", id)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.price", 98.9)
    }

    @Test
    fun getOneItemTestNotFound() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8e"
        webTestClient.get().uri("$V1_URI$ID_URI", id)
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
    }

    @Test
    fun createTest() {
        val item = Item(UUID.randomUUID(), "Macbook", 1999.0F)
        webTestClient.post().uri("$V1_URI$ADD_ITEM")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Item::class.java)
            .consumeWith<Nothing> { it.responseBody.price == 1999.0F }
    }

    @Test
    fun createTest_2() {
        val item = Item(UUID.randomUUID(), "Macbook", 1999.0F)
        webTestClient.post().uri("$V1_URI$ADD_ITEM")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.desc").isEqualTo("Macbook")
            .jsonPath("$.price").isEqualTo(1999.0F)
    }

    @Test
    fun updateItem() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8d"
        val item = Item(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d"), "Airpod", 198.9F)
        webTestClient.put().uri("$V1_URI$ID_URI", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.price").isEqualTo(198.9F)
    }

    @Test
    fun updateItem_NotFound() {
        val id = "47faa51c-f270-4564-a989-4300d5205c8e"
        val item = Item(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d"), "Airpod", 198.9F)
        webTestClient.put().uri("$V1_URI$ID_URI", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun exceptionHandlerTest() {
        webTestClient.get().uri("$V1_URI$ITEMS_URI/exception")
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .isEqualTo<Nothing>("Forced Exception")
    }
}