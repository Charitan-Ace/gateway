package com.charitan.gateway.filter

import com.charitan.gateway.jwt.JwtExternalService
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
    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
    ): Mono<Void> {
        val authCookie =
            exchange.request.cookies[authenticationCookieKey]
                ?.first()
                ?: return chain.filter(exchange)

        val jws = jwtService.parseJweContent(authCookie.value.split("=").last())

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
    }
}
