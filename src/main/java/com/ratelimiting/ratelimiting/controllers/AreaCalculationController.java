package com.ratelimiting.ratelimiting.controllers;

import com.ratelimiting.ratelimiting.models.AreaV1;
import com.ratelimiting.ratelimiting.models.RectangleDimensionsV1;
import com.ratelimiting.ratelimiting.models.TriangleDimensions;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping(value = "/api/v1/area")
class AreaCalculationController {
    private final Bucket bucket;

    // Rate limit that allows 20 requests per minute.
    // Reject request if it already has 20 requests in a time window of 1 minute
    // Add a Bucket and add the limit (Bandwidth)
    AreaCalculationController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping(value = "/rectangle")
    public ResponseEntity<AreaV1> rectangle(@RequestBody RectangleDimensionsV1 dimensions) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(new AreaV1("rectangle", dimensions.getLength() * dimensions.getWidth()));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping(value = "/triangle")
    public ResponseEntity<AreaV1> triangle(@RequestBody TriangleDimensions dimensions) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(new AreaV1("triangle", 0.5d * dimensions.getBase() * dimensions.getHeight()));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
