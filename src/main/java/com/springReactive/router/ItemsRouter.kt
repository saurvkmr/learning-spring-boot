package com.springReactive.router

import com.springReactive.handler.ItemsHandler
import com.springReactive.util.FUNCTIONAL
import com.springReactive.util.ITEMS_URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
open class ItemsRouter {

    @Bean
    open fun itemRouter(itemsHandler: ItemsHandler) : RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET("$FUNCTIONAL$ITEMS_URI")
                .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getAllItems)
    }
}