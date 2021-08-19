package me.demo.jwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoJwtApplication

fun main(args: Array<String>) {
    runApplication<DemoJwtApplication>(*args)
}
