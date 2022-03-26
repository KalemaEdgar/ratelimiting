package com.ratelimiting.ratelimiting.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * This class returns the bandwidth and the bucket per client
 * to the RateLimit API functionality
 * So we need to AutoWire this Service into the RateLimitController class
 */

@Service
public class PricingPlanService {

    // This could come from the database
    @Value("${rate.limit.client.basic}")
    private String basic;

    @Value("${rate.limit.client.free}")
    private String free;

    public Bucket getPlanServiceBucket(String clientToken)
    {
        // Create the bucket based on the client token
        return Bucket.builder()
            .addLimit(getClientBandWidth(clientToken))
            .build();
    }

    /**
     * Get the client's bandwidth / pricing plan from their token
     * @return Bandwidth
     */
    private Bandwidth getClientBandWidth(String clientToken)
    {
        if (clientToken.equals(free))
            return Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))
                    .withInitialTokens(5);
        else if (clientToken.equals(basic))
            return Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)))
                    .withInitialTokens(10);

        // In case the client doesn't have a plan
        return Bandwidth.classic(2, Refill.greedy(2, Duration.ofMinutes(1)));
    }
}
