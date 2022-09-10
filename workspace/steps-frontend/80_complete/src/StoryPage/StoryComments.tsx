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
  const { data, loading, error, subscribeToMore } = useStoryCommentsQuery({
    variables: { storyId },
  });

  React.useEffect(() => {
    subscribeToMore<OnNewCommentSubscription>({
      document: OnNewCommentDocument,
      variables: { storyId },
      updateQuery: (prev, { subscriptionData }) => {
        if (!subscriptionData.data) return prev;
        const newComment = subscriptionData.data.onNewComment.newComment;
        const result: StoryCommentsQuery = Object.assign({}, prev, {
          comments: [newComment, ...prev.comments],
        });
        return result;
      },
    });
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
