# Publy - GraphQL Example Application

# 1. Öffnen und Starten der Anwendung

![Screenshot der Publy Beispiel Anwendung](./publy-screenshot.png)

**Voraussetzungen:**

- Java 17
- Docker
- [yarn](https://yarnpkg.com/) optional, nur für das Frontend

**Schritt 1: Repository klonen**

- Bitte klone zunächst dieses Repository mit deinem Git Client.

**Schritt 2: Datenbank starten (mit Docker)**

Im `publy`-Verzeichnis des Repositories:

- `docker-compose up -d`

**Schritt 3: Userservice starten**

Achtung! Port 8081 darf nicht belegt sein:

- `./gradlew publy-userservice:bootRun`
  
**Schritt 4: Backend starten**

Achtung! Port 8080 darf nicht belegt sein:

- `./gradlew publy-backend:bootRun`

Du kannst nun über `http://localhost:8080` den GraphiQL-Explorer
aufmachen und Queries und Mutations ausführen.

**Schritt 5: Frontend starten (optional)**

Achtung! Port 3000 darf nicht belegt sein.

Voraussetzung: Der Package-Manager [pnpm](https://pnpm.io/) (Version 7.9.5) ist installiert.
 - Installation von pnpm siehe: https://pnpm.io/installation
 - Bei mir habe ich pnpm wie folgt installiert:
   ```bash
   corepack enable
   corepack prepare pnpm@7.9.5 --activate
   ```
- (Möglicherweise funktioniert das Starten auch mit yarn oder npm, aber ich teste mit pnpm).


Installieren der Packages und starten des Frontends:
```bash

cd publy-frontend

pnpm install

pnpm start
```

Du kannst nun das Frontend über `http://localhost:3000` in deinem Browser öffnen.

## Öffnen in der IDE

Du kannst das Root-Verzeichnis in IDEA öffnen, dann wird das Projekt als Gradle-Projekt erkannt und von IDEA automatisch richtig konfiguriert.

Dann kannst Du u.a. die beiden Server-Prozesse als Java-Anwendungen starten:

- `nh.graphql.publy.userservice.UserserviceApplication`: Der User-Service
- `nh.publy.backend.PublyApplication`: Das (GraphQL-)Backend

Wichtig: In jedem Fall bitte zuerst die Datenbank wie oben beschrieben mit `docker-compose` starten!

# 2. Frontend verwenden

Wenn Du das Beispiel-Frontend ausprobieren möchte, öffne es in deinem Browser unter `http://localhost:3000`.

Als nicht angemeldeter Benutzer darfst Du z.B. Artikel und Kommentare lesen sowie Benutzer ("Member")-Informationen anzeigen. Im Hintergrund werden dann GraphQL **Queries** ausgeführt,
die Du z.B. mit der Netzwerk-Konsole deines Browsers ansehen kannst.

Wenn Du einen Kommentar hinzufügen möchtest oder eine Reaktion hinterlassen willst (beides als **Mutations**) implementiert,
musst Du dich über die Login-Seite einloggen. Dort findest Du Benutzernamen, die Du verwenden kannst.

# 2. Ausführen von Queries in GraphiQL

Du kannst GraphiQL benutzen, um GraphQL Queries mit der Beispiel-Anwendung auszuführen.

Beispiel 1:

```graphql
query {
    stories(first: 10) {
        edges {
            node {
                id
                title
                body
            }
        }
    }
}
```

## Authorization-Tokens (für Mutations)

Wenn Du **Mutations** ausführen willst, erwartet das Backend einen Benutzer-Token.
Zum Testen kannst Du einen der folgenden Tokens verwenden, die immer gültig sind:

- User: `nils`
- Rolle: `ROLE_USER` (darf alles)

```json
{"Authorization": "Bearer eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiVTFcIixcInVzZXJuYW1lXCI6XCJuaWxzXCIsXCJuYW1lXCI6XCJOaWxzIEhhcnRtYW5uXCIsXCJlbWFpbFwiOlwia29udGFrdEBuaWxzaGFydG1hbm4ubmV0XCIsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9FRElUT1JcIl0sXCJndWVzdFwiOmZhbHNlfSIsImlhdCI6MTY0MTM2OTg0NywiZXhwIjozMjMyOTg3OTg3fQ.lsLAqfAdMID_2QFL7OimWEHcunaPy18zWVYZUDiEJYVwR9PQG5qf8_gRrNAwkd9w33dwunJC3bswR1W0zMtTID9DyeaXGLws2AtmwF_ZecD6x5TVDEBcFJV1-WrRt2yRWoo-hNywGqDUqR49dJoICoQ-aoP_7GOgYo5zYIYCRe2Xbn4DbX0xzGLyiRNzJzZhq8l6KE_Hb5Ern0hTZhTZXq4jmrCjf8wztuF37rsRJ-nZ9vozUaQU7vwhl93g1gvAMb3zJWBo_m9ujd3RKSZ5fjMuOVb-kVQI6NP9hLoEYO2mkcDSoHNIXBgJHr3TYNzeGtJ3Nt3bTXp-o_P-bSzXYQ"}
```

- User: `murphy`
- Rolle: `ROLE_GUEST` (darf Kommentare und Reaktion geben, aber keine Stories erzeugen. Hinweis: Stories erzeugen nur über GraphiQL möglich, nicht über das Frontend)

```json
{"Authorization": "Bearer eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiVTdcIixcInVzZXJuYW1lXCI6XCJtdXJwaHlcIixcIm5hbWVcIjpcIkV0aGVseW4gTXVycGh5XCIsXCJlbWFpbFwiOlwiZXRoZWx5bi5tdXJwaHlAZXhhbXBsZS5jb21cIixcInJvbGVzXCI6W1wiUk9MRV9VU0VSXCIsXCJST0xFX0dVRVNUXCJdLFwiZ3Vlc3RcIjp0cnVlfSIsImlhdCI6MTY0MTM2OTg0NywiZXhwIjozMjMyOTg3OTg3fQ.A1SHxkgGCfdo-v-kCGRSFuYngMW6438o1alkg4DAdwWBYuy1E7axYbpzGKghP5gR19b7qoc98Y9gY9-zekFxo35yrzDaEmWMYR0UmprYI27M_eh06OJzct2NJt9voldnUlPdCed8mn4vPs56IXHTxd6zGGwSA7JGYSIfswQh2w-3y1d3WCFR3ZPju0f9ZripR_4NQFOltm4NNbHC7CbcWgUtixJx-h5BiAeLfZcJDFoNqUq6obf8jkUzOX_2PEJaeRzxW6WTXd88EbSqjMns5PqmM5BosSJmyuZSjfGGbLaFBqPLrgMLNHHNFwGn6VBtarMvmHsU7zbGYmZXG31XAg"}
```

Den kompletten Authorization-JSON-String kannst Du in GraphiQL unten in den "**Request Headers**"-Tab einfügen.
Der Header wird dann bei jedem Query/jeder Mutation mitgesendet.

[![Request Header in GraphiQL setzen]](./graphiql-request-header.png)

Du kannst dann z.B. den angemeldeten Benutzer (der durch den Token repräsentiert wird),
abfragen:

```graphql
query {
    me {
        id
        user {
            name
            email
        }
        
        bio
        skills
        currentlyLearning
    }
}
```

# Links

* GraphQL Homepage: https://graphql.org/
* IntelliJ Plug-in für GraphQL: https://plugins.jetbrains.com/plugin/8097-graphql
* GraphQL Extension für VS Code: https://marketplace.visualstudio.com/items?itemName=GraphQL.vscode-graphql
* GraphQL Queries mit Postman ausführen: https://learning.postman.com/docs/sending-requests/supported-api-frameworks/graphql/
* GraphiQL: https://github.com/graphql/graphiql
* Liste von Frameworks für diverse Programmiersprachen, mit denen Du GraphQL APIs bauen kannst: https://graphql.org/code/#language-support
* Cursor-basierte Paginierung: https://graphql.org/learn/pagination/
* Relay Cursor Specification: https://relay.dev/graphql/connections.htm
* Postgraphile: https://www.graphile.org/postgraphile/
