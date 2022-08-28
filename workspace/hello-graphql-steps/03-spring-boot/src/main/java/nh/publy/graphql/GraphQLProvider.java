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
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class GraphQLProvider {

  private static final Logger log = LoggerFactory.getLogger(GraphQLProvider.class);

  private final StoryRepository storyRepository;
  private final QueryStoriesDataFetcher queryStoriesDataFetcher;
  private final StoryByIdDataFetcher storyByIdDataFetcher;
  private final ExcerptDataFetcher excerptDataFetcher;

  public GraphQLProvider(StoryRepository storyRepository,
                         QueryStoriesDataFetcher queryStoriesDataFetcher,
                         StoryByIdDataFetcher storyByIdDataFetcher,
                         ExcerptDataFetcher excerptDataFetcher) {
    this.storyRepository = storyRepository;
    this.queryStoriesDataFetcher = queryStoriesDataFetcher;
    this.storyByIdDataFetcher = storyByIdDataFetcher;
    this.excerptDataFetcher = excerptDataFetcher;
  }

  public GraphQL getGraphQL() {
    SchemaParser schemaParser = new SchemaParser();
    InputStream schemaStream = getClass().getResourceAsStream("/graphql/hello-graphql-java.graphqls");
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaStream);

    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
      .type("Query", builder -> {
        return builder
          .dataFetcher("stories", queryStoriesDataFetcher)
          .dataFetcher("story", storyByIdDataFetcher);
      })
      .type("Story", builder -> {
        return builder.dataFetcher("excerpt", excerptDataFetcher);
      })
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring);

    GraphQL graphql = GraphQL.newGraphQL(graphQLSchema).build();

    return graphql;
  }




}
