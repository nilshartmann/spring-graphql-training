package nh.publy.backend.graphql;

import graphql.relay.DefaultConnectionCursor;
import nh.publy.backend.domain.Identifiable;

import javax.validation.constraints.NotNull;
import java.util.Base64;

public class PublyCursor {
  public static DefaultConnectionCursor forId(Long id) {
    return new DefaultConnectionCursor(
      Base64.getEncoder().encodeToString(id.toString().getBytes())
    );
  }

  public static DefaultConnectionCursor forObject(Identifiable question) {
    return forId(question.getId());
  }

  public static Long decode(@NotNull String value) {
    String str = new String(Base64.getDecoder().decode(value));
    return Long.parseLong(str);
  }

}
