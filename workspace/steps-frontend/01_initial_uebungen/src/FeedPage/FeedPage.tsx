import * as React from "react";
import Button from "../components/Button";
import ButtonBar from "../components/ButtonBar";

export default function FeedPage() {
  // TODO: Verwende den 'FeedPageQuery', um die erste Seite mit Stories zu lesen
  //  - TS Typen und Hook-Funktionen sind bereits generiert dafür
  //  - Der Query liefert drei Stories zurück. Dazu kannst Du eine 'page' als
  //    Variable übergeben, mit der Du eine "Seite" angeben kannst, ab der
  //    Du die Stories lesen kannst (0 = erste Seite).
  //     (Seite 0 = Stories 1-3, Seite 1 = Stories 4-6, ...)
  //  - Initial sollen die Stories von der ersten Seite gelesen werden
  //
  //  - Behandel das Ergebnis:
  //    - wenn es einen Fehler gab, soll die Komponente eine Fehlermeldung darstellen
  //      (einfach einen String o.ä. mit return zurückliefern)
  //    - während die Daten geladen werden, gib die Komponente LoadingIndicator zurück
  //    - wenn Daten geladen wurden, zeige die geladenen Stories mit der Komponente
  //       StoryTeaserList an (s.u.)

  const handleFetchMore = () => {
    // TODO SCHRITT 2: Wenn das Laden der Daten funktioniert, implementiere
    //  die Logik zum Laden der nächsten Seite:
    //  - falls es eine weitere Seite gibt
    //      ('stories.page.hasNextPage' im Query-Ergebnis ist true),
    //      dann hier mit der refetch-Funktion den Query-erneut ausführen
    //      - Übergib dazu die nächste Seitenzahl
    //        - Die aktuelle Seite findest Du im aktuellen Query-Ergebnis...
  };

  return (
    <div>
      {/* TODO: Verwende die Komponente 'StoryTeaserList', um
				        die gelesenen Stories anzuzeigen
			   */}

      <ButtonBar align={"center"}>
        <Button onClick={handleFetchMore}>Load more</Button>
      </ButtonBar>
    </div>
  );
}
