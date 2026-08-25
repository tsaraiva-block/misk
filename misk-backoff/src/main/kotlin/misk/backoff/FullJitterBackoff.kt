package misk.backoff

import java.time.Duration

/**
 * Performs exponential backoff with 100% jitter: the delay doubles on each attempt up to `maxDelay`, then a random
 * amount of up to that same delay is added on top, so each wait lands somewhere in `[delay, 2 * delay]`.
 *
 * A wait therefore never falls below the exponential delay, and can reach twice `maxDelay`. This is not the "full
 * jitter" algorithm from AWS, which draws from `[0, delay]` and never exceeds `maxDelay`.
 *
 * Durations are supplied as functions, so that they can change dynamically as the system is running (e.g. in response
 * to changes in dynamic flags)
 */
class FullJitterBackoff(baseDelay: () -> Duration, maxDelay: () -> Duration) :
  ExponentialBackoff(baseDelay, maxDelay, { curDelayMs -> Duration.ofMillis(curDelayMs + 1) })
