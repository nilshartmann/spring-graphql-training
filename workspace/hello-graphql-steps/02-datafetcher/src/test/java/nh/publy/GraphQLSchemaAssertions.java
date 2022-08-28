package nh.publy;

import graphql.language.*;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nils Hartmann
 */
public class GraphQLSchemaAssertions {
  public static GraphQLSchemaAssertion assertGraphQLSchema(String schemaLocationInClasspath) {
    SchemaParser schemaParser = new SchemaParser();
    InputStream schemaStream = GraphQLSchemaAssertions.class.getResourceAsStream(schemaLocationInClasspath);
    assertThat(schemaStream).as(String.format(
      "SDL file not found. Please make sure, you have created the file '%s' in 'src/resources/'", schemaLocationInClasspath)
    ).isNotNull();

    TypeDefinitionRegistry registry = schemaParser.parse(schemaStream);
    return assertGraphQLSchema(registry);
  }

  public static GraphQLSchemaAssertion assertGraphQLSchema(TypeDefinitionRegistry registry) {
    return new GraphQLSchemaAssertion(registry);
  }

  static class GraphQLSchemaAssertion {
    private final TypeDefinitionRegistry typeDefinitionRegistry;

    GraphQLSchemaAssertion(TypeDefinitionRegistry typeDefinitionRegistry) {
      this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    GraphQLTypeAssertion withType(final String typeName) {
      TypeDefinition<?> typeDefinition = typeDefinitionRegistry.getType(typeName).orElse(null);
      assertThat(typeDefinition)
        .as(String.format("Schema should contain type '%s'", typeName))
        .isNotNull();
      return new GraphQLTypeAssertion(typeDefinition);
    }

    public GraphQLTypeAssertion withInputType(String inputTypeName) {
      GraphQLTypeAssertion typeAssertion = withType(inputTypeName);
      assertThat(typeAssertion.typeDefinition)
        .as(String.format("Type '%s' should be an 'input' type", inputTypeName))
        .isInstanceOf(InputObjectTypeDefinition.class);

      return typeAssertion;
    }


    class GraphQLTypeAssertion {
      private final TypeDefinition<?> typeDefinition;

      public GraphQLTypeAssertion(TypeDefinition<?> typeDefinition) {
        this.typeDefinition = typeDefinition;
      }

      public GraphQLSchemaAssertion schema() {
        return GraphQLSchemaAssertion.this;
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

        public GraphQLArgumentAssertion withArgument(String expectedArgumentName) {
          InputValueDefinition inputValueDefinition = getInputValueDefinition(expectedArgumentName);
          return new GraphQLArgumentAssertion(inputValueDefinition);
        }

        public GraphQLFieldAssertion ofType(String expectedTypeName) {
          Type<?> type = this.fieldDefinition.getType();

          final String qualifiedName = typeDefinition.getName() + "." + fieldDefinition.getName();

          assertOfType(type, qualifiedName, expectedTypeName);
          return this;
        }

        public GraphQLFieldAssertion withDescription() {
          final Description description = this.fieldDefinition.getDescription();
          assertThat(
            description != null && description.getContent().length() > 0)
            .as(String.format("%s.%s should have description",
              typeDefinition.getName(),
              fieldDefinition.getName()))
            .isTrue();
          return this;
        }


        public GraphQLFieldAssertion withDescription(String descriptionSubstring) {
          final Description description = this.fieldDefinition.getDescription();
          assertThat(
            description != null && description.getContent().contains(descriptionSubstring))
            .as(String.format("%s.%s should have description containing '%s'",
              typeDefinition.getName(),
              fieldDefinition.getName(),
              descriptionSubstring))
            .isTrue();
          return this;
        }

        private void assertOfType(Type<?> type, String qualifiedName, String expectedTypeName) {

          boolean expectedNonNullable = expectedTypeName.endsWith("!");
          expectedTypeName = expectedNonNullable ? expectedTypeName.substring(0, expectedTypeName.length() - 1) : expectedTypeName;

          if (expectedNonNullable) {
            assertThat(type)
              .as(String.format("%s should be non-nullable, but is nullable!", qualifiedName))
              .isInstanceOf(NonNullType.class);

            type = ((NonNullType) type).getType();
          } else {
            assertThat(type)
              .as(String.format("%s should be nullable, but is non-nullable!", qualifiedName))
              .isNotInstanceOf(NonNullType.class);
          }

          boolean shouldBeList = expectedTypeName.matches("\\[.*]");
          expectedTypeName = shouldBeList ? expectedTypeName.substring(1, expectedTypeName.length() - 1) : expectedTypeName;

          if (shouldBeList) {
            assertThat(type)
              .as(String.format("%s should be List/Array type", qualifiedName))
              .isInstanceOf(ListType.class);

            type = ((ListType) type).getType();
            assertOfType(type, qualifiedName + ", items in List", expectedTypeName);

          } else {
            TypeName typeName = (TypeName) type;

            assertThat(typeName.getName())
              .as(String.format("%s should be of type '%s'", qualifiedName, expectedTypeName))
              .isEqualTo(expectedTypeName);
          }

        }

        class GraphQLArgumentAssertion {
          private final InputValueDefinition inputValueDefinition;

          private GraphQLArgumentAssertion(InputValueDefinition inputValueDefinition) {
            this.inputValueDefinition = inputValueDefinition;
          }

          public GraphQLArgumentAssertion ofType(String expectedType) {
            Type<?> argumentType = inputValueDefinition.getType();

            assertOfType(argumentType,
              String.format("Argument '%s' on '%s.%s'", inputValueDefinition.getName(), typeDefinition.getName(), fieldDefinition.getName()),
              expectedType);

            return this;
          }

          public GraphQLFieldAssertion and() {
            return GraphQLFieldAssertion.this;
          }

          public GraphQLSchemaAssertion schema() {
            return GraphQLSchemaAssertion.this;
          }
        }


        private InputValueDefinition getInputValueDefinition(String argumentName) {
          InputValueDefinition inputValueDefinition = (InputValueDefinition) fieldDefinition
            .getNamedChildren()
            .getChildren("inputValueDefinition")
            .stream().filter(node -> {
              if (!(node instanceof InputValueDefinition)) {
                return false;
              }

              return ((InputValueDefinition) node).getName().equals(argumentName);
            }).findAny().orElse(null);

          assertThat(inputValueDefinition)
            .as(String.format("Field %s.%s should have an argument '%s'", typeDefinition.getName(), fieldDefinition.getName(), argumentName))
            .isNotNull();

          return inputValueDefinition;
        }
      }

      private FieldDefinition fieldDefinition(String fieldName) {
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
          as(String.format("Type '%s' should have a field called '%s'", typeDefinition.getName(), fieldName))
          .isNotNull();

        assertThat(child)
          .as(String.format("Type '%s' should have a field called '%s'", typeDefinition.getName(), fieldName))
          .isInstanceOf(FieldDefinition.class);

        FieldDefinition f = (FieldDefinition) child;
        return f;
      }
    }
  }
}
