# Spring for GraphQL Workshop

Hier findest Du alles, was Du zur Vorbereitung auf den GraphQL Workshop wissen musst.

## Arbeiten mit dem Workspace

Es gibt zwei Wege, wie Du den Workspace während des Workshops
für Übungen und Experimente verwenden kannst:

- **lokal**: dafür musst Du JDK, Editor, Git etc. auf deinem Laptop installiert haben. Mehr dazu, siehe unten.
- **GitPod**: GitPod ist eine Cloud-basierte Web-IDE, die du direkt aus deinem Browser starten kannst. Die Schritte für GitPod findest Du in [README-gitpod.md](./README-gitpod.md).

# Lokale Installation

## Voraussetzungen

**Für deinen Laptop/PC**

Auf deinem Laptop/PC sollte installiert sein:

- Git (zum klonen des Workspaces)
- Java JDK (mindestens Version 11)
- Browser
- Eine Java IDE, zum Beispiel [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Evaluationsversion reicht)

**Internetzugang**

- Bitte überprüfe, dass dein Internet-Zugang auch **für Git und Gradle/Maven** funktioniert (Proxies, VPN, Firewall beachten!)

## Ports

Die Anwendung benötigt folgende folgende Ports:

- 8090 (Backend für die GraphQL API)
- 8091 (UserService)

Bitte achte darauf, dass diese Ports nicht belegt sind.

## Schritt 1: Repository klonen

1. Das Repository klonen:

```
git clone https://github.com/nilshartmann/spring-graphql-training
```

## Schritt 2: Testweise den Workspace bauen

1. Im Verzeichnis **workspace** des Repositories einmal den Gradle-Build ausführen, damit werden dann auch gleich alle benötigten Module geladen:

```
cd workspace

./gradlew build
```

## Schritt 3: Öffnen in der IDE

Wenn Du möchstest, kannst Du das `workspace`-Verzeichnis in deiner IDE öffnen. Dort sollten zwei (Gradle-)Projekte erkannt und compiliert werden: `publy-backend` und `publy-userservice`.

Die anderen Verzeichnisse in diesem Repository brauchst Du _nicht_ in der IDE zu öffnen. Wir arbeiten ausschliesslich in dem `workspace`-Verzeichnis.

**Das ist alles!**

Bei Fragen oder Problemen melde dich gerne bei mir.

# Die fertige Anwendung

Im Verzeichnis `app` findest Du eine vollständige GraphQL-Anwendungen. Einen kleinen Teil dieser Anwendung, bzw. dessen API, werden wir im Laufe des
Trainings gemeinsam bauen.

Es ist nicht erforderlich, dass Du die fertige Anwendung ausführst oder verwendest. Ich werde sie dir auf meinem Computer zeigen.

Falls Du die Anwendung ausführen möchtest, findest Du in der `README`-Datei im `app`-Verzeichnis weitere Informationen. Achtung! Für die fertige Anwendung benötigst du Java 17.
