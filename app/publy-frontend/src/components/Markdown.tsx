import { Remarkable } from "remarkable";
import hljs from "highlight.js"; // https://highlightjs.org/

type MarkdownProps = {
  markdown?: string;
  children?: string;
};
export default function Markdown({ children, markdown }: MarkdownProps) {
  const md = new Remarkable({
    html: true,
    highlight: function (str, lang) {
      if (lang && hljs.getLanguage(lang)) {
        try {
          return hljs.highlight(lang, str).value;
        } catch (err) {}
      }

      try {
        return hljs.highlightAuto(str).value;
      } catch (err) {}

      return ""; // use external default escaping
    },
  });

  const html = md.render(markdown || children || "");

  return (
    <div className="Markdown">
      <span dangerouslySetInnerHTML={{ __html: html }} />
    </div>
  );
}
