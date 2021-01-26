package com.springReactive.handler

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Component
class ItemsHandler {
    @Autowired
    lateinit var itemReactive: ItemReactive

    private val notFound: Mono<ServerResponse> = ServerResponse.notFound().build()

    fun getAllItems(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(itemReactive.findAll(), Item::class.java)
    }

    fun getItem(request: ServerRequest): Mono<ServerResponse> {
        val id = UUID.fromString(request.pathVariable("id"))
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(itemReactive.findById(id), Item::class.java) // this is returning empty body
            .switchIfEmpty(notFound)
    }

    fun createItem(request: ServerRequest): Mono<ServerResponse> {
        val itemMono = request.bodyToMono(Item::class.java)
        return itemMono.flatMap {
            ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactive.save(it), Item::class.java)
        }
    }

    fun deleteItem(request: ServerRequest): Mono<ServerResponse> {
        val id = UUID.fromString(request.pathVariable("id"))
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(itemReactive.deleteById(id), Void::class.java)
            .switchIfEmpty(notFound)
    }

    fun updateItem(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val updatedItem: Mono<Item> = request.bodyToMono(Item::class.java)
            .flatMap {
                return@flatMap itemReactive.findById(UUID.fromString(id))
                    .flatMap { item ->
                        run {
                            item.price = it.price
                            item.desc = it.desc
                        }
                        return@flatMap itemReactive.save(item)
                    }
            }
        return updatedItem.flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(it, Item::class.java)
                .switchIfEmpty(notFound)
        }
    }
}