package com.github.aesteve.services

import com.github.aesteve.dto.Todo
import com.github.aesteve.extensions.bodyAsTodo
import com.github.aesteve.extensions.json
import com.github.aesteve.repository.TodoMongoRepo
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI

@Service
class TodoService(val mongo: TodoMongoRepo) {

    fun create(req: ServerRequest) =
        mongo.create(req.bodyAsTodo()).flatMap {
            created(URI.create(it.url)).json().body(it.toMono())
        }

    fun get(req: ServerRequest) =
        mongo.get(req.pathVariable("id")).flatMap {
            when (it) {
                null -> notFound().build() // :( why HeadersBuilder vs. BodyBuilder ? how to write to body ?
                else -> ok().json().body(it.toMono())
            }
        }

    fun update(req: ServerRequest): Mono<ServerResponse> {
        val id = req.pathVariable("id")
        return mongo.get(id).flatMap { todo ->
            when (todo) {
                null -> notFound().build() // :( why HeadersBuilder vs. BodyBuilder ? how to write to body ?
                else -> {
                    req.bodyAsTodo().flatMap { changes ->
                        ok().json().body(mongo.update(id, todo.merge(changes)))
                    }
                }
            }
        }
    }

    fun clear(req: ServerRequest) =
        mongo.clear().flatMap { _ -> noContent().build() }

    fun list(req: ServerRequest) =
        ok().json().body(mongo.list())

    fun remove(req: ServerRequest) =
        mongo.get(req.pathVariable("id")).flatMap {
            when (it) {
                null -> notFound().build() // :( why HeadersBuilder vs. BodyBuilder ? how to write to body ?
                else -> mongo.remove(it.toMono()).flatMap {
                    noContent().build()
                }

            }
        }

}
