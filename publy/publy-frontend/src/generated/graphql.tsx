import * as Apollo from "@apollo/client";
import { gql } from "@apollo/client";

export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = {
  [K in keyof T]: T[K];
};
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & {
  [SubKey in K]?: Maybe<T[SubKey]>;
};
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & {
  [SubKey in K]: Maybe<T[SubKey]>;
};
const defaultOptions = {};
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

/** Represents an error message when a comment could not be added */
export type AddCommentFailurePayload = {
  __typename?: "AddCommentFailurePayload";
  /** Error message describing the problem */
  msg: Scalars["String"];
};

/** Input-Type for adding a new `Comment` */
export type AddCommentInput = {
  /** The comment itself */
  content: Scalars["String"];
  /** The id of the `Story` that the comment should be added to */
  storyId: Scalars["ID"];
};

/**
 * Return type for the `addComment` `Mutation`
 *
 * It can either be a `AddCommentFailurePaylod` if adding failed or
 * `AddCommentSuccessPayload` if the comment have been added successfully
 */
export type AddCommentPayload =
  | AddCommentFailurePayload
  | AddCommentSuccessPayload;

/** Returns if a comment has been successfully created */
export type AddCommentSuccessPayload = {
  __typename?: "AddCommentSuccessPayload";
  /** The new comment object that has been added to the story */
  newComment: Comment;
};

/**
 * A Comment is written by a Member for a Story of another member
 *
 * A Comment cannot be changed and does not have a title.
 */
export type Comment = Node & {
  __typename?: "Comment";
  /**
   * Contains the actual content the `Member` has written.
   *
   * Note: Markdown is **not** supported here!
   */
  content: Scalars["String"];
  /** When was this comment written? */
  createdAt: Scalars["String"];
  id: Scalars["ID"];
  /** The `story` this comment referres to */
  story: Story;
  writtenBy: Member;
};

/** Describes a condition that should be used to filter comments from a collection of comments */
export type CommentCondition = {
  field: CommentConditionField;
  value: Scalars["ID"];
};

/** Specifes a field of a `Comment` that can be used to filter comments from a collection */
export enum CommentConditionField {
  Story = "story",
  WrittenBy = "writtenBy",
}

/** A _connection_ of `Comment` objects */
export type CommentConnection = {
  __typename?: "CommentConnection";
  /** Returns all `CommentEdge`s that this connection represents. Might be empty, but never null. */
  edges: Array<CommentEdge>;
  /** Information of the page that this connection represents */
  pageInfo: PageInfo;
  /** total number of objects that is selected by the criteria that was used to create this connection */
  totalCount: Scalars["Int"];
};

/** An Edge that represents a `Comment` */
export type CommentEdge = {
  __typename?: "CommentEdge";
  /** A cursor that points to this object */
  cursor: Scalars["ID"];
  /** The actual `Comment` object */
  node: Comment;
};

/** Specified how a list of comments should be ordered */
export type CommentOrder = {
  /** Sort direction */
  direction: OrderDirection;
  /** Field that should be used for ordering */
  field: CommentOrderField;
};

/** List of fields that a `Comment` can be ordered by */
export enum CommentOrderField {
  /** Order by creation date */
  CreatedAt = "createdAt",
  /** order by member that has written this comment */
  WrittenBy = "writtenBy",
}

/**
 * A Member is a person that is part of our application. It might be backed by a `User` or not.
 *
 * If the Member discards it`s user (leaving our service), the Member stays but is now "offline"
 */
export type Member = Node & {
  __typename?: "Member";
  /** A short biography of this Member in it's own words */
  bio?: Maybe<Scalars["String"]>;
  /**
   * The comments this user has written
   *
   * Note:
   * - you have to specify at least `first` or `last` argument
   * - ou cannot request more than 10 comments at a time
   */
  comments: CommentConnection;
  /** What is this Member currently learning? */
  currentlyLearning?: Maybe<Scalars["String"]>;
  id: Scalars["ID"];
  /** Date, when this Member first joined our service */
  joined: Scalars["String"];
  /** Location, where the Member currenty lives */
  location?: Maybe<Scalars["String"]>;
  /** URL to the profile image of this Member */
  profileImageUrl: Scalars["String"];
  /** Skills that Members has, for example `graphql` or `drinking beers` */
  skills?: Maybe<Scalars["String"]>;
  /**
   * The stories this Member has written.
   *
   * Note:
   *   - you have to specify at least `first` or `last` argument
   *   - ou cannot request more than 10 stories at a time
   */
  stories: StoryConnection;
  /**
   * The `User` that is represented by this member.
   *
   * If the `User` does not exists anymore, null is returned
   */
  user?: Maybe<User>;
  /** Where is this Member working currently */
  works?: Maybe<Scalars["String"]>;
};

/**
 * A Member is a person that is part of our application. It might be backed by a `User` or not.
 *
 * If the Member discards it`s user (leaving our service), the Member stays but is now "offline"
 */
export type MemberCommentsArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
  orderBy?: InputMaybe<CommentOrder>;
};

/**
 * A Member is a person that is part of our application. It might be backed by a `User` or not.
 *
 * If the Member discards it`s user (leaving our service), the Member stays but is now "offline"
 */
export type MemberStoriesArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
  orderBy?: InputMaybe<StoryOrder>;
};

/**
 * This is an example for an **Offset-based** pagination API
 *
 * In contrast, almost all other lists in our application use **Cursor-based** pagination ("Connections")
 *
 * A `MemberPage` represent a list of `Member` objects that are returned by the `members` `Query` field.
 */
export type MemberPage = {
  __typename?: "MemberPage";
  /** Members read in the query */
  content: Array<Member>;
  /** Number of all Members */
  totalElements: Scalars["Int"];
  /** Number of all pages */
  totalPages: Scalars["Int"];
};

export type Mutation = {
  __typename?: "Mutation";
  /**
   * Add a new `Comment` to a `Story`
   *
   * Note:
   * - to add a Comment you have to be logged in. Anonymoud users cannot add comments.
   * - the content lenght must be at least 5 characters
   *
   * Depending on the outcome of this operation:
   * - a `AddCommentFailurePayload` _or_ `AddCommentSuccessPayload` is returned
   * - in case of success, a `OnNewCommentEvent` is raised to inform clients about this new comment
   */
  addComment: AddCommentPayload;
  toggleReaction: ToggleReactionPayload;
};

export type MutationAddCommentArgs = {
  input: AddCommentInput;
};

export type MutationToggleReactionArgs = {
  input: ToggleReactionInput;
};

/**
 * Represents a single Node in our System.
 *
 * All objects in our API implement this Node. Nodes can be queried using the `node` `Query`-field
 */
export type Node = {
  /**
   * The unique identifier of this node.
   *
   * The value should not be interpreted by the client in any way.
   */
  id: Scalars["ID"];
};

/** This event is raised when a new `Comment` has been successfully added to a `Story` */
export type OnNewCommentEvent = {
  __typename?: "OnNewCommentEvent";
  id: Scalars["ID"];
  /** The newly added Comment object */
  newComment: Comment;
  /**
   * The new comment as a `CommentEdge` object that can be used on the client to add it to existing
   * list of CommentEdges (like CommentConnections)
   */
  newCommentEdge: CommentEdge;
};

/** Specifies the direction collections can be sorted by */
export enum OrderDirection {
  /** Order by value **ascending** */
  Asc = "asc",
  /** Order by value **descending** */
  Desc = "desc",
}

/** https://relay.dev/graphql/connections.htm#sec-undefined.PageInfo */
export type PageInfo = {
  __typename?: "PageInfo";
  /** The cursor pointing to the last result */
  endCursor?: Maybe<Scalars["ID"]>;
  /** Is there more data after `endCursor`? */
  hasNextPage: Scalars["Boolean"];
  /** If you would read data before the `startCursor` would you receive data? */
  hasPreviousPage: Scalars["Boolean"];
  /** The cursor pointing to the first result */
  startCursor?: Maybe<Scalars["ID"]>;
};

export type Query = {
  __typename?: "Query";
  comments: CommentConnection;
  /**
   * Return the current logged in Member (as read from the `Authorization` JWT) or null
   * if no member is logged in
   */
  me?: Maybe<Member>;
  /** Returns the `Member` by it's `id` or null, if the `Member` could not be found */
  member?: Maybe<Member>;
  /** Returns Publy's registered members */
  members: MemberPage;
  /**
   * Find stories, ordered and matched by specified criterias
   *
   * Returned is a always a `StoryConnection` that might be empty. Use the fields argument to
   * filter and limit the results that you need
   */
  stories: StoryConnection;
  /**
   * Returns the `Story` with the specified `id` or null if there is no `Story`
   * with that `id`.
   *
   * If you don't specify an `id`, the topmost story is returned
   */
  story?: Maybe<Story>;
  /**
   * Return a list of `TopTags` for the given member.
   *
   * _TopTags_  are tags and stories that are individually collected for a specific member using a very complex
   * and secret in our Publy backend
   */
  topTags: Array<TopTags>;
};

export type QueryCommentsArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  condition?: InputMaybe<CommentCondition>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
  orderBy?: InputMaybe<CommentOrder>;
};

export type QueryMemberArgs = {
  id: Scalars["ID"];
};

export type QueryMembersArgs = {
  page?: InputMaybe<Scalars["Int"]>;
  size?: InputMaybe<Scalars["Int"]>;
};

export type QueryStoriesArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  condition?: InputMaybe<StoryCondition>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
  orderBy?: InputMaybe<StoryOrder>;
};

export type QueryStoryArgs = {
  id?: InputMaybe<Scalars["ID"]>;
};

/**
 * Members can give a short `Reaction` to a `Story` to express their feelings about that story.
 *
 * There are different `ReactionTypes` and each member can give any type of reaction exactly once for each
 * Story
 */
export type Reaction = Node & {
  __typename?: "Reaction";
  createdAt: Scalars["String"];
  /** What member has given this reaction? */
  givenBy: Member;
  id: Scalars["ID"];
  /** Type of the reaction */
  type: ReactionType;
};

export type ReactionByType = {
  __typename?: "ReactionByType";
  givenByMe: Scalars["Boolean"];
  totalCount: Scalars["Int"];
  type: ReactionType;
};

export type ReactionConnection = {
  __typename?: "ReactionConnection";
  /** Returns all `ReactionEdge`s that this connection represents. Might be empty, but never null. */
  edges: Array<ReactionEdge>;
  pageInfo: PageInfo;
  totalCount: Scalars["Int"];
};

export type ReactionEdge = {
  __typename?: "ReactionEdge";
  cursor: Scalars["ID"];
  node: Reaction;
};

export type ReactionSummary = {
  __typename?: "ReactionSummary";
  reactionsByType: Array<ReactionByType>;
};

/** Classification of a `Reaction` that is given to a `Story` */
export enum ReactionType {
  /** ❤️ */
  Heart = "heart",
  /** 😀 */
  Laugh = "laugh",
  /** 👍 */
  Like = "like",
  /** 🍻 */
  Prosit = "prosit",
}

/**
 * A `Story` is the main object in our service.
 *
 * Stories are written and published by `Member`s. Other members can leave a `Comment` or give a `Reaction`
 */
export type Story = Node & {
  __typename?: "Story";
  /** The actual story text. Can contain markdown for formatting */
  body: Scalars["String"];
  /**
   * list all comments of this story
   *
   * Note that you have to specify either `first` or `last` and you cannot query for more than 10 comments at once
   */
  comments: CommentConnection;
  createdAt: Scalars["String"];
  /**
   * Returns a short _excerpt_ of this Story's body.
   *
   * The returned string _does not_ contain any markdown or html code.
   */
  excerpt: Scalars["String"];
  id: Scalars["ID"];
  /** A summary of all reactions given to this Story by Reaction type */
  reactionSummary: ReactionSummary;
  /**
   * list all reactions of this story
   *
   * Note that you have to specify either `first` or `last` and you cannot query for more than 10 reactions at once
   */
  reactions: ReactionConnection;
  /**
   * A `Tag` is a free-form word that categories this Story.
   *
   * Stories can be searched by their tags. This way Memebers can find stories they're interessted in.
   */
  tags: Array<Scalars["String"]>;
  /** The title of this Story, should be short and precise */
  title: Scalars["String"];
  /** Who has written this story */
  writtenBy: Member;
};

/**
 * A `Story` is the main object in our service.
 *
 * Stories are written and published by `Member`s. Other members can leave a `Comment` or give a `Reaction`
 */
export type StoryCommentsArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
  orderBy?: InputMaybe<CommentOrder>;
};

/**
 * A `Story` is the main object in our service.
 *
 * Stories are written and published by `Member`s. Other members can leave a `Comment` or give a `Reaction`
 */
export type StoryExcerptArgs = {
  maxLength?: InputMaybe<Scalars["Int"]>;
};

/**
 * A `Story` is the main object in our service.
 *
 * Stories are written and published by `Member`s. Other members can leave a `Comment` or give a `Reaction`
 */
export type StoryReactionsArgs = {
  after?: InputMaybe<Scalars["ID"]>;
  before?: InputMaybe<Scalars["ID"]>;
  first?: InputMaybe<Scalars["Int"]>;
  last?: InputMaybe<Scalars["Int"]>;
};

export type StoryCondition = {
  field?: InputMaybe<StoryConditionField>;
  value: Scalars["String"];
};

export enum StoryConditionField {
  Tag = "tag",
}

export type StoryConnection = {
  __typename?: "StoryConnection";
  /** Returns all `StoryEdges` that this connection represents. Might be empty, but never null. */
  edges: Array<StoryEdge>;
  pageInfo: PageInfo;
  totalCount: Scalars["Int"];
};

export type StoryEdge = {
  __typename?: "StoryEdge";
  cursor: Scalars["ID"];
  node: Story;
};

export type StoryOrder = {
  direction: OrderDirection;
  field: StoryOrderField;
};

export enum StoryOrderField {
  CreatedAt = "createdAt",
  Reactions = "reactions",
}

export type Subscription = {
  __typename?: "Subscription";
  /** Register for new comments for the given story */
  onNewComment: OnNewCommentEvent;
};

export type SubscriptionOnNewCommentArgs = {
  storyId: Scalars["ID"];
};

export type ToggleReactionInput = {
  reactionType: ReactionType;
  storyId: Scalars["ID"];
};

export type ToggleReactionPayload = {
  __typename?: "ToggleReactionPayload";
  updatedStory: Story;
};

/**
 * _Top Tags_  are a special feature of our service: determined by a high complex and very secret algorithm in the
 * backend we calculate a list of tags and their story that are very useful for the current Member.
 */
export type TopTags = {
  __typename?: "TopTags";
  /**
   * Some _top stories_ for this tag
   *
   * The stories that are returned are collected by our highly secret algorithm that determines the
   * best matching stories for the current member
   */
  stories: Array<Story>;
  /** The name of a Tag */
  tagName: Scalars["String"];
};

/**
 * An accunt of a user that is able to login into our application.
 *
 * Authorization is done in a separate process that also stores other User information, like credentials and roles
 */
export type User = Node & {
  __typename?: "User";
  /** The user's E-Mail adress */
  email: Scalars["String"];
  id: Scalars["ID"];
  /** Fullname of a user */
  name: Scalars["String"];
  /** Username is used to login */
  username: Scalars["String"];
};

export type FeedPageQueryVariables = Exact<{
  after?: InputMaybe<Scalars["ID"]>;
}>;

export type FeedPageQuery = {
  __typename?: "Query";
  stories: {
    __typename?: "StoryConnection";
    pageInfo: {
      __typename?: "PageInfo";
      endCursor?: string | null | undefined;
      hasNextPage: boolean;
    };
    edges: Array<{
      __typename?: "StoryEdge";
      node: {
        __typename?: "Story";
        id: string;
        createdAt: string;
        title: string;
        excerpt: string;
        tags: Array<string>;
        writtenBy: {
          __typename?: "Member";
          id: string;
          profileImageUrl: string;
          user?:
            | { __typename?: "User"; id: string; name: string }
            | null
            | undefined;
        };
        comments: { __typename?: "CommentConnection"; totalCount: number };
        reactions: { __typename?: "ReactionConnection"; totalCount: number };
      };
    }>;
  };
};

export type FeedSidebarQueryVariables = Exact<{ [key: string]: never }>;

export type FeedSidebarQuery = {
  __typename?: "Query";
  topTags: Array<{
    __typename?: "TopTags";
    tagName: string;
    stories: Array<{
      __typename?: "Story";
      id: string;
      title: string;
      comments: { __typename?: "CommentConnection"; totalCount: number };
    }>;
  }>;
};

export type MemberPageQueryVariables = Exact<{
  memberId: Scalars["ID"];
}>;

export type MemberPageQuery = {
  __typename?: "Query";
  member?:
    | {
        __typename?: "Member";
        id: string;
        profileImageUrl: string;
        bio?: string | null | undefined;
        joined: string;
        currentlyLearning?: string | null | undefined;
        location?: string | null | undefined;
        skills?: string | null | undefined;
        works?: string | null | undefined;
        user?:
          | { __typename?: "User"; name: string; email: string }
          | null
          | undefined;
        stories: {
          __typename?: "StoryConnection";
          totalCount: number;
          edges: Array<{
            __typename?: "StoryEdge";
            node: {
              __typename?: "Story";
              id: string;
              createdAt: string;
              title: string;
              excerpt: string;
              tags: Array<string>;
              writtenBy: {
                __typename?: "Member";
                id: string;
                profileImageUrl: string;
                user?:
                  | { __typename?: "User"; id: string; name: string }
                  | null
                  | undefined;
              };
              comments: {
                __typename?: "CommentConnection";
                totalCount: number;
              };
              reactions: {
                __typename?: "ReactionConnection";
                totalCount: number;
              };
            };
          }>;
        };
        comments: {
          __typename?: "CommentConnection";
          totalCount: number;
          edges: Array<{
            __typename?: "CommentEdge";
            node: {
              __typename?: "Comment";
              id: string;
              createdAt: string;
              content: string;
              story: { __typename?: "Story"; id: string; title: string };
            };
          }>;
        };
      }
    | null
    | undefined;
};

export type AddCommentMutationVariables = Exact<{
  storyId: Scalars["ID"];
  content: Scalars["String"];
}>;

export type AddCommentMutation = {
  __typename?: "Mutation";
  result:
    | { __typename?: "AddCommentFailurePayload"; msg: string }
    | {
        __typename?: "AddCommentSuccessPayload";
        newComment: {
          __typename?: "Comment";
          content: string;
          id: string;
          createdAt: string;
          writtenBy: {
            __typename?: "Member";
            id: string;
            profileImageUrl: string;
          };
        };
      };
};

export type OnNewCommentSubscriptionVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type OnNewCommentSubscription = {
  __typename?: "Subscription";
  onNewComment: {
    __typename?: "OnNewCommentEvent";
    newCommentEdge: {
      __typename?: "CommentEdge";
      node: {
        __typename?: "Comment";
        id: string;
        createdAt: string;
        content: string;
        writtenBy: {
          __typename?: "Member";
          id: string;
          profileImageUrl: string;
          user?:
            | { __typename?: "User"; id: string; name: string }
            | null
            | undefined;
        };
      };
    };
  };
};

export type StoryCommentEdgeFragment = {
  __typename?: "CommentEdge";
  node: {
    __typename?: "Comment";
    id: string;
    createdAt: string;
    content: string;
    writtenBy: {
      __typename?: "Member";
      id: string;
      profileImageUrl: string;
      user?:
        | { __typename?: "User"; id: string; name: string }
        | null
        | undefined;
    };
  };
};

export type StoryCommentsQueryVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type StoryCommentsQuery = {
  __typename?: "Query";
  comments: {
    __typename?: "CommentConnection";
    totalCount: number;
    edges: Array<{
      __typename?: "CommentEdge";
      node: {
        __typename?: "Comment";
        id: string;
        createdAt: string;
        content: string;
        writtenBy: {
          __typename?: "Member";
          id: string;
          profileImageUrl: string;
          user?:
            | { __typename?: "User"; id: string; name: string }
            | null
            | undefined;
        };
      };
    }>;
  };
};

export type StoryPageQueryVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type StoryPageQuery = {
  __typename?: "Query";
  story?:
    | {
        __typename?: "Story";
        id: string;
        createdAt: string;
        title: string;
        tags: Array<string>;
        body: string;
        writtenBy: {
          __typename?: "Member";
          bio?: string | null | undefined;
          id: string;
          profileImageUrl: string;
          user?:
            | { __typename?: "User"; id: string; name: string }
            | null
            | undefined;
        };
        reactionSummary: {
          __typename?: "ReactionSummary";
          reactionsByType: Array<{
            __typename?: "ReactionByType";
            type: ReactionType;
            totalCount: number;
            givenByMe: boolean;
          }>;
        };
        reactions: {
          __typename?: "ReactionConnection";
          edges: Array<{
            __typename?: "ReactionEdge";
            node: { __typename?: "Reaction"; createdAt: string; id: string };
          }>;
        };
      }
    | null
    | undefined;
};

export type ToggleReactionMutationVariables = Exact<{
  storyId: Scalars["ID"];
  reactionType: ReactionType;
}>;

export type ToggleReactionMutation = {
  __typename?: "Mutation";
  toggleReaction: {
    __typename?: "ToggleReactionPayload";
    updatedStory: {
      __typename?: "Story";
      id: string;
      reactionSummary: {
        __typename?: "ReactionSummary";
        reactionsByType: Array<{
          __typename?: "ReactionByType";
          type: ReactionType;
          givenByMe: boolean;
          totalCount: number;
        }>;
      };
    };
  };
};

export type TagListQueryVariables = Exact<{
  tagName: Scalars["String"];
}>;

export type TagListQuery = {
  __typename?: "Query";
  stories: {
    __typename?: "StoryConnection";
    totalCount: number;
    edges: Array<{
      __typename?: "StoryEdge";
      node: {
        __typename?: "Story";
        id: string;
        createdAt: string;
        title: string;
        excerpt: string;
        tags: Array<string>;
        writtenBy: {
          __typename?: "Member";
          id: string;
          profileImageUrl: string;
          user?:
            | { __typename?: "User"; id: string; name: string }
            | null
            | undefined;
        };
        comments: { __typename?: "CommentConnection"; totalCount: number };
        reactions: { __typename?: "ReactionConnection"; totalCount: number };
      };
    }>;
  };
};

export type MeQueryVariables = Exact<{ [key: string]: never }>;

export type MeQuery = {
  __typename?: "Query";
  me?:
    | {
        __typename?: "Member";
        id: string;
        profileImageUrl: string;
        user?: { __typename?: "User"; name: string } | null | undefined;
      }
    | null
    | undefined;
};

export type BasicMemberFragment = {
  __typename?: "Member";
  id: string;
  profileImageUrl: string;
  user?: { __typename?: "User"; id: string; name: string } | null | undefined;
};

export type TeaserListFragment = {
  __typename?: "StoryConnection";
  edges: Array<{
    __typename?: "StoryEdge";
    node: {
      __typename?: "Story";
      id: string;
      createdAt: string;
      title: string;
      excerpt: string;
      tags: Array<string>;
      writtenBy: {
        __typename?: "Member";
        id: string;
        profileImageUrl: string;
        user?:
          | { __typename?: "User"; id: string; name: string }
          | null
          | undefined;
      };
      comments: { __typename?: "CommentConnection"; totalCount: number };
      reactions: { __typename?: "ReactionConnection"; totalCount: number };
    };
  }>;
};

export type StoryTeaserFragment = {
  __typename?: "Story";
  id: string;
  createdAt: string;
  title: string;
  excerpt: string;
  tags: Array<string>;
  writtenBy: {
    __typename?: "Member";
    id: string;
    profileImageUrl: string;
    user?: { __typename?: "User"; id: string; name: string } | null | undefined;
  };
  comments: { __typename?: "CommentConnection"; totalCount: number };
  reactions: { __typename?: "ReactionConnection"; totalCount: number };
};

export const BasicMemberFragmentDoc = gql`
  fragment BasicMember on Member {
    id
    profileImageUrl
    user {
      id
      name
    }
  }
`;
export const StoryCommentEdgeFragmentDoc = gql`
  fragment StoryCommentEdge on CommentEdge {
    node {
      id
      writtenBy {
        ...BasicMember
      }
      createdAt
      content
    }
  }
  ${BasicMemberFragmentDoc}
`;
export const StoryTeaserFragmentDoc = gql`
  fragment StoryTeaser on Story {
    id
    createdAt
    title
    excerpt
    writtenBy {
      ...BasicMember
    }
    comments {
      totalCount
    }
    reactions {
      totalCount
    }
    tags
  }
  ${BasicMemberFragmentDoc}
`;
export const TeaserListFragmentDoc = gql`
  fragment TeaserList on StoryConnection {
    edges {
      node {
        ...StoryTeaser
      }
    }
  }
  ${StoryTeaserFragmentDoc}
`;
export const FeedPageDocument = gql`
  query FeedPage($after: ID) {
    stories(
      first: 3
      after: $after
      orderBy: { direction: desc, field: createdAt }
    ) {
      pageInfo {
        endCursor
        hasNextPage
      }
      edges {
        node {
          ...StoryTeaser
        }
      }
    }
  }
  ${StoryTeaserFragmentDoc}
`;

/**
 * __useFeedPageQuery__
 *
 * To run a query within a React component, call `useFeedPageQuery` and pass it any options that fit your needs.
 * When your component renders, `useFeedPageQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFeedPageQuery({
 *   variables: {
 *      after: // value for 'after'
 *   },
 * });
 */
export function useFeedPageQuery(
  baseOptions?: Apollo.QueryHookOptions<FeedPageQuery, FeedPageQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<FeedPageQuery, FeedPageQueryVariables>(
    FeedPageDocument,
    options
  );
}

export function useFeedPageLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    FeedPageQuery,
    FeedPageQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<FeedPageQuery, FeedPageQueryVariables>(
    FeedPageDocument,
    options
  );
}

export type FeedPageQueryHookResult = ReturnType<typeof useFeedPageQuery>;
export type FeedPageLazyQueryHookResult = ReturnType<
  typeof useFeedPageLazyQuery
>;
export type FeedPageQueryResult = Apollo.QueryResult<
  FeedPageQuery,
  FeedPageQueryVariables
>;
export const FeedSidebarDocument = gql`
  query FeedSidebar {
    topTags {
      tagName
      stories {
        id
        title
        comments {
          totalCount
        }
      }
    }
  }
`;

/**
 * __useFeedSidebarQuery__
 *
 * To run a query within a React component, call `useFeedSidebarQuery` and pass it any options that fit your needs.
 * When your component renders, `useFeedSidebarQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFeedSidebarQuery({
 *   variables: {
 *   },
 * });
 */
export function useFeedSidebarQuery(
  baseOptions?: Apollo.QueryHookOptions<
    FeedSidebarQuery,
    FeedSidebarQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<FeedSidebarQuery, FeedSidebarQueryVariables>(
    FeedSidebarDocument,
    options
  );
}

export function useFeedSidebarLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    FeedSidebarQuery,
    FeedSidebarQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<FeedSidebarQuery, FeedSidebarQueryVariables>(
    FeedSidebarDocument,
    options
  );
}

export type FeedSidebarQueryHookResult = ReturnType<typeof useFeedSidebarQuery>;
export type FeedSidebarLazyQueryHookResult = ReturnType<
  typeof useFeedSidebarLazyQuery
>;
export type FeedSidebarQueryResult = Apollo.QueryResult<
  FeedSidebarQuery,
  FeedSidebarQueryVariables
>;
export const MemberPageDocument = gql`
  query MemberPage($memberId: ID!) {
    member(id: $memberId) {
      id
      profileImageUrl
      user {
        name
        email
      }
      bio
      joined
      currentlyLearning
      location
      skills
      works
      stories(orderBy: { field: createdAt, direction: desc }, first: 5) {
        totalCount
        edges {
          node {
            ...StoryTeaser
          }
        }
      }
      comments(orderBy: { field: createdAt, direction: desc }, first: 5) {
        totalCount
        edges {
          node {
            id
            createdAt
            content
            story {
              id
              title
            }
          }
        }
      }
    }
  }
  ${StoryTeaserFragmentDoc}
`;

/**
 * __useMemberPageQuery__
 *
 * To run a query within a React component, call `useMemberPageQuery` and pass it any options that fit your needs.
 * When your component renders, `useMemberPageQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useMemberPageQuery({
 *   variables: {
 *      memberId: // value for 'memberId'
 *   },
 * });
 */
export function useMemberPageQuery(
  baseOptions: Apollo.QueryHookOptions<
    MemberPageQuery,
    MemberPageQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<MemberPageQuery, MemberPageQueryVariables>(
    MemberPageDocument,
    options
  );
}

export function useMemberPageLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    MemberPageQuery,
    MemberPageQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<MemberPageQuery, MemberPageQueryVariables>(
    MemberPageDocument,
    options
  );
}

export type MemberPageQueryHookResult = ReturnType<typeof useMemberPageQuery>;
export type MemberPageLazyQueryHookResult = ReturnType<
  typeof useMemberPageLazyQuery
>;
export type MemberPageQueryResult = Apollo.QueryResult<
  MemberPageQuery,
  MemberPageQueryVariables
>;
export const AddCommentDocument = gql`
  mutation AddComment($storyId: ID!, $content: String!) {
    result: addComment(input: { content: $content, storyId: $storyId }) {
      ... on AddCommentSuccessPayload {
        newComment {
          content
          id
          createdAt
          writtenBy {
            id
            profileImageUrl
          }
        }
      }
      ... on AddCommentFailurePayload {
        msg
      }
    }
  }
`;
export type AddCommentMutationFn = Apollo.MutationFunction<
  AddCommentMutation,
  AddCommentMutationVariables
>;

/**
 * __useAddCommentMutation__
 *
 * To run a mutation, you first call `useAddCommentMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddCommentMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addCommentMutation, { data, loading, error }] = useAddCommentMutation({
 *   variables: {
 *      storyId: // value for 'storyId'
 *      content: // value for 'content'
 *   },
 * });
 */
export function useAddCommentMutation(
  baseOptions?: Apollo.MutationHookOptions<
    AddCommentMutation,
    AddCommentMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<AddCommentMutation, AddCommentMutationVariables>(
    AddCommentDocument,
    options
  );
}

export type AddCommentMutationHookResult = ReturnType<
  typeof useAddCommentMutation
>;
export type AddCommentMutationResult =
  Apollo.MutationResult<AddCommentMutation>;
export type AddCommentMutationOptions = Apollo.BaseMutationOptions<
  AddCommentMutation,
  AddCommentMutationVariables
>;
export const OnNewCommentDocument = gql`
  subscription OnNewComment($storyId: ID!) {
    onNewComment(storyId: $storyId) {
      newCommentEdge {
        node {
          id
          writtenBy {
            id
            user {
              id
              name
            }
            profileImageUrl
          }
          createdAt
          content
        }
      }
    }
  }
`;

/**
 * __useOnNewCommentSubscription__
 *
 * To run a query within a React component, call `useOnNewCommentSubscription` and pass it any options that fit your needs.
 * When your component renders, `useOnNewCommentSubscription` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the subscription, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useOnNewCommentSubscription({
 *   variables: {
 *      storyId: // value for 'storyId'
 *   },
 * });
 */
export function useOnNewCommentSubscription(
  baseOptions: Apollo.SubscriptionHookOptions<
    OnNewCommentSubscription,
    OnNewCommentSubscriptionVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useSubscription<
    OnNewCommentSubscription,
    OnNewCommentSubscriptionVariables
  >(OnNewCommentDocument, options);
}

export type OnNewCommentSubscriptionHookResult = ReturnType<
  typeof useOnNewCommentSubscription
>;
export type OnNewCommentSubscriptionResult =
  Apollo.SubscriptionResult<OnNewCommentSubscription>;
export const StoryCommentsDocument = gql`
  query StoryComments($storyId: ID!) {
    comments(
      first: 10
      orderBy: { field: createdAt, direction: desc }
      condition: { field: story, value: $storyId }
    ) {
      totalCount
      edges {
        ...StoryCommentEdge
      }
    }
  }
  ${StoryCommentEdgeFragmentDoc}
`;

/**
 * __useStoryCommentsQuery__
 *
 * To run a query within a React component, call `useStoryCommentsQuery` and pass it any options that fit your needs.
 * When your component renders, `useStoryCommentsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useStoryCommentsQuery({
 *   variables: {
 *      storyId: // value for 'storyId'
 *   },
 * });
 */
export function useStoryCommentsQuery(
  baseOptions: Apollo.QueryHookOptions<
    StoryCommentsQuery,
    StoryCommentsQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<StoryCommentsQuery, StoryCommentsQueryVariables>(
    StoryCommentsDocument,
    options
  );
}

export function useStoryCommentsLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    StoryCommentsQuery,
    StoryCommentsQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<StoryCommentsQuery, StoryCommentsQueryVariables>(
    StoryCommentsDocument,
    options
  );
}

export type StoryCommentsQueryHookResult = ReturnType<
  typeof useStoryCommentsQuery
>;
export type StoryCommentsLazyQueryHookResult = ReturnType<
  typeof useStoryCommentsLazyQuery
>;
export type StoryCommentsQueryResult = Apollo.QueryResult<
  StoryCommentsQuery,
  StoryCommentsQueryVariables
>;
export const StoryPageDocument = gql`
  query StoryPage($storyId: ID!) {
    story(id: $storyId) {
      id
      createdAt
      title
      tags
      body
      writtenBy {
        ...BasicMember
        bio
      }
      reactionSummary {
        reactionsByType {
          type
          totalCount
          givenByMe
        }
      }
      reactions(first: 10) {
        edges {
          node {
            createdAt
            id
          }
        }
      }
    }
  }
  ${BasicMemberFragmentDoc}
`;

/**
 * __useStoryPageQuery__
 *
 * To run a query within a React component, call `useStoryPageQuery` and pass it any options that fit your needs.
 * When your component renders, `useStoryPageQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useStoryPageQuery({
 *   variables: {
 *      storyId: // value for 'storyId'
 *   },
 * });
 */
export function useStoryPageQuery(
  baseOptions: Apollo.QueryHookOptions<StoryPageQuery, StoryPageQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<StoryPageQuery, StoryPageQueryVariables>(
    StoryPageDocument,
    options
  );
}

export function useStoryPageLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    StoryPageQuery,
    StoryPageQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<StoryPageQuery, StoryPageQueryVariables>(
    StoryPageDocument,
    options
  );
}

export type StoryPageQueryHookResult = ReturnType<typeof useStoryPageQuery>;
export type StoryPageLazyQueryHookResult = ReturnType<
  typeof useStoryPageLazyQuery
>;
export type StoryPageQueryResult = Apollo.QueryResult<
  StoryPageQuery,
  StoryPageQueryVariables
>;
export const ToggleReactionDocument = gql`
  mutation ToggleReaction($storyId: ID!, $reactionType: ReactionType!) {
    toggleReaction(input: { storyId: $storyId, reactionType: $reactionType }) {
      updatedStory {
        id
        reactionSummary {
          reactionsByType {
            type
            givenByMe
            totalCount
          }
        }
      }
    }
  }
`;
export type ToggleReactionMutationFn = Apollo.MutationFunction<
  ToggleReactionMutation,
  ToggleReactionMutationVariables
>;

/**
 * __useToggleReactionMutation__
 *
 * To run a mutation, you first call `useToggleReactionMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useToggleReactionMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [toggleReactionMutation, { data, loading, error }] = useToggleReactionMutation({
 *   variables: {
 *      storyId: // value for 'storyId'
 *      reactionType: // value for 'reactionType'
 *   },
 * });
 */
export function useToggleReactionMutation(
  baseOptions?: Apollo.MutationHookOptions<
    ToggleReactionMutation,
    ToggleReactionMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<
    ToggleReactionMutation,
    ToggleReactionMutationVariables
  >(ToggleReactionDocument, options);
}

export type ToggleReactionMutationHookResult = ReturnType<
  typeof useToggleReactionMutation
>;
export type ToggleReactionMutationResult =
  Apollo.MutationResult<ToggleReactionMutation>;
export type ToggleReactionMutationOptions = Apollo.BaseMutationOptions<
  ToggleReactionMutation,
  ToggleReactionMutationVariables
>;
export const TagListDocument = gql`
  query TagList($tagName: String!) {
    stories(
      first: 10
      condition: { field: tag, value: $tagName }
      orderBy: { field: createdAt, direction: desc }
    ) {
      totalCount
      edges {
        node {
          ...StoryTeaser
        }
      }
    }
  }
  ${StoryTeaserFragmentDoc}
`;

/**
 * __useTagListQuery__
 *
 * To run a query within a React component, call `useTagListQuery` and pass it any options that fit your needs.
 * When your component renders, `useTagListQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useTagListQuery({
 *   variables: {
 *      tagName: // value for 'tagName'
 *   },
 * });
 */
export function useTagListQuery(
  baseOptions: Apollo.QueryHookOptions<TagListQuery, TagListQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<TagListQuery, TagListQueryVariables>(
    TagListDocument,
    options
  );
}

export function useTagListLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<TagListQuery, TagListQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<TagListQuery, TagListQueryVariables>(
    TagListDocument,
    options
  );
}

export type TagListQueryHookResult = ReturnType<typeof useTagListQuery>;
export type TagListLazyQueryHookResult = ReturnType<typeof useTagListLazyQuery>;
export type TagListQueryResult = Apollo.QueryResult<
  TagListQuery,
  TagListQueryVariables
>;
export const MeDocument = gql`
  query Me {
    me {
      id
      user {
        name
      }
      profileImageUrl
    }
  }
`;

/**
 * __useMeQuery__
 *
 * To run a query within a React component, call `useMeQuery` and pass it any options that fit your needs.
 * When your component renders, `useMeQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useMeQuery({
 *   variables: {
 *   },
 * });
 */
export function useMeQuery(
  baseOptions?: Apollo.QueryHookOptions<MeQuery, MeQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<MeQuery, MeQueryVariables>(MeDocument, options);
}

export function useMeLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<MeQuery, MeQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<MeQuery, MeQueryVariables>(MeDocument, options);
}

export type MeQueryHookResult = ReturnType<typeof useMeQuery>;
export type MeLazyQueryHookResult = ReturnType<typeof useMeLazyQuery>;
export type MeQueryResult = Apollo.QueryResult<MeQuery, MeQueryVariables>;
