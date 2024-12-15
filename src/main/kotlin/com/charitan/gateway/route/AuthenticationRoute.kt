package com.charitan.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthenticationRoute {
    @Bean
    fun authenticationRoutes(
        builder: RouteLocatorBuilder
    ): RouteLocator {
        return builder.routes {
            route() {
                path("/api/auth/**")
                uri("lb://AUTH")
            }
        }
    }

    @Bean
    fun keyRoutes(
        builder: RouteLocatorBuilder
    ): RouteLocator {
        return builder.routes {
            route() {
                path("/.well-known/**")
                uri("lb://AUTH")
            }
        }
    }

}
