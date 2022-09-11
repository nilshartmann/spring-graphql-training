import { gql } from "@apollo/client";
import * as Apollo from "@apollo/client";
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

export type Query = {
  __typename?: "Query";
  ping?: Maybe<Scalars["String"]>;
};

export type FeedPageQueryVariables = Exact<{ [key: string]: never }>;

export type FeedPageQuery = { __typename?: "Query"; ping?: string | null };

export const FeedPageDocument = gql`
  query FeedPage {
    ping
  }
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
