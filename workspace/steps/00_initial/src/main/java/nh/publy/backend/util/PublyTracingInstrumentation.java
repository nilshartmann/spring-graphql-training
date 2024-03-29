package nh.publy.backend.util;

import graphql.com.google.common.collect.ImmutableList;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.execution.instrumentation.tracing.TracingSupport;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TracingInstrumentation, very similiar to original {@link TracingInstrumentation}
 * but writes some other informations that I need for for demonstration purposes.
 */
//@Component
public class PublyTracingInstrumentation extends TracingInstrumentation {
  @Override
  public InstrumentationState createState() {
    return new TracingSupport(false) {
      private final ConcurrentLinkedQueue<Map<String, Object>> fieldData = new ConcurrentLinkedQueue<>();
      private final Map<String, Object> parseMap = new LinkedHashMap<>();
      private final Map<String, Object> validationMap = new LinkedHashMap<>();
      private final long startRequestMillis = System.currentTimeMillis();
      private final AtomicInteger counter = new AtomicInteger();

      public TracingContext beginField(DataFetchingEnvironment dataFetchingEnvironment, boolean isTrivialDataFetcher) {
        long startFieldFetch = System.currentTimeMillis();
        final int fetcherNo = isTrivialDataFetcher ? -1 : counter.incrementAndGet();
        return () -> {
          long now = System.currentTimeMillis();
          long duration = now - startFieldFetch;
          long startOffset = startFieldFetch - startRequestMillis;
          ExecutionStepInfo executionStepInfo = dataFetchingEnvironment.getExecutionStepInfo();

          Map<String, Object> fetchMap = new LinkedHashMap<>();
          String fieldName = executionStepInfo.getPath().toString();

          String source = GraphQLUtils.getFieldContext(dataFetchingEnvironment);

          if (!isTrivialDataFetcher) {
            // "structured" format: only trace non-trivial datafetchers
            fetchMap.put("fetcher_start_no", fetcherNo);
            fetchMap.put("field", fieldName);
            if (source != null) {
              fetchMap.put("description", source);
            }
            fetchMap.put("startedAt_ms", startOffset);
            fetchMap.put("took_ms", duration);
            fetchMap.put("thread", Thread.currentThread().getName());

          }

          if (!fetchMap.isEmpty()) {
            fieldData.add(fetchMap);
          }
        };
      }

      public TracingContext beginParse() {
        return traceToMap(parseMap);
      }

      /**
       * This should be called to start the trace of query validation, with {@link TracingContext#onEnd()} being called to
       * end the call.
       *
       * @return a context to call end on
       */
      public TracingContext beginValidation() {
        return traceToMap(validationMap);
      }

      private TracingContext traceToMap(Map<String, Object> map) {
        long start = System.currentTimeMillis();
        return () -> {
          long now = System.currentTimeMillis();
          long duration = now - start;
          long startOffset = now - startRequestMillis;

          map.put("startOffset_ms", startOffset);
          map.put("duration_ms", duration);
        };
      }

      /**
       * This will snapshot this tracing and return a map of the results
       *
       * @return a snapshot of the tracing data
       */
      public Map<String, Object> snapshotTracingData() {
        if (fieldData.isEmpty()) {
          return null;
        }

        Map<String, Object> traceMap = new LinkedHashMap<>();
        traceMap.put("overall_query_execution_time", System.currentTimeMillis() - startRequestMillis);
        traceMap.put("datafetchers", ImmutableList.copyOf(fieldData));

        return traceMap;
      }
    };
  }
}
