package nh.graphql.publy.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@RestController
@CrossOrigin
public class LoginController {

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  private final JwtTokenService jwtTokenService;
  private final UserRepository userRepository;

  public LoginController(JwtTokenService jwtTokenService, UserRepository userRepository) {
    this.jwtTokenService = jwtTokenService;
    this.userRepository = userRepository;
  }

  /**
   * Tries to login the given user
   * <p>
   * For testing:
   * - use a 'username' from list of users in Users class
   * - use any string as password that is longer than 4 chars
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    logger.debug("Authenticating '{}'", request.username());

    try {
      return userRepository.findUserWithUsernameAndPassword(request.username(), request.username())
        .map(jwtTokenService::createTokenForUser)
        .map(LoginResponse::new)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
    } catch (Exception ex) {
      logger.warn("Login failed " + ex, ex);
      return ResponseEntity.badRequest().body("Login failed: " + ex);
    }
  }

  @GetMapping("/public-key")
  @ResponseBody
  public ResponseEntity<PublicKeyResponse> getPublicKey() {
    return ResponseEntity.ok(new PublicKeyResponse(jwtTokenService.getPublicKeyString()));
  }

  @PostMapping("/verify")
  public ResponseEntity<VerifyResponse> verify(@Valid @RequestBody VerifyRequest request) {
    logger.debug("Verify token '{}'", request.token());

    var user = jwtTokenService.getUserFromToken(request.token());

    return ResponseEntity.ok(new VerifyResponse(user));
  }
}
