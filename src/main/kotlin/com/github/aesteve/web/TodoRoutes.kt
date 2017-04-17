package com.github.aesteve.web

import com.github.aesteve.services.TodoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod.*
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.router

@Configuration
class TodoRoutes(val todos: TodoService) {

    @Bean
    fun filter() =
        ReactiveCorsFilter(
            "*",
            listOf(GET, POST, PUT, PATCH, DELETE),
            listOf(CONTENT_TYPE)
        )


    @Bean
    fun router() = router {
        (accept(APPLICATION_JSON_UTF8) and "/todos").nest {
            OPTIONS("/*", { noContent().build() })
            GET("/", todos::list)
            POST("/", todos::create)
            DELETE("/", todos::clear)
            GET("/{id}", todos::get)
            PATCH("/{id}", todos::update)
            DELETE("/{id}", todos::remove)
        }
    }
}
