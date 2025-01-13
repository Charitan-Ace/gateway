package com.charitan.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StatementRoute {
    @Bean
    fun statementRoutes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route {
                path("/api/statement/**")
                uri("lb://STATEMENT")
            }
        }
}
