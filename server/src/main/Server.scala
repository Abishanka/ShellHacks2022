package main

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Profile}
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import abi.dex.AbiDex


@SpringBootApplication
class Server {

}

object Application extends App {
    AbiDex.init(dirty = true)
    SpringApplication.run(classOf[Server])
}