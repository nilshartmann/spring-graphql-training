# Übung: Query Language

Per ngrok am fertigen Schema ein Query ausführen

# Übung Schema Beschreiben:

type Story {
id: ID!

title: String!
body: String!
}

type Member {
id: ID!

userId: ID!
profileImage: String!
}

type Query {
stories: [Story!]!
story(id: ID!): Story
}

# Übung: Query Mapping

---

## Übungen

- Eins: Story-Typ definieren, stories am Query-Typ, in GraphiQL ansehen

- stories-Feld implementieren (QueryMapping) mit einem Argument

- SchemaMapping, Member aus User-Service, gleich async

- SchemaMapping mit User-Service

- Mutation mit Security? input-Type

# TODO

Noch nicht aus den PDF-Slides übernommen:

- GraphQL Request (HTTP POST, wie sieht JSON Antwort aus)
- The DataFetcherEnvironment
  - nur erwähnen, dass es das gibt
  - später im Zusammenhand mit DataFetcherFieldSelectionsEt
