package nh.publy.backend.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Service
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final WebClient webClient;

  public UserService(@Value("${publy.userservice.url}") String userServiceUrl) {
    this.webClient = WebClient.builder().baseUrl(userServiceUrl).build();
  }

  public Optional<User> getCurrentUser() {
    return getAuthentication()
      .map(Authentication::getPrincipal)
      .filter(principal -> principal instanceof User)
      .map(principal -> (User) principal);
  }

  public Mono<User> findUser(String userId) {

    var userMono = webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/users/{userId}")
        .build(userId))
      .retrieve().bodyToMono(User.class);

    return userMono;
  }

  public Flux<User> findUsers(List<String> userIds) {
    logger.debug("Loading users with Ids '{}'", userIds);

    var result = webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/find-users/{userIds}")
        .build(String.join(",", userIds)))
      .retrieve().bodyToFlux(User.class);

    return result;
  }

  private Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }

}
