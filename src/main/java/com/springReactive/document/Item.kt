package com.springReactive.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Item(
    @Id
    var id: UUID?,
    var desc: String?,
    var price: Float?
)