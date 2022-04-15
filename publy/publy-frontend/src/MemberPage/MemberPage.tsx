import PageLayout from "../components/PageLayout";
import * as React from "react";
import Card, { CardHeader, CardItem } from "../components/Card";
import {
  CakeIcon,
  ChatIcon,
  LocationMarkerIcon,
  MailIcon,
  PencilIcon,
} from "@heroicons/react/outline";
import Stack from "../components/Stack";
import StoryTeaserList from "../components/StoryTeaserList";
import { Link, useParams } from "react-router-dom";
import { MemberPageQuery, useMemberPageQuery } from "../generated/graphql";
import { formatLongDate, formatShortDate } from "../utils/formatDateTime";

type Member = NonNullable<MemberPageQuery["member"]>;

type ProfileHeaderProps = {
  member: Member;
};

function ProfileHeader({ member }: ProfileHeaderProps) {
  return (
    <div className={"bg-orange-50 rounded-md border border-orange-100"}>
      <div className={"flex flex-col items-center p-8"}>
        <div className={"w-1/6"}>
          <img src={member.profileImageUrl} />
        </div>
        <h2 className={"text-2xl text-orange-500 font-bold font-metro mt-2"}>
          {member.user?.name}
        </h2>
        <div className={"mt-4 p-2 w-3/4 text-center"}>{member.bio}</div>
        <div className={"mt-4 flex justify-between w-3/4 font-light"}>
          <div className={"flex flex-row space-x-2 items-center w-full "}>
            <LocationMarkerIcon className={"h-5 mr-3"} />
            {member.location}
          </div>
          {member.joined && (
            <div className={"flex flex-row space-x-2  items-center  w-full"}>
              <CakeIcon className={"h-5 mr-2"} />
              joined {formatLongDate(member.joined)}
            </div>
          )}
          {member.user?.email && (
            <div className={"flex flex-row space-x-2  items-center  w-full"}>
              <MailIcon className={"h-5 mr-2"} />
              {member.user.email}
            </div>
          )}
        </div>
      </div>

      {member.works && (
        <div className={"border-t-2 border-t-orange-200"}>
          <div className={"flex flex-col items-center pt-4 pr-16 pl-16 pb-8"}>
            <div className={"mt-4 flex justify-around w-full"}>
              <div className={"text-center"}>
                <div className={"font-light"}>Works</div>
                <div>{member.works}</div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

type MemberSidebarProps = {
  member: Member;
};

function MemberSidebar({ member }: MemberSidebarProps) {
  return (
    <aside>
      <Stack>
        {member.currentlyLearning && (
          <Card>
            <CardHeader title={"Currently Learning"} />
            <CardItem>{member.currentlyLearning}</CardItem>
          </Card>
        )}
        {member.skills && (
          <Card>
            <CardHeader title={"Skills"} />
            <CardItem>{member.skills}</CardItem>
          </Card>
        )}
        <Card>
          <CardItem>
            <Stack>
              <div className={"flex"}>
                <PencilIcon className={"h-5 mr-5"} />
                {member.stories.totalCount} stories written
              </div>
              <div className={"flex"}>
                <ChatIcon className={"h-5 mr-5"} />
                {member.comments.totalCount} comments written
              </div>
            </Stack>
          </CardItem>
        </Card>
      </Stack>
    </aside>
  );
}

type CommentsListProps = {
  comments: NonNullable<MemberPageQuery["member"]>["comments"]["edges"];
};

function CommentsList({ comments }: CommentsListProps) {
  return (
    <section className={"bg-orange-50 rounded-md border border-orange-100"}>
      <h2 className={"font-bold tracking-wide p-4"}>Recent comments</h2>
      {comments.map(({ node }) => {
        return (
          <Link to={`/s/${node.story.id}`} key={node.id}>
            <div
              className={
                "border-t-2 border-orange-100 p-4 hover:bg-orange-100 hover:cursor-pointer"
              }
            >
              <Stack>
                <div>
                  <span className={"mr-4"}>{node.story.title}</span>
                  <span className={"font-thin text-sm"}>
                    {formatShortDate(node.createdAt)}
                  </span>
                </div>
                <div className={"font-light"}>{node.content}</div>
              </Stack>
            </div>
          </Link>
        );
      })}
    </section>
  );
}

export default function MemberPage() {
  const { memberId = "" } = useParams<{ memberId: string }>();

  console.log("MEMBERID", memberId);
  const { loading, data, error } = useMemberPageQuery({
    variables: { memberId },
  });

  if (loading) {
    return (
      <PageLayout>
        <h1>Loading...</h1>
      </PageLayout>
    );
  }

  if (error || !data) {
    console.error("Could not load member", error);
    return <PageLayout>Error! Could not load member!</PageLayout>;
  }

  const { member } = data;

  if (!member) {
    return (
      <PageLayout>
        <h1>Error: Member {memberId} not found!</h1>
      </PageLayout>
    );
  }

  return (
    <PageLayout title={"..."}>
      <ProfileHeader member={member} />
      <div
        className={
          "mt-8 grid grid-cols-[1fr_2fr] gap-4 grid-rows-[min-content]"
        }
      >
        <MemberSidebar member={member} />
        <Stack>
          <CommentsList comments={member.comments.edges} />
          <StoryTeaserList edges={member.stories.edges} />
        </Stack>
      </div>
    </PageLayout>
  );
}
