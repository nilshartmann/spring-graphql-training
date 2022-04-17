package nh.graphql.publy.userservice;

import java.util.LinkedList;
import java.util.List;

import static nh.graphql.publy.userservice.User.*;

public class Users {
  public static List<User> users() {
    final var users = new LinkedList<User>();

    users.add(new User("U1", "nils", "Nils Hartmann", "kontakt@nilshartmann.net", ROLE_USER, ROLE_EDITOR));
//    users.add(new User("U1", "nils", "Nils Hartmann", "kontakt@nilshartmann.net", baseUrl + "avatars/nils.jpg", "Hamburg", "Software-Developer from Hamburg", "Beer and GraphQL", "How to teach GraphQL"));
    users.add(new User("U2", "larkin", "Brandt Larkin", "brandt.larkin@example.com", ROLE_USER, ROLE_EDITOR));
    users.add(new User("U3", "treutel", "Vita Treutel", "vita.treutel@example.com", ROLE_USER, ROLE_EDITOR));
    users.add(new User("U4", "purdy", "Deven Purdy", "deven.purdy@example.com", ROLE_USER, ROLE_EDITOR));
    users.add(new User("U5", "satterfield", "Spencer Satterfield", "spencer.satterfield@example.com", ROLE_USER, ROLE_EDITOR));
    users.add(new User("U6", "waters", "Garth Waters", "garth.waters@example.com", ROLE_USER, ROLE_EDITOR));
    users.add(new User("U7", "murphy", "Ethelyn Murphy", "ethelyn.murphy@example.com", ROLE_USER, ROLE_GUEST));
    users.add(new User("U8", "hayes", "Ivah Hayes", "ivah.hayes@example.com"));
    users.add(new User("U9", "emmerich", "Rollin Emmerich", "rollin.emmerich@example.com"));
    users.add(new User("U10", "carroll", "Brad Carroll", "brad.carroll@example.com"));
    users.add(new User("U11", "boehm", "Kari Boehm", "kari.boehm@example.com"));
    users.add(new User("U12", "howe", "Gardner Howe", "gardner.howe@example.com"));
    users.add(new User("U13", "willms", "Isaiah Willms", "isaiah.willms@example.com"));
    users.add(new User("U14", "weissnat", "Brayan Weissnat", "brayan.weissnat@example.com"));
    users.add(new User("U15", "herman", "Ryleigh Herman", "ryleigh.herman@example.com"));
    users.add(new User("U16", "dare", "Regan Dare", "regan.dare@example.com"));
    users.add(new User("U17", "cronin", "Felix Cronin", "felix.cronin@example.com"));
    users.add(new User("U18", "wilkinson", "Daphne Wilkinson", "daphne.wilkinson@example.com"));
    users.add(new User("U19", "connelly", "Oren Connelly", "oren.connelly@example.com"));
    users.add(new User("U20", "bartoletti", "Lauretta Bartoletti", "lauretta.bartoletti@example.com"));
    users.add(new User("U21", "spencer", "Brionna Spencer", "brionna.spencer@example.com"));
    users.add(new User("U22", "schroeder", "Royce Schroeder", "royce.schroeder@example.com"));
    users.add(new User("U23", "paucek", "Darryl Paucek", "darryl.paucek@example.com"));
    users.add(new User("U24", "waters", "Cade Waters", "cade.waters@example.com"));
    users.add(new User("U25", "lemke", "Lamar Lemke", "lamar.lemke@example.com"));
    users.add(new User("U26", "gottlieb", "Kieran Gottlieb", "kieran.gottlieb@example.com"));
    users.add(new User("U27", "considine", "Angelina Considine", "angelina.considine@example.com"));
    users.add(new User("U28", "reichel", "Lamont Reichel", "lamont.reichel@example.com"));
    users.add(new User("U29", "grimes", "Dejon Grimes", "dejon.grimes@example.com"));
    users.add(new User("U30", "anderson", "Eva Anderson", "eva.anderson@example.com"));
    users.add(new User("U31", "braun", "Winifred Braun", "winifred.braun@example.com"));
    users.add(new User("U32", "wilkinson", "Tevin Wilkinson", "tevin.wilkinson@example.com"));
    users.add(new User("U33", "bode", "Alice Bode", "alice.bode@example.com"));
    users.add(new User("U34", "fahey", "Sadie Fahey", "sadie.fahey@example.com"));
    users.add(new User("U35", "hahn", "Nichole Hahn", "nichole.hahn@example.com"));
    users.add(new User("U36", "watsica", "Gunner Watsica", "gunner.watsica@example.com"));
    users.add(new User("U37", "kuhn", "Everett Kuhn", "everett.kuhn@example.com"));
    users.add(new User("U38", "langworth", "Cecilia Langworth", "cecilia.langworth@example.com"));
    users.add(new User("U39", "muller", "Gloria Muller", "gloria.muller@example.com"));
    users.add(new User("U40", "lang", "Lucious Lang", "lucious.lang@example.com"));
    users.add(new User("U41", "cronin", "Dorothy Cronin", "dorothy.cronin@example.com"));
    users.add(new User("U42", "goyette", "Jakob Goyette", "jakob.goyette@example.com"));
    users.add(new User("U43", "miller", "Saul Miller", "saul.miller@example.com"));


    return users;
  }
}

