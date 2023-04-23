package nh.publy.backend.graphql.runtime;
import nh.publy.backend.graphql.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NodeIdConverter implements Converter<String, NodeId>{
  private static final Logger log = LoggerFactory.getLogger( NodeIdConverter.class );
  @Override
  public NodeId convert(String source) {
    return NodeId.parse(source);
  }
}
