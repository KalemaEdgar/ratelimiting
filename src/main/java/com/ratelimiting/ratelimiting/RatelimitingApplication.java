package com.ratelimiting.ratelimiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RatelimitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatelimitingApplication.class, args);

//		Implement Rate Limiting
//		Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
//		Bandwidth limit = Bandwidth.classic(10, refill);
//		Bucket bucket = Bucket4j.builder()
//				.addLimit(limit)
//				.build();
//
//		for (int i = 1; i <= 10; i++) {
//			// assertTrue(bucket.tryConsume(1));
//			System.out.println(bucket.tryConsume(1));
//		}
//		// assertFalse(bucket.tryConsume(1));
//		System.out.println(bucket.tryConsume(1));
//
//		//// Throttle our requests to honor the rate limit
//		System.out.println("Throttle our requests to honor the rate limit");
//		Bandwidth limit2 = Bandwidth.classic(1, Refill.intervally(1, Duration.ofSeconds(2)));
//		Bucket bucket2 = Bucket4j.builder()
//				.addLimit(limit)
//				.build();
//		//assertTrue(bucket.tryConsume(1)); // first request
//		System.out.println(bucket.tryConsume(1)); // first request
//		Executors.newScheduledThreadPool(1) // schedule another request for 2 seconds later
//			//.schedule(() -> assertTrue(bucket.tryConsume(1)), 2, TimeUnit.SECONDS);
//			.schedule(() -> System.out.println(bucket.tryConsume(1)), 2, TimeUnit.SECONDS);
//
//		System.out.println("Allow only 5 requests in a 20-second time window");
//		Bucket bucket3 = Bucket4j.builder()
//				.addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
//				.addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(20))))
//				.build();
//
//		for (int i = 1; i <= 5; i++) {
//			//assertTrue(bucket.tryConsume(1));
//			System.out.println(bucket.tryConsume(1));
//		}
//		//assertFalse(bucket.tryConsume(1));
//		System.out.println(bucket.tryConsume(1));
	}

}

