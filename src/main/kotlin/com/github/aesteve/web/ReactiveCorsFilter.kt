package com.github.aesteve.web

import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpMethod
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


val DEFAULT_METHODS = listOf(GET, POST, PUT, PATCH, DELETE, OPTIONS)
val ALL_METHODS = DEFAULT_METHODS + listOf(TRACE, HEAD)

class ReactiveCorsFilter(
        val pattern: String = "*",
        val allowedMethods: List<HttpMethod> = DEFAULT_METHODS,
        val headers: List<String>
) : WebFilter {


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.response.headers.set(ACCESS_CONTROL_ALLOW_HEADERS, headers.joinToString(","))
        exchange.response.headers.set(ACCESS_CONTROL_ALLOW_ORIGIN, pattern)
        exchange.response.headers.set(ACCESS_CONTROL_ALLOW_METHODS, allowedMethods.joinToString(", "))
        return chain.filter(exchange)
    }

}
