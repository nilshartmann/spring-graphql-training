package nh.publy;

import graphql.language.*;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest {

  private final static String SCHEMA_LOCATION = "/graphql/hello-graphql-java.graphqls";

  @Test
  void memberTypeIsCorrect() {
    assertGraphQLSchema()
      .type("Member")
        .isPresent()
          .withField("id")
            .ofNonNullableType("ID")
          .and().withField("profileImage")
            .ofNonNullableType("String")
    ;
  }

  @Test
  void storyTypeIsCorrect() {
    assertGraphQLSchema()
      .type("Story")
      .isPresent()
      .withField("id").ofNonNullableType("ID")
      .and().withField("title").ofNonNullableType("String")
      .and().withField("body").ofNonNullableType("String")
      .and().withField("writtenBy").ofNonNullableType("Member")
    ;
  }


  @Test
  void queryTypeIsCorrect() {
    assertGraphQLSchema()
      .type("Query")
        .isPresent()
        .withField("stories")
          .ofNonNullableType("[Story]")
          .and().withField("story")
            .ofNullableType("Story")
            .withArgument("storyId", "ID")
    ;
  }

  private final GraphQLSchemaAssertion assertGraphQLSchema() {
    SchemaParser schemaParser = new SchemaParser();
    InputStream schemaStream = getClass().getResourceAsStream(SCHEMA_LOCATION);
    assertThat(schemaStream).as(String.format(
      "SDL file not found. Please make sure, you have created the file '%s' in 'src/resources/graphql'", SCHEMA_LOCATION)
    ).isNotNull();

    TypeDefinitionRegistry registry = schemaParser.parse(schemaStream);
    return new GraphQLSchemaAssertion(registry);
  }

  class GraphQLSchemaAssertion {
    private final TypeDefinitionRegistry typeDefinitionRegistry;

    GraphQLSchemaAssertion(TypeDefinitionRegistry typeDefinitionRegistry) {
      this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    GraphQLTypeAssertion type(final String typeName) {
      return new GraphQLTypeAssertion(typeName);
    }


    class GraphQLTypeAssertion {
      private final String typeName;

      public GraphQLTypeAssertion(String typeName) {
        this.typeName = typeName;
      }

      public GraphQLTypeAssertion isPresent() {
        assertThat(typeDefinitionRegistry.getType("Member"))
          .as("Member type should be defined")
          .isPresent();
        return this;
      }

      public GraphQLFieldAssertion withField(String fieldName) {
        FieldDefinition fieldDefinition = fieldDefinition(fieldName);

        return new GraphQLFieldAssertion(fieldDefinition);
      }

      class GraphQLFieldAssertion {
        private final FieldDefinition fieldDefinition;

        private GraphQLFieldAssertion(FieldDefinition fieldDefinition) {
          this.fieldDefinition = fieldDefinition;
        }

        public GraphQLTypeAssertion and() {
          return GraphQLTypeAssertion.this;
        }

        public GraphQLFieldAssertion ofNullableType(String expectedType) {
          Type<?> type = this.fieldDefinition.getType();
          String fieldName = this.fieldDefinition.getName();

          assertThat(type)
            .as(String.format("Field '%s.%s' should be nullable, but is non-nullable!", typeName, fieldName))
            .isNotInstanceOf(NonNullType.class);

          return this;
        }

        public GraphQLFieldAssertion ofNonNullableType(String expectedType) {
          Type<?> type = this.fieldDefinition.getType();
          String fieldName = this.fieldDefinition.getName();

          assertNonNullableType(type,
            String.format("Field '%s.%s'", typeName, fieldName),
            expectedType
          );

          return this;
        }

        public GraphQLFieldAssertion withArgument(String expectedArgumentName, String expectedType) {
          InputValueDefinition inputValueDefinition = (InputValueDefinition) fieldDefinition
            .getNamedChildren()
            .getChildren("inputValueDefinition")
            .stream().filter(node -> {
              if (!(node instanceof InputValueDefinition)) {
                return false;
              }

              return ((InputValueDefinition) node).getName().equals(expectedArgumentName);
            }).findAny().orElse(null);

          assertThat(inputValueDefinition)
            .as(String.format("Field %s.%s should have an argument '%s'", typeName, fieldDefinition.getName(), expectedArgumentName))
            .isNotNull();

          Type<?> argumentType = inputValueDefinition.getType();
          assertNonNullableType(argumentType,
            String.format("Argument '%s' on '%s.%s'", expectedArgumentName, typeName, fieldDefinition.getName()),
            expectedType);

          return this;
        }

        private void assertNonNullableType(Type<?> type, String qualifiedName, String expectedType) {

          assertThat(type)
            .as(String.format("%s should be non-nullable, but is nullable!", qualifiedName))
            .isInstanceOf(NonNullType.class);

          NonNullType nonNullType = (NonNullType) type;

          boolean shouldBeList = expectedType.matches("\\[.*]");
          expectedType = shouldBeList ? expectedType.substring(1, expectedType.length() - 1) : expectedType;

          if (shouldBeList) {
            assertThat(nonNullType.getType())
              .as(String.format("%s should be List/Array type", qualifiedName))
              .isInstanceOf(ListType.class);

            nonNullType = (NonNullType) ((ListType) nonNullType.getType()).getType();

          } else {
            assertThat(nonNullType.getType())
              .as(String.format("%s should not be List/Array type", qualifiedName))
              .isNotInstanceOf(ListType.class);
          }

          String fieldTypeName = ((TypeName) nonNullType.getType()).getName();
          assertThat(fieldTypeName)
            .as(String.format("%s has wrong type '%s'", typeName, qualifiedName))
            .isEqualTo(expectedType);
        }
      }

      private FieldDefinition fieldDefinition(String fieldName) {
        TypeDefinition<?> typeDefinition = typeDefinitionRegistry.getType(typeName).orElseThrow();
        Node<?> child = typeDefinition.getChildren()
          .stream().filter(node -> {
            if (!(node instanceof FieldDefinition)) {
              return false;
            }
            FieldDefinition fieldDefinition = (FieldDefinition) node;
            return fieldDefinition.getName().equals(fieldName);
          })
          .findAny()
          .orElse(null);

        assertThat(child).
          as(String.format("Type '%s' should have a field called '%s'", typeName, fieldName))
          .isNotNull();

        assertThat(child)
          .as(String.format("Type '%s' should have a field called '%s'", typeName, fieldName))
          .isInstanceOf(FieldDefinition.class);

        FieldDefinition f = (FieldDefinition) child;
        return f;
      }
    }
  }
}
