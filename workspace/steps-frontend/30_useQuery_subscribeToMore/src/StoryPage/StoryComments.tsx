import {
  OnNewCommentDocument,
  OnNewCommentSubscription,
  StoryCommentsQuery,
  useStoryCommentsQuery,
} from "../generated/graphql";
import * as React from "react";
import Stack from "../components/Stack";
import { CommentEditor } from "./CommentEditor";
import { Comment } from "./Comment";

type StoryCommentsProps = {
  storyId: string;
};

export function StoryComments({ storyId }: StoryCommentsProps) {
  // TODO: Hole die 'subscribeToMore'-Funktion aus dem
  //       Rückgabe-Wert von useStoryCommentsQuery
  const { data, loading, error, subscribeToMore } = useStoryCommentsQuery({
    variables: { storyId },
  });

  React.useEffect(() => {
    // TODO: Verwende die 'subscribeToMore'-Funktion, um neue KOmmentare
    //    in die bestehende Kommentar-Liste im Cache hinzufügen
    //  - Die Subscription die ausgeführt werden soll, ist 'OnNewComment',
    //    die entsprechendne TypeScript-Typen und -Hooks sind dafür bereits
    //    generiert
    //  - Denk' dran, auch den Typ-Parameter zu übergeben, damit updateQuery
    //    korrekt funktioniert
    //  - Als Argument musst Du ein Objekt übergeben, das folgende Eigenschaften hat:

    subscribeToMore<OnNewCommentSubscription>({
      //    - 'document': Das GraphQL Dokument mit der Subscription (ist bereits generiert)
      document: OnNewCommentDocument,
      //    - 'variables': muss die storyId enthalten
      variables: { storyId },
      //    - 'updateQuery', Funktion zum Aktualisieren des Caches:
      //       updateQuery(prev, {subscriptionData}) { ... }
      updateQuery: (prev, { subscriptionData }) => {
        if (!subscriptionData.data) return prev;
        //    - In der updateQuery-Funktion musst Du ein neues Objekt erzeugen,
        //       das ein 'comments'-Feld enthält mit der aktualisierten Liste von Comment-Objekten:
        //        - dem neuen Kommentar, der über die Subscription reingekommen ist und
        //        - den bestehenden Kommentaren aus prev
        //    - Das neue Objekt muss dem TypeScript-Typen 'StoryCommentsQuery' entsprechen
        const newComment = subscriptionData.data.onNewComment.newComment;
        const result: StoryCommentsQuery = Object.assign({}, prev, {
          comments: [newComment, ...prev.comments],
        });
        //    - Das neue Objekt ist der Return-Wert von 'updateQuery', also mit return zurückliefern
        return result;
      },
    });
    // Dependency-Array von React.useEffect:
    //  hier auch 'subscribeToMore' hinzufügen:
    //   [subscribeToMore, storyId]
  }, [subscribeToMore, storyId]);

  if (error) {
    console.error("Fetching comments failed: ", error);
  }

  return (
    <div className={"pt-4"}>
      <Stack space={10}>
        <h2 className={"text-2xl font-metro font-bold text-orange-900"}>
          Comments
        </h2>

        {!!error && <h1>Error loading comment</h1>}
        {loading && <h1>Comments loading...</h1>}
        <CommentEditor storyId={storyId} />
        {data &&
          data.comments &&
          data.comments.map((comment) => {
            return <Comment key={comment.id} comment={comment} />;
          })}
      </Stack>
    </div>
  );
}
