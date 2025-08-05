package service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimiterService {
    private val cache = ConcurrentHashMap<String, Bucket>()

    fun resolveBucket(key: String): Bucket {
        return cache.computeIfAbsent(key) {
            val limit = Bandwidth.classic(
                100,
                Refill.greedy(100, Duration.ofMinutes(1))
            )
            Bucket.builder().addLimit(limit).build()
        }
    }
}
