import Stack from "../components/Stack";
import PageLayout from "../components/PageLayout";
import * as React from "react";
import Card, { CardHeader, CardLink } from "../components/Card";
import StoryTeaserList from "../components/StoryTeaserList";
import { useFeedPageQuery, useFeedSidebarQuery } from "../generated/graphql";
import Button from "../components/Button";
import ButtonBar from "../components/ButtonBar";
import LoadingIndicator from "../components/LoadingIndicator";

function FeedSidebar() {
  const { data, error } = useFeedSidebarQuery();
  if (error) {
    console.error("Loading TopTags failed", error);
    return (
      <Stack>
        <h1>Loading Tags failed</h1>
      </Stack>
    );
  }
  if (!data) {
    return null;
  }

  const { topTags } = data;
  return (
    <Stack>
      {topTags.map((topTag) => (
        <Card key={topTag.tagName}>
          <CardHeader
            to={`/t/${topTag.tagName}`}
            title={`#${topTag.tagName}`}
          />

          {topTag.stories.map((story) => (
            <CardLink
              key={story.id}
              to={`/s/${story.id}`}
              title={story.title}
              subtitle={`${story.comments.totalCount} comments`}
            />
          ))}
        </Card>
      ))}
    </Stack>
  );
}

//

export default function FeedPage() {
  // https://www.apollographql.com/docs/react/pagination/core-api/#the-fetchmore-function
  const { loading, data, error, fetchMore } = useFeedPageQuery({
    notifyOnNetworkStatusChange: true,
  });

  if (error) {
    console.log("ERROR", error);
    return <PageLayout>Error while loading Data!</PageLayout>;
  }

  if (!data && !loading) {
    return (
      <PageLayout>
        Sorry... loading your feed failed... no data received 😥
      </PageLayout>
    );
  }

  const hasMore = data?.stories.pageInfo.hasNextPage;

  const handleFetchMore = () => {
    if (!hasMore) {
      return;
    }

    fetchMore({
      variables: {
        after: data.stories.pageInfo.endCursor,
      },
    });
  };

  return (
    <PageLayout>
      <div className={"grid grid-cols-[2fr_1fr] gap-4"}>
        <div>
          {error ? "Error" : null}
          {!!data && <StoryTeaserList edges={data.stories.edges} />}
          {loading && (
            <div className={"mt-5 w-full"}>
              <LoadingIndicator />
            </div>
          )}
          {!loading && (
            <ButtonBar align={"center"}>
              <Button disabled={!hasMore} onClick={handleFetchMore}>
                Load more
              </Button>
            </ButtonBar>
          )}
        </div>
        <FeedSidebar />
      </div>
    </PageLayout>
  );
}
