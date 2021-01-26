package com.springReactive.router

import com.springReactive.handler.ItemsHandler
import com.springReactive.util.ADD_ITEM
import com.springReactive.util.FUNCTIONAL
import com.springReactive.util.ID_URI
import com.springReactive.util.ITEMS_URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
open class ItemsRouter {

    @Bean
    open fun itemRouter(itemsHandler: ItemsHandler) : RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET("$FUNCTIONAL$ITEMS_URI").and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getAllItems)
            .andRoute(GET("$FUNCTIONAL$ID_URI").and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getItem)
            .andRoute(POST("$FUNCTIONAL$ADD_ITEM").and(accept(MediaType.APPLICATION_JSON)), itemsHandler::createItem)
    }
}