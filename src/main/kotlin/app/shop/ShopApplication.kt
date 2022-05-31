package app.shop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(value = ["app.shop.repository"])
@EntityScan(value = ["app/shop/entity"])
class ShopApplication

fun main(args: Array<String>) {
    runApplication<ShopApplication>(*args)
}
