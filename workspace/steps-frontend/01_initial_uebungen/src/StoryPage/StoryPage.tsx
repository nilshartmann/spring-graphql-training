import * as React from "react";
import Stack from "../components/Stack";
import Markdown from "../components/Markdown";
import { Link as RouterLink, useLocation, useParams } from "react-router-dom";
import { formatShortDate } from "../utils/formatDateTime";
import { StoryPageQuery, useStoryPageQuery } from "../generated/graphql";
import { StoryComments } from "./StoryComments";
import { useMe } from "../shared/useMe";

export default function StoryPage() {
  const { storyId = "" } = useParams<{
    storyId: string;
  }>();
  const { loggedIn } = useMe();
  const { pathname } = useLocation();

  const { data, loading, error } = useStoryPageQuery({
    variables: {
      storyId,
    },
  });

  if (error) {
    console.error("fetch error", error);
    return <h1>Error!</h1>;
  }

  return (
    <div>
      {!!error && "error!"}
      {loading && "Loading..."}
      {data && data.story && (
        <div>
          <SingleStory story={data.story} />
          {loggedIn || (
            <div>
              You need to{" "}
              <RouterLink
                to={"/login"}
                state={{ redirectTo: pathname }}
                className={"underline text-rose-500 font-bold"}
              >
                login
              </RouterLink>{" "}
              before you can give a reaction!
            </div>
          )}
          <StoryComments storyId={data.story.id} />
        </div>
      )}
    </div>
  );
}

function Author({ story }: SingleStoryProps) {
  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-1/12"}>
        <img
          alt={"Profile image of " + story.writtenBy.user?.name}
          src={story.writtenBy.profileImage}
        />
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
  return (
    <Stack>
      <Author story={story} />
      <h1 className={"text-4xl font-bold font-metro text-orange-500"}>
        {story.title}
      </h1>
      <Markdown>{story.body}</Markdown>
    </Stack>
  );
}
