package app.shop.database

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "database")
class DatabaseProperties {

    var user: String = String()
    var password: String = String()
}