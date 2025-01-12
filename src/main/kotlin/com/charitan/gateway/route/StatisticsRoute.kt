package com.charitan.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StatisticsRoute {
    @Bean
    fun statisticsRoutes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route {
                path("/api/statistics/**")
                uri("lb://STATISTICS")
            }
        }
}
