import React from "react";
import { createRoot } from "react-dom/client";

import { BrowserRouter } from "react-router-dom";
import { ApolloProvider } from "@apollo/client";
import App from "./App";
import "./index.css";
import { createApolloClient } from "./create-apollo-client";

const apolloClient = createApolloClient();

createRoot(document.getElementById("root") as HTMLElement).render(
  <BrowserRouter>
    <ApolloProvider client={apolloClient}>
      <App />
    </ApolloProvider>
  </BrowserRouter>
);
