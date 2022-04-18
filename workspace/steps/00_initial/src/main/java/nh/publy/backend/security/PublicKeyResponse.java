package nh.publy.backend.security;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public final class PublicKeyResponse {
  private String publicKey;


  public PublicKeyResponse() {

  }

  public PublicKeyResponse(String publicKey) {
    this.publicKey = publicKey;
  }

  public String publicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (PublicKeyResponse) obj;
    return Objects.equals(this.publicKey, that.publicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicKey);
  }

  @Override
  public String toString() {
    return "PublicKeyResponse[" +
      "publicKey=" + publicKey + ']';
  }

}
