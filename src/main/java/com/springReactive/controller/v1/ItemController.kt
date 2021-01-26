package com.springReactive.controller.v1

import com.springReactive.document.Item
import com.springReactive.repo.ItemReactive
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.*

@Slf4j
@RestController
@RequestMapping("/v1")
class ItemController {
    @Autowired
    lateinit var itemReactive: ItemReactive

    @GetMapping("/items")
    fun getAllItems(): Flux<Item> {
        return itemReactive.findAll()
    }
}