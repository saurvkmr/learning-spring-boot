package com.springReactive.handler

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ItemsHandler {
    @Autowired
    lateinit var itemReactive: ItemReactive

    fun getAllItems(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(itemReactive.findAll(), Item::class.java)
    }
}