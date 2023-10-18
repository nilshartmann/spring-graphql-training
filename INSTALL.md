# Spring for GraphQL Workshop

Hier findest Du alles, was Du zur Vorbereitung auf den GraphQL Workshop wissen musst.

## Arbeiten mit dem Workspace

Es gibt zwei Wege, wie Du den Workspace während des Workshops
für Übungen und Experimente verwenden kannst:

- **lokal**: dafür musst Du JDK, Editor, Git etc. auf deinem Laptop installiert haben. Mehr dazu: siehe unten.
- **GitPod**: GitPod ist eine Cloud-basierte Web-IDE, die du direkt aus deinem Browser starten kannst. Die Schritte für GitPod findest Du in [README-gitpod.md](./README-gitpod.md).

# Lokale Installation

## Voraussetzungen

**Für deinen Laptop/PC**

Auf deinem Laptop/PC sollte installiert sein:

- Java JDK (mindestens **Version 17**)
- Git (zum Klonen des Workspaces)
- Browser
- Eine Java IDE, zum Beispiel [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Evaluationsversion reicht)

**Internetzugang**

- Bitte überprüfe, dass dein Internet-Zugang auch **für Git und Gradle/Maven** funktioniert (Proxies, VPN, Firewall beachten!)
- Auch wenn die Installation im Vorwege gemacht hast, kann es vorkommen, dass wir während des Workshops noch Dinge installieren müssen,
  daher sollte das Internet-Zugriff auch während der Schulung funktionieren.

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

> [!IMPORTANT]
> Aus dem Repository verwenden wir **nur das `workspace`-Unterverzeichnis**.
> 
> Bitte nicht das ganze Repository in der IDE öffnen (siehe unten)!

## Schritt 2: Testweise den Workspace bauen

1. Im Verzeichnis **workspace** des Repositories einmal den Gradle-Build ausführen, damit werden dann auch gleich alle benötigten Module geladen:

```
cd workspace

./gradlew clean build -x :hello-graphql-java:test
```

## Schritt 3: Öffnen in der IDE

Importiere nun das **`workspace`-Unterverzeichnis** in deiner IDE. Dort sollten zwei (Gradle-)Projekte erkannt und compiliert werden: `publy-backend` und `publy-userservice`.

- IntelliJ: `File -> Open` und dann das `workspace` Verzeichnis auswählen
- Eclipse: `File -> Import... -> Gradle -> Existing Gradle Project`. Dann das `workspace`-Verzeichnis auswählen. Keine weiteren Anpassungen im Eclipse Wizard machen, einfach `Finish` klicken.

Die anderen Top-Level-Verzeichnisse in diesem Repository bitte _nicht_ in der IDE zu öffnen. Wir arbeiten ausschließlich in dem `workspace`-Verzeichnis.

## Schritt 4: Anwendungen starten

Bitte starte aus deiner IDE auf dem gewohnten Weg die beiden Java Anwendungen:

- `nh.publy.backend.PublyApplication`
- `nh.graphql.publy.userservice.UserserviceApplication`

Die beiden Anwendungen belegen die Ports `8090` bzw `8091`. Diese Ports sollten also vorher nicht belegt sein.

## Schritt 5: Source-Code bearbeiten

Du kannst die Sourcen in deiner IDE wie gewohnt bearbeiten. Nach dem Speichern bzw. Compilieren/Build wird die PublyApplication automatisch neu gestartet.

Hinweis: wir werden Änderungen nur im Modul `publy-backend` machen.

**Das ist alles!**

Bei Fragen oder Problemen melde dich gerne bei mir.

# Die fertige Anwendung

Im Verzeichnis `app` findest Du eine vollständige GraphQL-Anwendungen. Einen kleinen Teil dieser Anwendung, bzw. dessen API, werden wir im Laufe des
Trainings gemeinsam bauen.

Es ist nicht erforderlich, dass Du die fertige Anwendung ausführst oder verwendest. Ich werde sie dir auf meinem Computer zeigen.

Falls Du die Anwendung ausführen möchtest, findest Du in der `README`-Datei im `app`-Verzeichnis weitere Informationen. Achtung! Für die fertige Anwendung benötigst du Java 17.
