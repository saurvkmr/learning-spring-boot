package com.springReactive.repo

import com.springReactive.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.util.*

interface ItemReactive : ReactiveMongoRepository<Item, UUID> {
    fun findByDesc(desc: String): Flux<Item>
}