package nh.publy.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import nh.publy.backend.domain.user.PublicKeyResponse;
import nh.publy.backend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class PublyAuthenticationService implements AuthenticationService {
  private static final Logger log = LoggerFactory.getLogger(PublyAuthenticationService.class);

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private JwtParser jwtParser;

  public PublyAuthenticationService(@Value("${publy.userservice.url}") String url, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.webClient = WebClient.builder().baseUrl(url).build();
  }

  @Override
  public User verify(@NotNull String token) {
    log.info("Verifying token '{}'", token);

    try {
      Claims claims = getJwtParser()
        .parseClaimsJws(token)
        .getBody();

      String subject = claims.getSubject();
      User user = objectMapper.readValue(subject, User.class);
      return user;
    } catch (Exception ex) {
      log.error("Verify Token failed: " + ex, ex);
      throw new BadCredentialsException("Invalid token received!", ex);
    }
  }

  private String loadPublicKey() {
    var response = webClient.get()
      .uri("/public-key")
      .retrieve().bodyToMono(PublicKeyResponse.class).block();

    return response.publicKey();
  }

  private JwtParser getJwtParser() {
    if (this.jwtParser == null) {
      try {
        String publicKeyString = loadPublicKey();
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey publicKey = kf.generatePublic(keySpecX509);
        this.jwtParser = Jwts.parserBuilder()
          .setSigningKey(publicKey)
          .build();
      } catch (Exception ex) {
        log.error("Could not create JwtParser: " + ex, ex);
        throw new IllegalStateException(ex);
      }
    }
    return this.jwtParser;
  }

}
