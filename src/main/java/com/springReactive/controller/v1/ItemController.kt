package com.springReactive.controller.v1

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import com.springReactive.util.ADD_ITEM
import com.springReactive.util.ID_URI
import com.springReactive.util.ITEMS_URI
import com.springReactive.util.V1_URI
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Slf4j
@RestController
@RequestMapping(V1_URI)
class ItemController {
    @Autowired
    lateinit var itemReactive: ItemReactive

    @GetMapping(ITEMS_URI)
    fun getAllItems(): Flux<Item> {
        return itemReactive.findAll()
    }

    @GetMapping(ID_URI)
    fun getOneItem(@PathVariable id: String): Mono<ResponseEntity<Item>> {
        return itemReactive.findById(UUID.fromString(id))
            .map { ResponseEntity(it, HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PostMapping(ADD_ITEM)
    @ResponseStatus(HttpStatus.CREATED)
    fun createItem(@RequestBody item: Item): Mono<Item> {
        return itemReactive.save(item);
    }

    /*
    get id and item to be updated
    using id get the item from DB
    update the item
    save the item
    return the updated item
     */

    @PutMapping(ID_URI)
    fun updateItem(@PathVariable id: String, @RequestBody item: Item): Mono<ResponseEntity<Item>> {
        return itemReactive.findById(UUID.fromString(id))
            .flatMap { it.price = item.price; it.desc = item.desc; itemReactive.save(it) }
            .map { ResponseEntity(it, HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }


}