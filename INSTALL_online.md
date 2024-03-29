# Spring for GraphQL Training

Hier findest Du alles, was Du zur Vorbereitung auf das GraphQL Training wissen musst.

## Voraussetzungen

**Für dein Laptop/PC**

Auf deinem Laptop/PC sollte installiert sein:

- Git (zum klonen des Workspaces)
- Java JDK (mindestens Version 11)
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

## Verzeichnisse

Das Repository besteht aus zwei Verzeichnissen: `app` und `workspace`
- Wir arbeiten nur in `workspace`. Es reicht, wenn Du dieses Verzeichnis in deiner IDE öffnest (am Besten als Gradle-Projekt importieren)
- Das `app`-Verzeichnis enthält eine "fertige" Anwendung, die braucht ihr nicht zu verwenden. Falls ihr die ausprobieren wollt, findet ihr weiter unten eine Beschreibung.

Die folgenden Schritte beziehen sich also immer auf das `workspace`-Verzeichnis, soweit nicht anders angegeben.

## Ports

Die Anwendung benötigt folgende folgende Ports:

- 8090 (Backend für die GraphQL API)
- 8091 (UserService)

Bitte achte darauf, dass diese Ports bei dir nicht belegt sind.

# Den Workspace für die Übungen einrichten

## Schritt 1: Repository klonen

1. Das Repository klonen:

```
git clone https://github.com/nilshartmann/spring-graphql-training
```

## Schritt 2: Testweise den Workspace bauen

1. Im Verzeichnis **workspace** des Repositories einmal den Gradle-Build ausführen, damit werden dann auch gleich alle benötigten Module geladen:

```
cd workspace

./gradlew clean build -x :hello-graphql-java:test
```


## Schritt 3: Öffnen des Workspaces in der IDE

Wenn Du möchstest, kannst Du das `workspace`-Verzeichnis schon in deiner IDE öffnen. Dort sollten vier (Gradle-)Projekte erkannt und compiliert werden:
* `publy-backend`
* `publy-userservice`
Die folgenden beiden Projekten sind ebenfalls im `workspace` vorhanden, werden von uns während diese Trainings aber nicht benötigt. Du kannst sie einfach ignorieren:
* `hello-graphql-java` 
* `publy-frontend`

Die anderen Verzeichnisse in diesem Repository brauchst Du _nicht_ in der IDE zu öffnen. 
Wir arbeiten ausschliesslich in dem `workspace`-Verzeichnis.

### Schritt 4: Starten der Backend Services

Testweise kannst Du schon einmal die beiden Backend-Services ("Backend" und "UserService") starten.

* "User Service" starten
    * Entweder in der IDE `nh.graphql.publy.userservice.UserserviceApplication` starten
    * oder: im `workspace`-Verzeichnis: `./gradlew :publy-userservice:bootRun`
* "Backend" starten
    * Entweder in der IDE `nh.publy.backend.PublyApplication` starten
    * oder: im `workspace`-Verzeichnis: `./gradlew :publy-backend:bootRun`

Bei den beiden Services handelt es sich um "normale" Spring Boot Anwendungen,
im Logfile müsste jeweils `Tomcat started on port(s)...` auftauchen.

**Das ist alles!**

Bei Fragen oder Problemen melde dich gerne bei mir.

# Die fertige Anwendung

Im Verzeichnis `app` findest Du eine vollständige GraphQL-Anwendungen. Einen kleinen Teil dieser Anwendung, bzw. dessen API, werden wir im Laufe des
Trainings gemeinsam bauen, aber die fertige Anwendung enthält noch viel mehr Beispiele 
und eine etwas realistischere API.

Es ist nicht erforderlich, dass Du die fertige Anwendung ausführst oder verwendest. Ich werde sie dir auf meinem Computer zeigen.

Falls Du die Anwendung ausführen möchtest, findest Du in der `README`-Datei im `app`-Verzeichnis weitere Informationen. Achtung! Für die fertige Anwendung benötigst du Java 17.
