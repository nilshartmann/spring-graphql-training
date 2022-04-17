package nh.graphql.publy.userservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTJavaWithPublicPrivateKey {
  private static final Logger log = LoggerFactory.getLogger(JWTJavaWithPublicPrivateKey.class);

  private final PrivateKey privateKey;
  private final PublicKey publicKey;

  public JWTJavaWithPublicPrivateKey(@Value("${publicKey}") String publicKeyString, @Value("${privateKey}") String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {

    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
    privateKey = kf.generatePrivate(privateSpec);

    X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
    publicKey = kf.generatePublic(keySpecX509);

//    RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(Base64.getDecoder().decode(publicKeyString));
//     publicKey = kf.generatePublic(publicSpec);
  }

  @PostConstruct
  public void aaa() {


    System.out.println("generating keys");
//    Map<String, Object> rsaKeys = null;
//
//    try {
//      rsaKeys = getRSAKeys();
//    } catch (Exception e) {
//
//      e.printStackTrace();
//    }
//    PublicKey publicKey = (PublicKey) rsaKeys.get("public");
//    PrivateKey privateKey = (PrivateKey) rsaKeys.get("private");

    System.out.println("generated keys");

    String token = generateToken(privateKey);
    System.out.println("Generated Token:\n" + token);

    verifyToken(token, publicKey);

  }

  public static String generateToken(PrivateKey privateKey) {
    String token = null;
    try {
      Map<String, Object> claims = new HashMap<String, Object>();

      // put your information into claim
      claims.put("id", "xxx");
      claims.put("role", "user");
      claims.put("created", new Date());

      token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.RS512, privateKey).compact();


    } catch (Exception e) {
      e.printStackTrace();
    }
    return token;
  }

  // verify and get claims using public key

  private static Claims verifyToken(String token, PublicKey publicKey) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

      System.out.println(claims.get("id"));
      System.out.println(claims.get("role"));

    } catch (Exception e) {

      claims = null;
    }
    return claims;
  }

  // Get RSA keys. Uses key size of 2048.
  private static Map<String, Object> getRSAKeys() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048, new SecureRandom(new byte[123]));
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();
    String publicString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    String privateString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

    log.info("public '{}'", publicString);
    log.info("private '{}'", privateString);

    Map<String, Object> keys = new HashMap<String, Object>();
    keys.put("private", privateKey);
    keys.put("public", publicKey);
    return keys;
  }

//  static void abc() throws Exception {
//    byte[] b1 = Base64.getDecoder().decode(privateKeyString);
//    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
//    KeyFactory kf = KeyFactory.getInstance("RSA");
//    PrivateKey privateKey = kf.generatePrivate(spec);
//  }
}
