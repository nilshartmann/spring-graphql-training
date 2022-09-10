import { StoryCommentFragment } from "../generated/graphql";
import Stack from "../components/Stack";
import { formatShortDate } from "../utils/formatDateTime";
import * as React from "react";

type CommentProps = {
  comment: StoryCommentFragment;
};

export function Comment({ comment }: CommentProps) {
  return (
    <Stack orientation={"horizontal"}>
      <div className={"w-8"}>
        <img
          src={comment.writtenBy.profileImage}
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
