package app.shop.database

import com.mchange.v2.c3p0.ComboPooledDataSource
import kotlin.Throws
import java.beans.PropertyVetoException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*
import javax.sql.DataSource

@Configuration
class JPAConfig {


    @get:Throws(PropertyVetoException::class)
    @get:Bean(name = ["dataSource"])
    val dataSource: DataSource
        get() {
            val dataSource = ComboPooledDataSource()
            dataSource.driverClass = "com.mysql.cj.jdbc.Driver"
            dataSource.jdbcUrl =
                    "jdbc:mysql://eu-cdbr-west-02.cleardb.net:3306/heroku_5e862376badc49f?useSSL=false&serverTimezone=UTC"
            dataSource.user = "b5847ae296e239"
            dataSource.password = "44a2b9ad"
            /* Connection pool properties */
            dataSource.initialPoolSize = 5
            dataSource.minPoolSize = 5
            dataSource.maxPoolSize = 300
            dataSource.maxIdleTime = 30000
            return dataSource
        }

    @get:Throws(PropertyVetoException::class)
    @get:Bean(name = ["entityManagerFactory"])
    val factorySession: LocalSessionFactoryBean
        get() {
            val sessionFactoryBean = LocalSessionFactoryBean()
            val dataSource = dataSource
            sessionFactoryBean.setDataSource(dataSource)
            sessionFactoryBean.setPackagesToScan("app.shop")
            val hibernateProperties = Properties()
            hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
            hibernateProperties.setProperty("hibernate.show_sql", "true")
            sessionFactoryBean.hibernateProperties = hibernateProperties
            return sessionFactoryBean
        }
}