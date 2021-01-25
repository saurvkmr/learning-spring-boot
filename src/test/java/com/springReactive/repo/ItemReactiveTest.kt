package com.springReactive.repo

import com.springReactive.document.Item
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemReactiveTest {
    @Autowired
    lateinit var itemReactive: ItemReactive

    private val items = arrayListOf(Item(UUID.randomUUID(), "Samsung TV", 4.5F),
        Item(UUID.randomUUID(), "Apple Watch", 14.5F),
        Item(UUID.randomUUID(), "OnePlus", 24.5F),
        Item(UUID.randomUUID(), "Tata Car", 64.5F),//47faa51c-f270-4564-a989-4300d5205c8d
        Item(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d"), "Airpod", 98.9F)
    )

    /*private val items = arrayListOf(Item(null, "Samsung TV", 4.5F),
        Item(null, "Apple Watch", 14.5F),
        Item(null, "OnePlus", 24.5F),
        Item(null, "Tata Car", 64.5F),
    )*/

    @BeforeAll
    fun setUp() {
        //println("saurav")
        itemReactive.deleteAll()
            .thenMany(Flux.fromIterable(items))
            .flatMap(itemReactive::save)
            .doOnNext { println(it.toString()) }
            .blockLast()
    }

    @Test
    fun findAllElements() {
        StepVerifier.create(itemReactive.findAll())
            .expectSubscription()
            .expectNextCount(5)
            .verifyComplete()
    }

    @Test
    fun getItemById() {
        StepVerifier.create(itemReactive.findById(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d")))
            .expectSubscription()
            .expectNextMatches { it.price ==  98.9F}
            .verifyComplete()
    }

    @Test
    fun getItemByDesc() {
        StepVerifier.create(itemReactive.findByDesc("Airpod").log())
            .expectSubscription()
            .expectNextMatches { it.price ==  98.9F}
            .verifyComplete()
    }

    @Test
    fun saveItem() {
        val item = Item(UUID.randomUUID(), "BMW", 64.5F)
        StepVerifier.create(itemReactive.save(item))
            .expectSubscription()
            .expectNextCount(1)
            .expectNextMatches { it.desc == "BMW" }
            .expectComplete()

        itemReactive.delete(item)
    }

    @Test
    fun updateItem() {
        val flux = itemReactive.findByDesc("OnePlus")
            .map { it.price = 99F; return@map it }
            .flatMap { itemReactive.save(it); return@flatMap Flux.just(it) }

        StepVerifier.create(flux.log())
            .expectSubscription()
            .expectNextCount(1)
            //.expectNextMatches { it.price == 99.0F }
            .verifyComplete()

    }
}