import React from "react";
import { createRoot } from "react-dom/client"

import { BrowserRouter } from "react-router-dom";
import { ApolloClient, ApolloLink, ApolloProvider, createHttpLink, InMemoryCache, split } from "@apollo/client";
import { setContext } from "@apollo/client/link/context";
import { createClient } from "graphql-ws";
import { getMainDefinition, relayStylePagination } from "@apollo/client/utilities";
import { onError } from "@apollo/client/link/error";
import { GraphQLWsLink } from "@apollo/client/link/subscriptions";
import App from "./App";
import "./index.css";

import { graphqlApiUrl, graphqlWsApiUrl } from "./urls";

const httpLink = createHttpLink({
  uri: graphqlApiUrl
});
const wsLink = new GraphQLWsLink(
  createClient({
    url: graphqlWsApiUrl
  })
);

// using the ability to split links, you can send data to each link
// depending on what kind of operation is being sent
const remoteLink = split(
  // split based on operation type
  ({ query }) => {
    const def = getMainDefinition(query);
    return (
      def.kind === "OperationDefinition" && def.operation === "subscription"
    );
  },
  wsLink,
  httpLink
);

const errorLink = onError(({ graphQLErrors, networkError }) => {
  if (graphQLErrors) {
    graphQLErrors.map(({ message, locations, path }) =>
      console.log(
        `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
      )
    );
  }
  if (networkError) {
    // no real way to interpret return value, so remove token in all cases
    console.log(`[Network error]: ${networkError}`);
  }
});

const authLink = setContext((_, { headers }) => {
  // get the authentication token from local storage if it exists
  const token = localStorage.getItem("publy.token");
  if (!token) {
    return headers;
  }
  // return the headers to the context so httpLink can read them
  return {
    headers: {
      ...headers,
      Authorization: `Bearer ${token}`
    }
  };
});

const client = new ApolloClient({
  link:  ApolloLink.from([errorLink, authLink, remoteLink]),
  cache: new InMemoryCache({
    typePolicies: {
      Query: {
        fields: {
          //https://www.apollographql.com/docs/react/pagination/cursor-based/#relay-style-cursor-pagination
          stories: relayStylePagination()
        }
      }
    }
  })
});


createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <BrowserRouter>
      <ApolloProvider client={client}>
        <App />
      </ApolloProvider>
    </BrowserRouter>
  </React.StrictMode>
);
