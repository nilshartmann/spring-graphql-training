package nh.graphql.publy.userservice;


import java.util.Arrays;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class User {

  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_GUEST = "ROLE_GUEST";
  public static final String ROLE_EDITOR = "ROLE_EDITOR";

  private String id;
  private String username;
  private String name;
  private String email;
  private String[] roles = {ROLE_USER};

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

  public boolean isGuest() {
    return Arrays.asList(roles).contains(ROLE_GUEST);
  }
}
