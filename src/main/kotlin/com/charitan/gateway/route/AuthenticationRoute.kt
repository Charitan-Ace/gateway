package com.charitan.gateway.route

import com.charitan.gateway.jwt.JwtExternalService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

@Configuration
class AuthenticationRoute {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun authenticationRoutes(
        builder: RouteLocatorBuilder,
        jwtService: JwtExternalService,
        objectMapper: ObjectMapper,
    ) = builder.routes {
        // decrypt body jwe
        route {
            path("/api/auth/register", "api/auth/login")
            filters {
                this.modifyRequestBody(
                    String::class.java,
                    String::class.java,
                    MediaType.APPLICATION_JSON_VALUE,
                ) { _, body ->
                    val jwt = jwtService.parseJweClaims(body)
                    logger.debug("Parsing login/register body encrypted content")
                    Mono.just(objectMapper.writeValueAsString(jwt))
                }
            }
            uri("lb://AUTH")
        }
        // route everything else
        route {
            path("/api/auth/**")
            uri("lb://AUTH")
        }
    }

    @Bean
    fun keyRoutes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route {
                path("/.well-known/**")
                uri("lb://AUTH")
            }
        }
}
