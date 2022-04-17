import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter } from "react-router-dom";
import {
  ApolloClient,
  ApolloLink,
  ApolloProvider,
  createHttpLink,
  FetchResult,
  InMemoryCache,
  Observable,
  Operation,
  split,
} from "@apollo/client";
import { setContext } from "@apollo/client/link/context";
import { Client, ClientOptions, createClient } from "graphql-ws";
import { GraphQLError, print } from "graphql";
import {
  getMainDefinition,
  relayStylePagination,
} from "@apollo/client/utilities";
import { onError } from "@apollo/client/link/error";
import { graphqlApiUrl, graphqlWsApiUrl } from "./urls";

class GraphQLWsWebSocketLink extends ApolloLink {
  private client: Client;

  constructor(options: ClientOptions) {
    super();
    this.client = createClient(options);
  }

  public request(operation: Operation): Observable<FetchResult> {
    return new Observable((sink) => {
      return this.client.subscribe<FetchResult>(
        { ...operation, query: print(operation.query) },
        {
          next: sink.next.bind(sink),
          complete: sink.complete.bind(sink),
          error: (err) => {
            if (err instanceof Error) {
              console.error("WebSocket Error", err);
              return sink.error(err);
            }

            if (err instanceof CloseEvent) {
              console.error("WebSocket Closed", err);
              return sink.error(
                // reason will be available on clean closes
                new Error(
                  `Socket closed with event ${err.code} ${err.reason || ""}`
                )
              );
            }

            return sink.error(
              new Error(
                (err as GraphQLError[]).map(({ message }) => message).join(", ")
              )
            );
          },
        }
      );
    });
  }
}

const httpLink = createHttpLink({
  uri: graphqlApiUrl,
});

const wsLink = new GraphQLWsWebSocketLink({
  url: graphqlWsApiUrl,
  connectionParams: () => {
    return {};
  },
});

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
      Authorization: `Bearer ${token}`,
    },
  };
});

const client = new ApolloClient({
  link: ApolloLink.from([errorLink, authLink, remoteLink]),
  cache: new InMemoryCache({
    typePolicies: {
      Query: {
        fields: {
          //https://www.apollographql.com/docs/react/pagination/cursor-based/#relay-style-cursor-pagination
          stories: relayStylePagination(),
        },
      },
    },
  }),
});

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <ApolloProvider client={client}>
        <App />
      </ApolloProvider>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
