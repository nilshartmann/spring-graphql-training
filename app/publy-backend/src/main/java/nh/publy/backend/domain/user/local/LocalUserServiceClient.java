package nh.publy.backend.domain.user.local;

import nh.publy.backend.domain.user.User;
import nh.publy.backend.domain.user.UserServiceClient;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Profile("with-loal-user-service")
@Primary
public class LocalUserServiceClient implements UserServiceClient {

  @Override
  public Optional<User> getCurrentUser() {
    return Optional.empty();
  }

  @Override
  public User findUserSync(String userId) {
    return Users.users().
      stream()
      .filter(u -> u.getId().equals(userId))
      .findFirst().orElseThrow();
  }

  @Override
  public Mono<User> findUserAsync(String userId) {
    return Mono.just(findUserSync(userId));
  }

  @Override
  public Flux<User> findUsers(List<String> userIds) {
    return Flux.fromStream(
      Users.users().stream().filter(
        u -> userIds.contains(u.getId())
      )
    );
  }
}
