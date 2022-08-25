import PageLayout from "../components/PageLayout";
import * as React from "react";
import Stack from "../components/Stack";
import Card, { CardHeader } from "../components/Card";
import TagLine from "../components/TagLine";
import Markdown from "../components/Markdown";
import { Link as RouterLink, useLocation, useParams } from "react-router-dom";
import {
  OnNewCommentDocument,
  OnNewCommentSubscription,
  ReactionType,
  StoryCommentsQuery,
  StoryPageQuery,
  useAddCommentMutation,
  useStoryCommentsQuery,
  useStoryPageQuery,
  useToggleReactionMutation,
} from "../generated/graphql";
import { formatShortDate } from "../utils/formatDateTime";
import { useMe } from "../shared/useMe";
import Button from "../components/Button";
import ButtonBar from "../components/ButtonBar";
import { XCircleIcon } from "@heroicons/react/outline";

type MemberCardProps = {
  member: NonNullable<StoryPageQuery["story"]>["writtenBy"];
};

function MemberCard({ member }: MemberCardProps) {
  return (
    <Card>
      <div className={"h-4 bg-rose-500 border-rose-500 border-1"}>&nbsp;</div>
      <div className={"flex justify-center  mt-2"}>
        <div className={"w-1/6 mr-4"}>
          <img src={member.profileImageUrl} />
        </div>
        <CardHeader to={`/u/${member.id}`} title={member.user?.name || "na"} />
      </div>

      {!!member.bio && <div className={"p-2 text-sm"}>{member.bio}</div>}
    </Card>
  );
}

type SidebarProps = {
  member: NonNullable<StoryPageQuery["story"]>["writtenBy"];
};

function Sidebar({ member }: SidebarProps) {
  return (
    <Stack>
      <MemberCard member={member} />
    </Stack>
  );
}

export default function StoryPage() {
  const { storyId = "" } = useParams<{
    storyId: string;
  }>();

  const { data, loading, error } = useStoryPageQuery({
    variables: {
      storyId,
    },
  });

  if (error) {
    console.error("fetch error", error);
  }

  return (
    <PageLayout title={"..."}>
      <div className={"grid grid-cols-[2fr_1fr] gap-4 grid-rows-[min-content]"}>
        {!!error && "error!"}
        {loading && "Loading..."}
        {data && data.story && (
          <>
            <div>
              <SingleStory story={data.story} />
              <StoryComments storyId={data.story.id} />
            </div>
            <Sidebar member={data.story.writtenBy} />
          </>
        )}
      </div>
    </PageLayout>
  );
}

function Author({ story }: SingleStoryProps) {
  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-1/12"}>
        <img src={story.writtenBy.profileImageUrl} />
      </div>
      <Stack>
        <div>
          <span
            className={
              "text-orange-900 font-bold hover:bg-orange-100 hover:underline hover:cursor-pointer"
            }
          >
            <RouterLink to={`/u/${story.writtenBy.id}`}>
              {story.writtenBy.user?.name}
            </RouterLink>
          </span>
          <div className={"font-extralight text-xs"}>
            Posted on {formatShortDate(story.createdAt)}
          </div>
        </div>
      </Stack>
    </Stack>
  );
}

type SingleStoryProps = {
  story: NonNullable<StoryPageQuery["story"]>;
};

function SingleStory({ story }: SingleStoryProps) {
  const { me } = useMe();
  const [notLoggedInError, setNotLoggedInError] = React.useState(0);
  const [toggleReaction, { error }] = useToggleReactionMutation();
  const { pathname } = useLocation();

  if (error) {
    console.error("Could not toggle reaction", error);
  }

  async function handleReactionTypeClick(reactionType: ReactionType) {
    if (!me) {
      setNotLoggedInError((current) => current + 1);
      return;
    }
    setNotLoggedInError(0);
    toggleReaction({
      variables: {
        storyId: story.id,
        reactionType,
      },
    });
  }

  return (
    <Stack>
      <Author story={story} />
      <h1 className={"text-4xl font-bold font-metro text-orange-500"}>
        {story.title}
      </h1>
      <TagLine tags={story.tags} />

      <Markdown>{story.body}</Markdown>
      <Stack orientation={"horizontal"} space={10}>
        {story.reactionSummary.reactionsByType.map((reactionByType) => (
          <ReactionBadge
            onReactionClick={() => handleReactionTypeClick(reactionByType.type)}
            key={reactionByType.type}
            reactionCount={reactionByType}
          />
        ))}
      </Stack>
      {notLoggedInError > 0 && (
        <AutoCloseMessage key={notLoggedInError}>
          You need to{" "}
          <RouterLink
            to={"/login"}
            state={{ redirectTo: pathname }}
            className={"underline text-rose-500 font-bold"}
          >
            login
          </RouterLink>{" "}
          before you can give a reaction!
        </AutoCloseMessage>
      )}
    </Stack>
  );
}

type AutoCloseMessageProps = {
  children: React.ReactNode;
};

function AutoCloseMessage({ children }: AutoCloseMessageProps) {
  const [open, setOpen] = React.useState(true);

  React.useEffect(() => {
    if (!open) {
      return;
    }
    const timer = setTimeout(() => setOpen(false), 5000);
    return () => clearTimeout(timer);
  }, [open]);

  if (!open) {
    return null;
  }

  return (
    <div
      className={
        "p-2 border-2 rounded-md border-orange-500 text-orange-500 flex justify-between"
      }
    >
      <div>{children}</div>
      <XCircleIcon
        onClick={() => setOpen(false)}
        className={"w-4 hover:underline hover:cursor-pointer"}
      />
    </div>
  );
}

type ReactionBadgeProps = {
  reactionCount: NonNullable<
    StoryPageQuery["story"]
  >["reactionSummary"]["reactionsByType"][0];
  onReactionClick(): void;
};

function ReactionBadge({ reactionCount, onReactionClick }: ReactionBadgeProps) {
  const icons: Record<ReactionType, string> = {
    laugh: "😀",
    like: "👍",
    heart: "❤️",
    prosit: "🍻",
  };

  const info = (
    <>
      <span className={"h-5 mr-2"}>{icons[reactionCount.type]}</span>
      {reactionCount.totalCount}
    </>
  );

  return reactionCount.givenByMe ? (
    <div
      onClick={onReactionClick}
      className={
        "flex  font-light border-2 border-orange-500 border-2 text-orange-500 border-orange-500 bg-orange-100 p-1 rounded-md hover:cursor-pointer hover:bg-orange-200 hover:border-orange-700"
      }
    >
      {info}
    </div>
  ) : (
    <div
      onClick={onReactionClick}
      className={
        "flex  font-light border-2 border-orange-50 hover:border-2 hover:text-orange-500 hover:border-orange-500 hover:bg-orange-100 p-1 rounded-md hover:cursor-pointer"
      }
    >
      {info}
    </div>
  );
}

type StoryCommentsProps = {
  storyId: string;
};

function StoryComments({ storyId }: StoryCommentsProps) {
  const { data, loading, error, subscribeToMore } = useStoryCommentsQuery({
    variables: { storyId },
  });

  React.useEffect(() => {
    subscribeToMore<OnNewCommentSubscription>({
      document: OnNewCommentDocument,
      variables: { storyId },
      updateQuery: (prev, { subscriptionData }) => {
        if (!subscriptionData.data) return prev;
        const newComment = subscriptionData.data.onNewComment.newCommentEdge;
        const result: StoryCommentsQuery = Object.assign({}, prev, {
          comments: {
            ...prev.comments,
            totalCount: prev.comments.totalCount + 1, // 🤪
            edges: [newComment, ...prev.comments.edges],
          },
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
          {data && data.comments.totalCount} Comments
        </h2>
        {!!error && <h1>Error loading comment</h1>}
        {loading && <h1>Comments loading...</h1>}
        <CommentEditor storyId={storyId} />
        {data &&
          data.comments &&
          data.comments.edges.map(({ node }) => {
            return <Comment key={node.id} comment={node} />;
          })}
      </Stack>
    </div>
  );
}

type CommentEditorProps = {
  storyId: string;
};

function CommentEditor({ storyId }: CommentEditorProps) {
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
    const { errors, data } = await addComment({
      variables: {
        storyId,
        content,
      },
    });

    if (data) {
      if ("msg" in data.result) {
        setAddError(data.result.msg);
        return;
      }
    }

    if (!errors || errors.length === 0) {
      setContent("");
    }
  }

  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-8"}>
        {me && <img src={me.profileImageUrl} alt={me.user?.name || "n/a"} />}
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

type CommentProps = {
  comment: NonNullable<StoryCommentsQuery>["comments"]["edges"][0]["node"];
};

function Comment({ comment }: CommentProps) {
  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-8"}>
        <img
          src={comment.writtenBy.profileImageUrl}
          alt={comment.writtenBy.user?.name || "n/a"}
        />
      </div>
      <div
        className={
          "w-full border rounded-md border border-2 border-orange-100 p-2 bg-orange-50 drop-shadow-xl"
        }
      >
        <Stack>
          <div>
            <span className={"font-bold text-base mr-2"}>
              {comment.writtenBy.user?.name || "n/a"}
            </span>
            <span className={"font-light"}>
              {formatShortDate(comment.createdAt)}
            </span>
          </div>
          <div>{comment.content}</div>
        </Stack>
      </div>
    </Stack>
  );
}
