package nh.graphql.publy.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Component
public class UserRepository {

  private final List<User> users;

  public UserRepository(@Value("${userservice.baseurl}") String baseUrl) {
    users = Users.users();
  }

  public List<User> findUsersWithId(String[] ids) {
    final List<String> idList = Arrays.asList(ids);

    return idList.stream()
      .map(userId -> this.users.stream()
        .filter(u -> userId.equals(u.getId()))
        .findFirst().orElse(null))
        .collect(Collectors.toList());
  }

  public Optional<User> findUserWithId(String id) {
    return this.users.stream().filter(
      u -> u.getId().equals(id)
    ).findFirst();
  }

  public User getUserWithId(String id) {
    return this.users.stream().filter(
      u -> u.getId().equals(id)
    ).findFirst().orElseThrow();
  }

  public Optional<User> findUserWithUsernameAndPassword(String username, String password) {
    if (password == null || password.isBlank() || password.length() < 4) {
      return Optional.empty();
    }
    return this.users.stream().filter(
      u -> u.getUsername().equals(username)
    ).findFirst();
  }
}
