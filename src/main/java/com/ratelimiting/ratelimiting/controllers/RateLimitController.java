package com.ratelimiting.ratelimiting.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/ratelimit")
public class RateLimitController {

    private Bucket bucket;

    /**
     * Route to generate token using the API
     * @return ResponseEntity
     */
    @GetMapping("/generate-token")
    public ResponseEntity<String> generateToken() {
        // Refill the bucket every minute. Bucket can have up-to 5 tokens
        Refill refill = Refill.greedy(5, Duration.ofMinutes(1));

        // Create the bandwidth -- The default number of tokens
        int initialTokens = 2;
        Bandwidth limit = Bandwidth
            .classic(5, refill)
            .withInitialTokens(initialTokens); // Want to have a lesser initial size, like for a cold start to prevent denial of service

        // Create the bucket
        bucket = Bucket.builder()
            .addLimit(limit)
            .build();

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
}