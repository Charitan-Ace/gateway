package com.charitan.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotificationRoute {
    @Bean
    fun notificationRoutes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route {
                path("/notification/ws/**")
                filters {
                    this.stripPrefix(1)
                    this.rewritePath("/ws/(?<remaining>.*)", "/ws/\${remaining}")
                    this.filter { exchange, chain ->
                        exchange.response.headers.remove("Access-Control-Allow-Origin")
                        exchange.response.headers.remove("Access-Control-Allow-Credentials")
                        return@filter chain.filter(exchange)
                    }
                }
                uri("lb:ws://NOTIFICATION")
            }
            route {
                path("/notification/**")
                filters {
                    this.stripPrefix(1)
                }
                uri("lb://NOTIFICATION")
            }
        }
}
