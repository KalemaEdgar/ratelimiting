package com.ratelimiting.ratelimiting.controllers;

import com.ratelimiting.ratelimiting.models.Request;
import com.ratelimiting.ratelimiting.services.PricingPlanService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/ratelimit")
public class RateLimitController
{
    private Bucket bucket;

    @Autowired
    private PricingPlanService pricingPlanService;

    // Autowire the service instead of pulling it in through a constructor
    // private final PricingPlanService pricingPlanService;
    // public RateLimitController(PricingPlanService pricingPlanService) {
    //     this.pricingPlanService = pricingPlanService;
    // }

    /**
     * Route to generate token using the API
     * @return ResponseEntity
     * @// TODO: 26/03/2022 clientToken needs to be part of the RequestBody (json parameter) instead of a PathVariable (part of the URL).
     */
    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody Request apiToken)
    {
        String clientToken = apiToken.getToken();
        // Refill the bucket every minute. Bucket can have up-to 5 tokens
        //Refill refill = Refill.greedy(5, Duration.ofMinutes(1));

        // Create the bandwidth -- The default number of tokens
        //int initialTokens = 2;
        //Bandwidth limit = Bandwidth.classic(5, refill)
        //    .withInitialTokens(initialTokens); // Want to have a lesser initial size, like for a cold start to prevent denial of service

        // Check if the token supplied is valid
        if ( ! pricingPlanService.isTokenValid(apiToken.getToken())) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.UNAUTHORIZED);
        }

        // Create the bucket
        bucket = pricingPlanService.getPlanServiceBucket(clientToken);

        return new ResponseEntity<String>("Token is valid. " + bucket.toString(), HttpStatus.OK);
    }

    /**
     * Consume the token
     * @return ResponseEntity
     */
    @GetMapping("/consume-request")
    public ResponseEntity<String> demo(@RequestBody Request apiToken)
    {
        if ( ! pricingPlanService.isTokenValid(apiToken.getToken())) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.UNAUTHORIZED);
        }

        if (bucket.tryConsume(1)) // Try and consume a token
        {
            System.out.println("======== API working successfully ========");
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }
        System.out.println("======== Too many hits. Please try again later ========");
        return new ResponseEntity<String>("Too many requests. Please try again later", HttpStatus.TOO_MANY_REQUESTS);
    }
}
