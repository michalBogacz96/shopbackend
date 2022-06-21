package app.shop

import app.shop.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(value = ["app.shop.repository"])
@EntityScan(value = ["app/shop/entity"])
@EnableConfigurationProperties(value = [AppProperties::class])
class ShopApplication

fun main(args: Array<String>) {
    runApplication<ShopApplication>(*args)
}
