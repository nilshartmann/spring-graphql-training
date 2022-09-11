const backendHost = window.__publy__backend_host__.replace(
  "__BACKEND__",
  "http://localhost:8090"
);
const userserviceHost = window.__publy__userservice_host__.replace(
  "__USERSERVICE__",
  "http://localhost:8091"
);

export const userServiceUrl = `${userserviceHost}`;
export const graphqlApiUrl = `${backendHost}/graphql`;

export const graphqlWsApiUrl = `${backendHost}/graphql`
  .replaceAll("https", "wss")
  .replace("http", "ws");

export const graphiqlUrl = `${backendHost}/`;

console.log("USING GRAPHQL URL", graphqlApiUrl);
console.log("USING GRAPHQL WS URL", graphqlWsApiUrl);
console.log("USERSERVICE URL", userServiceUrl);
