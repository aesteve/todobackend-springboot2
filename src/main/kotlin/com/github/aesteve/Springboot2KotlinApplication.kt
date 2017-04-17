package com.github.aesteve

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

@SpringBootApplication
class Springboot2KotlinApplication

fun main(args: Array<String>) {


    val starter = MongodStarter.getDefaultInstance()

	val ip = "localhost"
	val port = 27017
	val mongodConfig = MongodConfigBuilder()
		.version(Version.Main.PRODUCTION)
		.net(Net(ip, port, Network.localhostIsIPv6()))
		.build()

	val mongodExecutable = starter.prepare(mongodConfig)
	mongodExecutable.start()

	ObjectMapper().registerModule(KotlinModule()) // FIXME : I thought this'd help, but doesn't

	SpringApplication.run(Springboot2KotlinApplication::class.java, *args)
}
