package nh.publy.backend.domain.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface UserServiceClient {
  default Optional<User> getCurrentUser() {
    return getAuthentication()
      .map(Authentication::getPrincipal)
      .filter(principal -> principal instanceof User)
      .map(principal -> (User) principal);
  }

  User findUserSync(String userId);

  Mono<User> findUserAsync(String userId);

  Flux<User> findUsers(List<String> userIds);

  default Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }
}
