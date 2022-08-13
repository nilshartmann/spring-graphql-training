declare var process: any;

const _backendHost = window.__publy__backend_host__ || "http://localhost:8080";
const _userserviceHost =
  window.__publy__userservice_host__ || "http://localhost:8081";

const backendHost = _backendHost.replace(
  "__BACKEND__",
  "http://localhost:8080"
);
const userserviceHost = _userserviceHost.replace(
  "__USERSERVICE__",
  "http://localhost:8081"
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
