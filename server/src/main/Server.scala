package main

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Profile}


@SpringBootApplication
class Server

object Application extends App {
    SpringApplication.run(classOf[Server])
}