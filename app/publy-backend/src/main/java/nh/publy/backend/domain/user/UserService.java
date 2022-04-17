package nh.publy.backend.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static nh.publy.backend.graphql.DemoConfig.enableUserServiceSlowDown;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Service
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final RestTemplate restTemplate;
  private final String userServiceUrl;
  private final WebClient webClient;

  public UserService(RestTemplateBuilder restTemplateBuilder, @Value("${publy.userservice.url}") String userServiceUrl) {
    this.userServiceUrl = userServiceUrl;
    this.restTemplate = restTemplateBuilder.build();
    this.webClient = WebClient.builder().baseUrl(userServiceUrl).build();
  }

  public Optional<User> getCurrentUser() {
    return getAuthentication()
      .map(Authentication::getPrincipal)
      .filter(principal -> principal instanceof User)
      .map(principal -> (User) principal);
  }

  public User findUserSync(String userId) {

    URI uri = UriComponentsBuilder.fromHttpUrl(this.userServiceUrl)
      .path("/users/{userId}")
      .queryParamIfPresent("slowDown", enableUserServiceSlowDown)
      .build(userId);

    logger.debug("Loading user with Id '{}'", userId);
    ResponseEntity<User> response =
      restTemplate.exchange(uri,
        HttpMethod.GET,
        null,
        User.class);
    User user = response.getBody();
    logger.debug("Received user for id {}: {}", userId, user);
    return user;
  }

  public Mono<User> findUserAsync(String userId) {
    logger.debug("Loading user with Id '{}'", userId);
    var userMono = webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/users/{userId}")
        .queryParamIfPresent("slowDown", enableUserServiceSlowDown)
        .build(userId))
      .retrieve().bodyToMono(User.class)
      .doFirst(() -> logger.trace("Requesting user with id '{}'!", userId))
      .doOnNext(result -> logger.trace("Retrieved Result for user id '{}'!", userId));

    return userMono;
  }

  public Flux<User> findUsers(List<String> userIds) {
    logger.debug("Loading users with Ids '{}'", userIds);

    var result = webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/find-users/{userIds}")
        .queryParamIfPresent("slowDown", enableUserServiceSlowDown)
        .build(String.join(",", userIds)))
      .retrieve().bodyToFlux(User.class)
      .doFirst(() -> logger.trace("Requesting users with ids '{}'!", userIds))
      .doOnNext(x -> logger.trace("Retrieved Result for user ids '{}'!", userIds));

    return result;
  }

  private Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }
}
