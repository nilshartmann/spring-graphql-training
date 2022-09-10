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
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

export type AddCommentFailedPayload = {
  __typename?: "AddCommentFailedPayload";
  errorMsg: Scalars["String"];
};

export type AddCommentInput = {
  content: Scalars["String"];
  storyId: Scalars["ID"];
};

export type AddCommentPayload =
  | AddCommentFailedPayload
  | AddCommentSuccessPayload;

export type AddCommentSuccessPayload = {
  __typename?: "AddCommentSuccessPayload";
  newComment: Comment;
};

export type Comment = {
  __typename?: "Comment";
  content: Scalars["String"];
  createdAt: Scalars["String"];
  id: Scalars["ID"];
  story: Story;
  writtenBy: Member;
};

export type Member = {
  __typename?: "Member";
  /** Short biography of a member. Optional. */
  bio?: Maybe<Scalars["String"]>;
  id: Scalars["ID"];
  profileImage: Scalars["String"];
  stories: Array<Story>;
  user?: Maybe<User>;
};

export type Mutation = {
  __typename?: "Mutation";
  addComment: AddCommentPayload;
};

export type MutationAddCommentArgs = {
  input: AddCommentInput;
};

export type OnNewCommentEvent = {
  __typename?: "OnNewCommentEvent";
  newComment: Comment;
};

export type PageResult = {
  __typename?: "PageResult";
  hasNextPage: Scalars["Boolean"];
  hasPreviousPage: Scalars["Boolean"];
  pageNumber: Scalars["Int"];
  totalCount: Scalars["Int"];
  totalPageCount: Scalars["Int"];
};

export type Query = {
  __typename?: "Query";
  comments: Array<Comment>;
  /**
   * Return the current logged in Member (as read from the `Authorization` JWT) or null
   * if no member is logged in
   */
  me?: Maybe<Member>;
  /**
   * Returns the requested amount of stories, ordered by date, so
   * that newest/latest stories come first
   */
  stories: StoryConnection;
  /** Returns the newest `Story` in our backend or null if no Story available */
  story?: Maybe<Story>;
};

export type QueryCommentsArgs = {
  storyId: Scalars["ID"];
};

export type QueryStoriesArgs = {
  page?: Scalars["Int"];
  pageSize?: Scalars["Int"];
};

export type QueryStoryArgs = {
  storyId?: InputMaybe<Scalars["ID"]>;
};

/** This is a `Story`. */
export type Story = {
  __typename?: "Story";
  body: Scalars["String"];
  comments: Array<Comment>;
  createdAt: Scalars["String"];
  excerpt: Scalars["String"];
  id: Scalars["ID"];
  title: Scalars["String"];
  writtenBy: Member;
};

/** This is a `Story`. */
export type StoryExcerptArgs = {
  maxLength?: Scalars["Int"];
};

/**
 * A **StoryConnection** represents a connection in our graph from one
 * node to a list of `Story` nodes: `Query --> StoryConnection --> Store`
 */
export type StoryConnection = {
  __typename?: "StoryConnection";
  page: PageResult;
  stories: Array<Story>;
};

export type Subscription = {
  __typename?: "Subscription";
  onNewComment: OnNewCommentEvent;
};

export type SubscriptionOnNewCommentArgs = {
  storyId: Scalars["ID"];
};

export type User = {
  __typename?: "User";
  email: Scalars["String"];
  id: Scalars["ID"];
  name: Scalars["String"];
};

export type StoryTeaserFragment = {
  __typename?: "Story";
  id: string;
  title: string;
  createdAt: string;
  excerpt: string;
  writtenBy: {
    __typename?: "Member";
    id: string;
    profileImage: string;
    user?: { __typename?: "User"; name: string } | null;
  };
};

export type FeedPageQueryVariables = Exact<{
  page: Scalars["Int"];
}>;

export type FeedPageQuery = {
  __typename?: "Query";
  stories: {
    __typename?: "StoryConnection";
    page: {
      __typename?: "PageResult";
      pageNumber: number;
      hasNextPage: boolean;
    };
    stories: Array<{
      __typename?: "Story";
      id: string;
      title: string;
      createdAt: string;
      excerpt: string;
      writtenBy: {
        __typename?: "Member";
        id: string;
        profileImage: string;
        user?: { __typename?: "User"; name: string } | null;
      };
    }>;
  };
};

export type AddCommentErrorFragment = {
  __typename?: "AddCommentFailedPayload";
  errorMsg: string;
};

export type AddCommentMutationVariables = Exact<{
  storyId: Scalars["ID"];
  content: Scalars["String"];
}>;

export type AddCommentMutation = {
  __typename?: "Mutation";
  result:
    | { __typename?: "AddCommentFailedPayload"; errorMsg: string }
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
            profileImage: string;
          };
        };
      };
};

export type BasicMemberFragment = {
  __typename?: "Member";
  id: string;
  profileImage: string;
  user?: { __typename?: "User"; id: string; name: string } | null;
};

export type OnNewCommentSubscriptionVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type OnNewCommentSubscription = {
  __typename?: "Subscription";
  onNewComment: {
    __typename?: "OnNewCommentEvent";
    newComment: {
      __typename?: "Comment";
      id: string;
      createdAt: string;
      content: string;
      writtenBy: {
        __typename?: "Member";
        id: string;
        profileImage: string;
        user?: { __typename?: "User"; id: string; name: string } | null;
      };
    };
  };
};

export type StoryCommentFragment = {
  __typename?: "Comment";
  id: string;
  createdAt: string;
  content: string;
  writtenBy: {
    __typename?: "Member";
    id: string;
    profileImage: string;
    user?: { __typename?: "User"; id: string; name: string } | null;
  };
};

export type StoryCommentsQueryVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type StoryCommentsQuery = {
  __typename?: "Query";
  comments: Array<{
    __typename?: "Comment";
    id: string;
    createdAt: string;
    content: string;
    writtenBy: {
      __typename?: "Member";
      id: string;
      profileImage: string;
      user?: { __typename?: "User"; id: string; name: string } | null;
    };
  }>;
};

export type StoryPageQueryVariables = Exact<{
  storyId: Scalars["ID"];
}>;

export type StoryPageQuery = {
  __typename?: "Query";
  story?: {
    __typename?: "Story";
    id: string;
    createdAt: string;
    title: string;
    body: string;
    writtenBy: {
      __typename?: "Member";
      bio?: string | null;
      id: string;
      profileImage: string;
      user?: { __typename?: "User"; id: string; name: string } | null;
    };
    comments: Array<{
      __typename?: "Comment";
      id: string;
      content: string;
      createdAt: string;
      writtenBy: {
        __typename?: "Member";
        user?: { __typename?: "User"; name: string } | null;
      };
    }>;
  } | null;
};

export type MeQueryVariables = Exact<{ [key: string]: never }>;

export type MeQuery = {
  __typename?: "Query";
  me?: {
    __typename?: "Member";
    id: string;
    profileImage: string;
    user?: { __typename?: "User"; name: string } | null;
  } | null;
};

export const StoryTeaserFragmentDoc = gql`
  fragment StoryTeaser on Story {
    id
    title
    createdAt
    excerpt
    writtenBy {
      id
      user {
        name
      }
      profileImage
    }
  }
`;
export const AddCommentErrorFragmentDoc = gql`
  fragment AddCommentError on AddCommentFailedPayload {
    errorMsg
  }
`;
export const BasicMemberFragmentDoc = gql`
  fragment BasicMember on Member {
    id
    profileImage
    user {
      id
      name
    }
  }
`;
export const StoryCommentFragmentDoc = gql`
  fragment StoryComment on Comment {
    id
    writtenBy {
      ...BasicMember
    }
    createdAt
    content
  }
  ${BasicMemberFragmentDoc}
`;
export const FeedPageDocument = gql`
  query FeedPage($page: Int!) {
    stories(page: $page, pageSize: 3) {
      page {
        pageNumber
        hasNextPage
      }
      stories {
        ...StoryTeaser
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
 *      page: // value for 'page'
 *   },
 * });
 */
export function useFeedPageQuery(
  baseOptions: Apollo.QueryHookOptions<FeedPageQuery, FeedPageQueryVariables>
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
            profileImage
          }
        }
      }
      ... on AddCommentFailedPayload {
        ...AddCommentError
      }
    }
  }
  ${AddCommentErrorFragmentDoc}
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
      newComment {
        id
        writtenBy {
          id
          user {
            id
            name
          }
          profileImage
        }
        createdAt
        content
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
    comments(storyId: $storyId) {
      ...StoryComment
    }
  }
  ${StoryCommentFragmentDoc}
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
    story(storyId: $storyId) {
      id
      createdAt
      title
      body
      writtenBy {
        ...BasicMember
        bio
      }
      comments {
        id
        content
        createdAt
        writtenBy {
          user {
            name
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
export const MeDocument = gql`
  query Me {
    me {
      id
      user {
        name
      }
      profileImage
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
