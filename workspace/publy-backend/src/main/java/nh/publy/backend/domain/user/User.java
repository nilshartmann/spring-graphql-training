package nh.publy.backend.domain.user;


import java.util.Arrays;
import java.util.Objects;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class User {
  private String id;
  private String username;
  private String name;
  private String email;
  private String[] roles;

  public User() {
  }

  public User(String id, String username, String name, String email) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
  }

  public User(String id, String username, String name, String email, String... roles) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
    this.roles = roles;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String[] getRoles() {
    return this.roles;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", username='" + username + '\'' +
      ", name='" + name + '\'' +
      ", email='" + email + '\'' +
      ", roles=" + Arrays.toString(roles) +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
