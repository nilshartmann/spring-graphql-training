package nh.publy.backend.graphql;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {

  private final TextContentRenderer textContentRenderer = TextContentRenderer.builder().
    stripNewlines(true).
    build();

  private final Parser parser = Parser.builder().build();

  public String toPlainText(String markdownString) {
    Node node = parser.parse(markdownString);
    String plainText = textContentRenderer.render(node);

    return plainText;
  }

  public String toHtml(String markdownString) {
    Node node = parser.parse(markdownString);
    HtmlRenderer build = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
    return build.render(node);
  }

}
