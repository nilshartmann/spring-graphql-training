import { useMe } from "../shared/useMe";
import * as React from "react";
import {
  AddCommentErrorFragment,
  AddCommentMutation,
  useAddCommentMutation,
} from "../generated/graphql";
import Stack from "../components/Stack";
import ButtonBar from "../components/ButtonBar";
import Button from "../components/Button";

type CommentEditorProps = {
  storyId: string;
};

export function CommentEditor({ storyId }: CommentEditorProps) {
  const { me } = useMe();
  const [addError, setAddError] = React.useState("");
  const [addComment, { loading }] = useAddCommentMutation();
  const [content, setContent] = React.useState("");
  const editorDisabled = !me || loading;
  const buttonDisabled = editorDisabled || !content;

  function handleContentChange(newContent: string) {
    setAddError("");
    setContent(newContent);
  }

  async function handleAddClick() {
    setAddError("");

    // TODO:
    //  Fuehre die 'addCommentMutation' aus und verarbeite das Ergebnis:
    //  - Als neuen "content" für den Kommentar kannst du den 'content' aus
    //    dem State verwenden
    //  - Die Mutation liefert einen Union-Typ zurück
    //  - Ob Du den Fehler UnionType zurückbekommen hast, kannst Du mit der
    //    Funktion 'isAddCommentFailed' überprüfen
    const { errors, data } = await addComment({
      variables: {
        storyId,
        content,
      },
    });
    //  - Im "fachlichen" Fehlerfall setze die übergebene Fehlermeldung als 'addError'
    //    in den State
    //    - Du kannst einen Fehler provozieren, in dem Du einen Kommentar
    //      eingibst und speicherst, der weniger als fünf Zeichen lang ist
    if (data) {
      if (isAddCommentFailed(data.result)) {
        setAddError(data.result.errorMsg);
        return;
      }
    }
    //  - Wenn Du auch technische Fehler verarbeiten willst (errors-Field),
    //     kannst Du ebenfalls einen Fehler in 'addError' setzen
    //     - Einen technischen Error kannst Du provozieren, in dem Du
    //       die Mutation nicht eingeloggt ausführst.
    //       Dazu musst Du 'buttonDisabled' auf false setzen, da der 'Add'
    //        Button normalerweise disabled ist, wenn man nicht eingeloggt ist
    //  - Im Erfolgsfall kannst du das Eingabefeld wieder leeren
    //    (content-State leer machen)
    if (!errors || errors.length === 0) {
      setContent("");
    }
  }

  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-8"}>
        {me && <img src={me.profileImage} alt={me.user?.name || "n/a"} />}
      </div>
      <div
        className={
          "w-full border rounded-md border border-2 border-orange-100 p-2 bg-orange-50 drop-shadow-xl"
        }
      >
        <Stack orientation={"vertical"}>
          <span className={"font-bold text-base mr-2"}>Add your comment</span>
          <div className={"w-full"}>
            <textarea
              value={content}
              onChange={(c) => handleContentChange(c.target.value)}
              disabled={editorDisabled}
              rows={5}
              className={"w-full p-2"}
              placeholder={
                editorDisabled ? "Please login first" : "Add comment"
              }
            />
          </div>
          <ButtonBar>
            <Button onClick={handleAddClick} small disabled={buttonDisabled}>
              Submit
            </Button>
            <Button
              small
              disabled={buttonDisabled}
              onClick={() => handleContentChange("")}
              type={"blue"}
            >
              Cancel
            </Button>
          </ButtonBar>
          {addError && (
            <div className={"font-bold text-orange-700"}>
              Could not add comment: {addError}
            </div>
          )}
        </Stack>
      </div>
    </Stack>
  );
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
function isAddCommentFailed(
  result?: AddCommentMutation["result"]
): result is AddCommentErrorFragment {
  return (
    result !== undefined && result.__typename === "AddCommentFailedPayload"
  );
}
