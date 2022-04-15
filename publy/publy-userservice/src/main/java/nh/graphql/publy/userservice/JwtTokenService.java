package nh.graphql.publy.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Service
public class JwtTokenService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ObjectMapper objectMapper;
  private final int jwtExpirationInMs;
  private final JwtParser jwtParser;
  private final PrivateKey privateKey;
  private final PublicKey publicKey;

  public JwtTokenService(@Value("${publy.jwt.expirationInMs}") int jwtExpirationInMs, @Value("${publicKey}") String publicKeyString, @Value("${privateKey}") String privateKeyString, ObjectMapper objectMapper) throws NoSuchAlgorithmException, InvalidKeySpecException {
    this.jwtExpirationInMs = jwtExpirationInMs;
    this.objectMapper = objectMapper;


    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
    privateKey = kf.generatePrivate(privateSpec);

    X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
    publicKey = kf.generatePublic(keySpecX509);
    this.jwtParser = Jwts.parserBuilder()
      .setSigningKey(publicKey)
      .build();
  }

  @PostConstruct
  public void generateNonExpiringTokenForTestingAndDemo() {
    Calendar expiry = Calendar.getInstance();
    expiry.set(2072, Calendar.JUNE, 12, 22, 13, 7);
    Calendar issued = Calendar.getInstance();
    issued.set(2022, Calendar.JANUARY, 5, 9, 4, 7);

    // ROLE_USER => can do everything
    User user = Users.users().get(0);
    String userToken = createToken(user, issued.getTime(), expiry.getTime());

    // ROLE_GUEST => cannot create stories
    User guest = Users.users().stream().filter(User::isGuest).findFirst().orElseThrow(() -> new IllegalStateException("No user with guest role found"));
    String guestToken = createToken(guest, issued.getTime(), expiry.getTime());

    logger.info("""

        ===============================================================
        NEVER EXPIRING JWT TOKENS
        ===============================================================
        ROLE_USER (can create stories, leave comments and reactions)
        login: '{}'

        {"Authorization": "Bearer {}"}

        ROLE_GUEST (cannot create stories, but leave comments and reactions)
        login: '{}'
         
        {"Authorization": "Bearer {}"}

        ===============================================================
        """,
      user.getUsername(),
      userToken,
      guest.getUsername(),
      guestToken);

  }

  public String createTokenForUser(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
    return createToken(user, now, expiryDate);
  }

  private String createToken(User user, Date issuedAt, Date expiryDate) {

    try {
      String subject = objectMapper.writeValueAsString(user);
      return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(issuedAt)
        .setExpiration(expiryDate)
        .signWith(privateKey, SignatureAlgorithm.RS512)
        .compact();
    } catch (Exception ex) {
      logger.error("Could not create Token: " + ex, ex);
      throw new IllegalStateException(ex);
    }
  }

  public User getUserFromToken(String token) {
    try {
      Claims claims = jwtParser
        .parseClaimsJws(token)
        .getBody();

      String subject = claims.getSubject();
      User user = objectMapper.readValue(subject, User.class);
      return user;
    } catch (Exception ex) {
      logger.error("Could not read user from token '{}': " + ex, token, ex);
      throw new IllegalStateException(ex);
    }
  }

  public String getPublicKeyString() {
    return Base64.getEncoder().encodeToString(publicKey.getEncoded());
  }
}
