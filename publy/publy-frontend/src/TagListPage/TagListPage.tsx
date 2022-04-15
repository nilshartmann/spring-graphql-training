import { useParams } from "react-router-dom";
import { useTagListQuery } from "../generated/graphql";
import PageLayout from "../components/PageLayout";
import StoryTeaserList from "../components/StoryTeaserList";
import Stack from "../components/Stack";

export default function TagListPage() {
  const { tagName = "" } = useParams<{ tagName: string }>();

  const { loading, data, error } = useTagListQuery({
    variables: { tagName },
  });

  if (loading) {
    return <PageLayout>Stories are loading...</PageLayout>;
  }

  if (error || !data) {
    console.error("Error while loading stories", error);
    return <PageLayout>Loading stories failed!</PageLayout>;
  }

  return (
    <PageLayout>
      <Stack>
        <div
          className={
            "bg-rose-50 rounded-md border border-rose-100 p-8 align-middle"
          }
        >
          <div className={"flex justify-between h-full"}>
            <h2 className={"font-bold text-2xl"}>#{tagName}</h2>
            <div className={"font-light flex items-center"}>
              {data.stories.totalCount} Stories
            </div>
          </div>
        </div>
        <StoryTeaserList edges={data.stories.edges} activeTag={tagName} />
      </Stack>
    </PageLayout>
  );
}
