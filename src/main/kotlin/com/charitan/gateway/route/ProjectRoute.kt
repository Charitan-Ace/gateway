package com.charitan.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProjectRoute {
    @Bean
    fun projectRoutes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route {
                path("/project/**")
                filters {
                    this.stripPrefix(1)
                }
                uri("lb://PROJECT")
            }
        }
}
