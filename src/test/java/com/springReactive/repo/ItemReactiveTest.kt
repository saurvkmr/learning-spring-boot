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
        Item(UUID.randomUUID(), "Tata Car", 64.5F),
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
            .expectNextCount(4)
            .verifyComplete()
    }
}