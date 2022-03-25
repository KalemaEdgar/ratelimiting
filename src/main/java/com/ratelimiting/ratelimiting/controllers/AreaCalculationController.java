package com.ratelimiting.ratelimiting.controllers;

import com.ratelimiting.ratelimiting.models.Area;
import com.ratelimiting.ratelimiting.models.Rectangle;
import com.ratelimiting.ratelimiting.models.Triangle;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
//@RequestMapping(value = "/api/v1/area")
@RequestMapping("/api/v1/area")
class AreaCalculationController {
    private Bucket bucket;

    // Rate limit that allows 5 requests per minute.
    // Reject request if it already has 5 requests in a time window of 1 minute
    // Add a Bucket and add the limit (Bandwidth)
    AreaCalculationController() {
        //Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        //this.bucket = Bucket.builder()
        //        .addLimit(limit)
        //        .build();
    }

    /**
     * Route to generate token using the API
     * @return ResponseEntity
     */
    @GetMapping("/generate-token")
    public ResponseEntity<String> generateToken() {
        // Refill the bucket every minute. Bucket can have up-to 5 tokens
        Refill refill = Refill.greedy(5, Duration.ofMinutes(1));

        // Bucket - Create a bucket with initial tokens
        // Create the bandwidth -- The default number of tokens
        int initialTokens = 2;
        Bandwidth limit = Bandwidth
            .classic(5, refill)
            .withInitialTokens(initialTokens); // Want to have a lesser initial size, for example for the case of cold start in order to prevent denial of service
        bucket = Bucket.builder()
            .addLimit(limit)
            .build();

        // Consume Token will be in the route consuming the tokens
        //System.out.println(bucket);
        //System.out.println(initialTokens);
        //System.out.println(refill);

        return new ResponseEntity<String>("Token generated successfully. " + bucket.toString(), HttpStatus.OK);
    }

    /**
     * Consume the token
     * @return ResponseEntity
     */
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        if (bucket.tryConsume(1)) { // Try and consume a token
            System.out.println("======== API working successfully ========");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }
        System.out.println("======== Too many hits. Please try again later ========");
        return new ResponseEntity<String>("Too many requests. Please try again later", HttpStatus.TOO_MANY_REQUESTS);
    }

    @PostMapping(value = "/rectangle")
    public ResponseEntity<Area> rectangle(@RequestBody Rectangle dimensions) {
        if (bucket.tryConsume(1)) { // Try to consume 1 token
            return ResponseEntity.ok(new Area("rectangle", dimensions.getLength() * dimensions.getWidth()));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping(value = "/triangle")
    public ResponseEntity<Area> triangle(@RequestBody Triangle dimensions) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(new Area("triangle", 0.5d * dimensions.getBase() * dimensions.getHeight()));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
