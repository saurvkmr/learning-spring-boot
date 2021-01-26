package com.springReactive.initializer

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

@Component
class ItemInitializer : CommandLineRunner {
    @Autowired
    lateinit var itemReactive: ItemReactive
    override fun run(vararg args: String?) {
        initialDataSetup()
    }

    private fun initialDataSetup() {
        itemReactive.deleteAll()
            .thenMany(Flux.fromIterable(items()))
            .flatMap { item -> itemReactive.save(item) }
            .thenMany(itemReactive.findAll())
            .subscribe { println(it) }
    }

    private fun items(): MutableList<Item> {
        return arrayListOf(Item(UUID.randomUUID(), "Samsung TV", 4.5F),
            Item(UUID.randomUUID(), "Apple Watch", 14.5F),
            Item(UUID.randomUUID(), "OnePlus", 24.5F),
            Item(UUID.randomUUID(), "Tata Car", 64.5F),
            Item(UUID.fromString("47faa51c-f270-4564-a989-4300d5205c8d"), "Airpod", 98.9F))
    }

}