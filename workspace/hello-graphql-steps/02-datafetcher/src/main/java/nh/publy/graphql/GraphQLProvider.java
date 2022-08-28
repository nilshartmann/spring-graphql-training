package nh.publy.graphql;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import nh.publy.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class GraphQLProvider {

  private static final Logger log = LoggerFactory.getLogger(GraphQLProvider.class);
  private final StoryRepository storyRepository;

  public GraphQLProvider() {
    this.storyRepository = new StoryRepository();
  }

  public GraphQL getGraphQL() {
    SchemaParser schemaParser = new SchemaParser();
    InputStream schemaStream = getClass().getResourceAsStream("/graphql/hello-graphql-java.graphqls");
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaStream);

    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
      .type("Query", builder -> {
        return builder
          .dataFetcher("stories", new QueryStoriesDataFetcher(storyRepository))
          .dataFetcher("story", new StoryByIdDataFetcher(storyRepository));
      })
      .type("Story", builder -> {
        return builder.dataFetcher("excerpt", new ExcerptDataFetcher());
      })
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring);

    GraphQL graphql = GraphQL.newGraphQL(graphQLSchema).build();

    return graphql;
  }




}
