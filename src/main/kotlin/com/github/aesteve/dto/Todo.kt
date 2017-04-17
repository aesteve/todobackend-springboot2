package com.github.aesteve.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono


// FIXME : I'm sure this can be re-written in a cleaner way, using data classes properly
data class Todo(
        @get: JsonProperty("id")
        @set: JsonProperty("id")
        var id: String?,
        @get: JsonProperty("title")
        @set: JsonProperty("title")
        var title: String?,
        @get: JsonProperty("order")
        @set: JsonProperty("order")
        var order: Int?,
        @get: JsonProperty("completed")
        @set: JsonProperty("completed")
        var completed: Boolean = false
) {
    constructor(): // FIXME : for Jackson ? but why register the kotlinModule, then ?
        this(null, null, null, false)

    var url : String = ""
        get() { return "http://localhost:8080/todos/$id" }

    // FIXME : there must be a better way to write this
    fun merge(other: Todo): Update {
        val upd = Update.update("id", id)
        if (other.title != null && other.title != title)
            upd.set("title", other.title)
        if (other.order != null && other.order != order)
            upd.set("order", other.order)
        if (other.completed != completed)
            upd.set("completed", other.completed)
        return upd
    }
}
