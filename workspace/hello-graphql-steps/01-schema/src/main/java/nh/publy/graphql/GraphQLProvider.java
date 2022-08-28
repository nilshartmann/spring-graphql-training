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

    // TODO:
    //  1. Implementiere die DataFetcher:
    //      - QueryStoriesDataFetcher (für Query.stories)
    //      - StoryByIdDataFetcher (für Query.story)
    //      - ExcerptDataFetcher( für Story.excerpt)
    //  2. Instantiiere die DataFetcher:
    //   - Die Story-Datafetcher benötigen die Instanz StoryRepository von oben
    //      übergib diese per Konstruktor an die DataFetcher

    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
    // TODO:
    //  2. Registriere die DataFetcher hier am RuntimeWiring
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring);

    GraphQL graphql = GraphQL.newGraphQL(graphQLSchema).build();

    return graphql;
  }




}
