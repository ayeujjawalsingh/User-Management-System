package com.ujjawal.user_management_system.apigatewayservice.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.ujjawal.user_management_system.apigatewayservice.filters.AuthFilter.AuthResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            System.out.println("Incoming request path: " + path);

            // Skip token validation for /user/register and /auth/login APIs
            if (path.contains("/user/register") || path.contains("/auth/login") || path.contains("auth/refresh-token") || path.contains("password/forgot") || path.contains("password/reset")) {
                System.out.println("Skipping authentication for path: " + path);
                return chain.filter(exchange);
            }

            // Get the token from the request header
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            System.out.println("Token received: " + (token != null ? "Yes" : "No"));

            if (token == null || token.isEmpty()) {
                // If no token is found, block the request with 401 Unauthorized
                System.out.println("No token found, rejecting request.");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Call the auth-service to validate the token
            return webClientBuilder.build()
                    .post()
                    .uri("http://localhost:9092/api/auth/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(AuthResponse.class)
                    .flatMap(authResponse -> {
                        if (authResponse.isValidUser()) {
                            // If token is valid, proceed to respective service
                            System.out.println("Token validated. User is valid.");
                            return chain.filter(exchange);
                            // return addUserIdToRequestBody(exchange, authResponse.getUserId())
                            //         .flatMap(chain::filter);
                        } else {
                            // If token is invalid, block the request with 401 Unauthorized
                            System.out.println("Invalid token, rejecting request.");
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    })
                    .onErrorResume(e -> {
                        // Handle any error from auth-service by blocking the request
                        System.out.println("Error during token validation: " + e.getMessage());
                        e.printStackTrace();
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    private Mono<ServerWebExchange> addUserIdToRequestBody(ServerWebExchange exchange, String userId) {
        Flux<DataBuffer> body = exchange.getRequest().getBody();
        return DataBufferUtils.join(body)
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    String bodyStr = new String(bytes, StandardCharsets.UTF_8);

                    // Modify the body by adding the userId
                    String updatedBodyStr = bodyStr.substring(0, bodyStr.length() - 1) +
                            ",\"userId\":\"" + userId + "\"}";

                    byte[] updatedBodyBytes = updatedBodyStr.getBytes(StandardCharsets.UTF_8);
                    DataBuffer updatedDataBuffer = exchange.getResponse().bufferFactory().wrap(updatedBodyBytes);

                    // Create a new request with the modified body and mutable headers
                    ServerHttpRequestDecorator modifiedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return Flux.just(updatedDataBuffer);
                        }

                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders headers = new HttpHeaders(super.getHeaders()); // Mutable copy of headers
                            headers.setContentLength(updatedBodyBytes.length); // Set content length
                            return headers;
                        }
                    };

                    return Mono.just(exchange.mutate().request(modifiedRequest).build());
                });
    }

    public static class Config {
        // Configuration properties if needed
    }

    // AuthResponse class to map the validate_token response
    public static class AuthResponse {
        private String userId;
        private boolean validUser;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isValidUser() {
            return validUser;
        }

        public void setValidUser(boolean validUser) {
            this.validUser = validUser;
        }
    }
}
