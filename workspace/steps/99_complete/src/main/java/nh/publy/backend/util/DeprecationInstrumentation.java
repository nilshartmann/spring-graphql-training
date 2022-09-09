package nh.publy.backend.util;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.GraphQLFieldDefinition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static graphql.execution.instrumentation.SimpleInstrumentationContext.noOp;
import static graphql.schema.GraphQLTypeUtil.simplePrint;

@Component
public class DeprecationInstrumentation extends SimpleInstrumentation {
  static class DeprecationState implements InstrumentationState {

    private final List<DeprecatedField> deprecatedFields = new LinkedList<>();

    public Optional<List<DeprecatedField>> snapshot() {
      return deprecatedFields.isEmpty() ? Optional.empty() : Optional.of(deprecatedFields);
    }

    private static class DeprecatedField {
      private final String name;
      private final String path;
      private final String reason;

      public DeprecatedField(String name, String path, String reason) {
        this.name = name;
        this.path = path;
        this.reason = reason;
      }

      public String getName() {
        return name;
      }

      public String getPath() {
        return path;
      }

      public String getReason() {
        return reason;
      }
    }

    public void addDeprecatedField(String name, String path, String reason) {
      this.deprecatedFields.add(new DeprecatedField(name, path, reason));
    }
  }

  @Override
  public InstrumentationState createState() {
    return new DeprecationState();
  }

  @Override
  public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
    Map<Object, Object> currentExt = executionResult.getExtensions();

    DeprecationState deprecationState = parameters.getInstrumentationState();
    Map<Object, Object> newExt = deprecationState.snapshot().map(snapshot -> {
      Map<Object, Object> withTracingExt = new LinkedHashMap<>(currentExt == null ? Collections.emptyMap() : currentExt);
      withTracingExt.put("deprecations", snapshot);
      return withTracingExt;
    }).orElse(currentExt);


    return CompletableFuture.completedFuture(
      new ExecutionResultImpl(
        executionResult.getData(),
        executionResult.getErrors(),
        newExt)
    );
  }

  @Override
  public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {

    GraphQLFieldDefinition field = parameters.getField();

    if (field.isDeprecated()) {
      DeprecationState deprecationState = parameters.getInstrumentationState();
      ExecutionStepInfo executionStepInfo = parameters.getEnvironment().getExecutionStepInfo();
      var reason = field.getDeprecationReason();
      var fieldName = simplePrint(executionStepInfo.getParent().getUnwrappedNonNullType()) + "." + executionStepInfo.getFieldDefinition().getName();
      var path = executionStepInfo.getPath().toString();
      deprecationState.addDeprecatedField(fieldName, path, reason);
    }

    return noOp();
  }
}