package com.charitan.gateway.jwt.internal

import io.jsonwebtoken.security.Jwks
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback
import org.springframework.stereotype.Component
import java.security.Key
import java.security.PrivateKey

@Component
internal class JwtConsumer(
    private val jwtService: JwtServiceImpl,
) : AbstractConsumerSeekAware() {
    private val logger = LoggerFactory.getLogger(JwtConsumer::class.java)

    @KafkaListener(topics = ["key.encryption.private.change"])
    fun encPrivateKeyConsumer(jwkString: String) {
        val jwk: Key =
            Jwks
                .parser()
                .build()
                .parse(jwkString)
                .toKey()
        if (jwk is PrivateKey) {
            jwtService.encPrivateKey = jwk
            logger.info("Encryption ${jwk.format} private key updated")
        }
    }

    override fun onPartitionsAssigned(
        assignments: MutableMap<TopicPartition, Long>,
        callback: ConsumerSeekCallback,
    ) {
        assignments.keys.forEach { topicPartition ->
            logger.info("Trying to change partition offset ${topicPartition.topic()}...")
            callback.seekRelative(topicPartition.topic(), topicPartition.partition(), -1, false)
        }
    }
}
