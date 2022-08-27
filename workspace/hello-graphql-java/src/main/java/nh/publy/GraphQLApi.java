package nh.publy;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import nh.publy.domain.Story;
import nh.publy.domain.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class GraphQLApi {

  private static final Logger log = LoggerFactory.getLogger(GraphQLApi.class);

  StoryRepository storyRepository = new StoryRepository();

  String schema = """
    type Member {
      id: ID!
      name: String!
     }
     
    type Story {
      id: ID!
      title: String!
      body: String!
      
      excerpt: String!
      
      writtenBy: Member!
    }
        
    type Query {
      stories: [Story!]!
      
      story(storyId: ID!): Story
    }
    """;

  class QueryStoriesDataFetcher implements DataFetcher<List<Story>> {

    @Override
    public List<Story> get(DataFetchingEnvironment environment) throws Exception {
      return storyRepository.findAllStories();
    }
  }

  class StoryByIdDataFetcher implements DataFetcher<Optional<Story>> {
    @Override
    public Optional<Story> get(DataFetchingEnvironment environment) throws Exception {
      String storyId = environment.getArgument("storyId");
      Optional<Story> story = storyRepository.findStoryById(Long.parseLong(storyId));
      return story;
    }
  }


  class ExcerptDataFetcher implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment environment) throws Exception {
      Story story = environment.getSource();
      String excerpt = story.getBody().substring(0, 3);
      return excerpt;
    }
  }

  public void graphqlApi() {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
      .type("Query", builder -> {
        return builder
          .dataFetcher("stories", new QueryStoriesDataFetcher())
          .dataFetcher("story", new StoryByIdDataFetcher());
      })
      .type("Story", builder -> {
        return builder.dataFetcher("excerpt", new ExcerptDataFetcher());
      })
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring);

    GraphQL graphql = GraphQL.newGraphQL(graphQLSchema).build();

    ExecutionResult executionResult = graphql.execute("query { stories { id title excerpt writtenBy { id } } }");

//    ExecutionResult executionResult = graphql.execute("""
//      query { story(storyId: 100) { id title } }
//      """);

    Object data = executionResult.getData();
    log.info("GraphQL Result {}", data);
  }

  public static void main(String[] args) {
    GraphQLApi graphQLApi = new GraphQLApi();
    graphQLApi.graphqlApi();
  }

}
