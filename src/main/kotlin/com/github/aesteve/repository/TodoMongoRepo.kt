package com.github.aesteve.repository

import com.github.aesteve.dto.Todo
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TodoMongoRepo(val tpl: ReactiveMongoTemplate) {

    fun list(): Flux<Todo> =
        tpl.find(Query().with(Sort.by("id")), Todo::class.java)

    fun get(id: String): Mono<Todo> =
        tpl.findById(id, Todo::class.java)

    fun create(todo: Mono<Todo>): Mono<Todo> =
        tpl.save(todo)

    fun update(id: String, changes: Update): Mono<Todo> =
        tpl.findAndModify(Query(Criteria.where("id").`is`(id)), changes, FindAndModifyOptions().returnNew(true), Todo::class.java)

    fun remove(todo: Mono<Todo>): Mono<DeleteResult> =
        tpl.remove(todo)

    fun clear(): Mono<DeleteResult> =
        tpl.remove(Query(), Todo::class.java)

}
