package nh.graphql.publy.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@RestController
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final static Random random = new Random();

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/users/{userId}")
  public Optional<User> user(@PathVariable String userId, @RequestParam(name = "slowDown", defaultValue = "false") boolean enableSlowdown) {
    logger.info("Finding user with id '{}'", userId);
    Optional<User> userWithId = userRepository.findUserWithId(userId);
    slowDown(enableSlowdown);

    logger.info("Returning User {}", userWithId);

    return userWithId;
  }

  @GetMapping(value = "/find-users/{userIds}")
  public List<User> users(@PathVariable String[] userIds) {
    logger.info("Finding users with ids '{}'", Arrays.asList(userIds));
    return userRepository.findUsersWithId(userIds);
  }


  private static void slowDown(boolean enableSlowdown) {
    if (enableSlowdown) {
      try {
        int x = random.nextInt(1500) + 500;
        Thread.sleep(x);
      } catch (InterruptedException e) {
        logger.info("Interrupted");
      }
    }
  }
}
