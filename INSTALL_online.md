# Spring-GraphQL Training

Hier findest Du alles, was Du zur Vorbereitung auf das GraphQL Training wissen musst.

## Voraussetzungen

**Für dein Laptop/PC**

Auf deinem Laptop/PC sollte installiert sein:

- Git (zum klonen des Workspaces)
- Java JDK (mindestens Version 11)
- Docker
- Browser
- Eine Java IDE, zum Beispiel [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Evaluationsversion reicht)
- Für Schulungen, die wir über **Zoom** machen: bitte den Zoom **Client** installieren (und nicht die Web-Version von Zoom verwenden). Du benötigst _keinen_ Zoom-Account.

**Während des Trainings**

- Da wir vor und während des Trainings ggf. noch Aktualisierungen installieren müssen, bitte sicherstellen, dass auch während des Trainings auf deinem Computer der Internet-Zugang (logisch, online-Schulung 🙃) funktioniert - und zwar auch **für Git, Gradle und Docker** (Proxies beachten!)
- **Ich freue mich, wenn Du während des Trainings deine Kamera an hast**, damit wir uns sehen können 🎥. Mikrofon hingegen bitte nur anmachen, wenn Du etwas sagen oder fragen möchtest (was Du natürlich jederzeit darfst!)
- W-LAN ist bequem, aber gerade bei (langen) Streamings ist ein Kabel-gebundenes Netzwerk stabiler als W-LAN, also im Zweifel lieber das Kabel einstecken (und W-LAN deaktivieren) 😊

# Installation und Vorbereitung des Workspaces für die Schulung

Um Zeit während des Workshops zu sparen, kannst Du im Vorweg schon den Workspace
klonen und die Projekte installieren.

## Ports

Die Anwendung benötigt folgende folgende Ports:

- 8442 (Docker/Postgres)
- 8090 (Backend für die GraphQL API)
- 8091 (UserService)

Bitte achte darauf, dass diese Ports nicht belegt sind.

## Schritt 1: Repository klonen

1. Das Repository klonen:

```
git clone https://github.com/nilshartmann/spring-graphql-training
```

## Schritt 2: Docker starten

Im `workspace`-Verzeichnis des Repositories die Datenbank mit `docker-compose` starten:

```
cd workspace

docker-compose up -d
```

Das Starten der Datenbank beim ersten Mal kann etwas dauern, weil zunächst die
Test-Daten importiert werden.

Mit `docker logs publy_db_graphql_training` kannst Du im Logfile des Containers nach
den beiden Zeilen suchen, die den Text `database system was shut down`
bzw. `database system is ready to accept connections` enthalten. Dann ist Postgres
komplett gestartet.

Zum Beispiel:

```shell
$ docker logs publy_db_graphql_training

2022-04-19 09:28:35.924 UTC [56] LOG:  database system was shut down at 2022-04-19 09:28:35 UTC
2022-04-19 09:28:35.950 UTC [1] LOG:  database system is ready to accept connections
```

## Schritt 3: Testweise den Workspace bauen

1. Im Verzeichnis **workspace** des Repositories einmal den Gradle-Build ausführen, damit werden dann auch gleich alle benötigten Module geladen:

```
cd workspace

./gradlew build
```

## Schritt 4: Öffnen in der IDE

Wenn Du möchstest, kannst Du das `workspace`-Verzeichnis in deiner IDE öffnen. Dort sollten zwei (Gradle-)Projekte erkannt und compiliert werden: `publy-backend` und `publy-userservice`.

Die anderen Verzeichnisse in diesem Repository brauchst Du _nicht_ in der IDE zu öffnen. Wir arbeiten ausschliesslich in dem `workspace`-Verzeichnis.

**Das ist alles!**

Bei Fragen oder Problemen melde dich gerne bei mir.

# Die fertige Anwendung

Im Verzeichnis `app` findest Du eine vollständige GraphQL-Anwendungen. Einen kleinen Teil dieser Anwendung, bzw. dessen API, werden wir im Laufe des
Trainings gemeinsam bauen.

Es ist nicht erforderlich, dass Du die fertige Anwendung ausführst oder verwendest. Ich werde sie dir auf meinem Computer zeigen.

Falls Du die Anwendung ausführen möchtest, findest Du in der `README`-Datei im `app`-Verzeichnis weitere Informationen. Achtung! Für die fertige Anwendung benötigst du Java 17.
