package com.notesSpringProj.rateLimiting;

import org.springframework.stereotype.Component;

import com.notesSpringProj.config.AppConstants;

import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiter {

    // Map to store request counts for each client
    private final Map<String, Long> requestCounts = new HashMap<>();

    /**
     * Checks if a request from a client is allowed based on rate-limiting criteria.
     *
     * @param clientId The unique identifier for the client making the request.
     * @return true if the request is allowed, false if it exceeds the rate limit.
     */
    public boolean allowRequest(String clientId) {
        // Generate a unique key for the client
        String key = getClientKey(clientId);

        // Get the current time in seconds
        long currentTime = System.currentTimeMillis() / 1000;

        // Initialize the last request time if not present
        requestCounts.putIfAbsent(key, currentTime);

        // Retrieve the last request time for the client
        long lastRequestTime = requestCounts.get(key);

        // Check if the current time is within the time window
        if (currentTime - lastRequestTime < AppConstants.TIME_WINDOW_SECONDS) {
            // Increment the request count for the client
            long requestCount = requestCounts.compute(key + "-count", (k, v) -> v == null ? 1 : v + 1);

            // Check if the request count exceeds the maximum allowed requests
            if (requestCount >= AppConstants.MAX_REQUESTS) {
                return false; // Too many requests, deny the request
            } else {
                // Update the request count
                requestCounts.put(key + "-count", requestCounts.get(key + "-count") + 1);
            }
        } else {
            // Reset the count for the new time window
            requestCounts.put(key, currentTime);
            requestCounts.put(key + "-count", 1L);
        }

        return true; // Request is allowed
    }

    /**
     * Generates a unique key for the client based on the client ID.
     *
     * @param clientId The client ID.
     * @return A unique key for the client.
     */
    private String getClientKey(String clientId) {
        return "client-" + clientId;
    }
}
