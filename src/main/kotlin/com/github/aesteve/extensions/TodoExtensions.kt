package com.github.aesteve.extensions

import com.github.aesteve.dto.Todo
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

fun ServerRequest.bodyAsTodo(): Mono<Todo> =
    this.bodyToMono(Todo::class.java)
