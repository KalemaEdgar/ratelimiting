package com.ratelimiting.ratelimiting.controllers;

import com.ratelimiting.ratelimiting.models.Request;
import com.ratelimiting.ratelimiting.services.PricingPlanService;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @// TODO: 29/03/2022 clientToken needs to be part of the RequestBody (json parameter) instead of a PathVariable (part of the URL).
     */
    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody Request apiToken)
    {
        String clientToken = apiToken.getToken();

        // Check if the token supplied is valid
        if ( ! pricingPlanService.isTokenValid(apiToken.getToken())) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.UNAUTHORIZED);
        }

        // Create the bucket
        bucket = pricingPlanService.getPlanServiceBucket(clientToken);

        //return new ResponseEntity<String>("Token is valid. " + bucket.toString(), HttpStatus.OK);
        return new ResponseEntity<String>("Client key is valid. Tokens setup successfully", HttpStatus.OK);
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
