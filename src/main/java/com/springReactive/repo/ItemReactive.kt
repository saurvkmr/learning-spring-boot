package com.springReactive.repo

import com.springReactive.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface ItemReactive : ReactiveMongoRepository<Item, UUID> {}