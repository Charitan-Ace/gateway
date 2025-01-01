package com.charitan.gateway.filter

import com.charitan.gateway.jwt.JwtExternalService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JweDecryptPreFilter(
    @Value("\${auth.cookie.name:charitan}")
    private val authenticationCookieKey: String,
    private val jwtService: JwtExternalService,
) : GlobalFilter {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
    ): Mono<Void> {
        val authCookie =
            exchange.request.cookies[authenticationCookieKey]
                ?.first()
                ?: return chain.filter(exchange)

        logger.debug("Parsing ${authCookie.name} cookie encrypted content")

        try {
            val jwe = authCookie.value.removePrefix("$authenticationCookieKey=")
            val jws = jwtService.parseJweContent(jwe)
            return chain.filter(
                exchange
                    .mutate()
                    .request(
                        exchange.request
                            .mutate()
                            .header("Cookie", "$authenticationCookieKey=$jws")
                            .build(),
                    ).build(),
            )
        } catch (e: Exception) {
            logger.error(e.message)
        }

        return chain.filter(exchange)
    }
}
