import * as React from "react";
import { useFeedPageQuery } from "../generated/graphql";
import Button from "../components/Button";
import ButtonBar from "../components/ButtonBar";
import LoadingIndicator from "../components/LoadingIndicator";
import StoryTeaserList from "./StoryTeaserList";

export default function FeedPage() {
  // https://www.apollographql.com/docs/react/pagination/core-api/#the-fetchmore-function
  const { loading, data, error, fetchMore } = useFeedPageQuery({
    variables: {
      page: 0,
    },
  });

  if (error) {
    console.log("ERROR", error);
    return <h1>Error while loading Data!</h1>;
  }

  if (loading || !data) {
    return (
      <div className={"mt-5 w-full"}>
        <LoadingIndicator />
      </div>
    );
  }

  const hasMore = data?.stories.page.hasNextPage;

  const handleFetchMore = () => {
    if (!hasMore) {
      return;
    }

    fetchMore({
      variables: {
        page: data.stories.page.pageNumber + 1,
      },
    });
  };

  return (
    <div>
      <StoryTeaserList stories={data!.stories.stories} />

      <ButtonBar align={"center"}>
        <Button disabled={!hasMore} onClick={handleFetchMore}>
          Load more
        </Button>
      </ButtonBar>
    </div>
  );
}
