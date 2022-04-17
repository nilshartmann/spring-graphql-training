// More info about initialization & config:
// - https://revealjs.com/initialization/
// - https://revealjs.com/config/

const isLocalServer =
  window.location.hostname.indexOf("localhost") !== -1 ||
  window.location.hostname.indexOf("127.0.0.1") !== -1;

// Reveal.addEventListener("ready", function (event) {

// });

Reveal.initialize({
  hash: true,
  fragmentInURL: false,
  fragments: false,

  controls: true,
  progress: true,
  history: true,
  center: true,

  width: "100%",
  height: "100%",
  hideInactiveCursor: false,

  // Learn about plugins: https://revealjs.com/plugins/
  plugins: [RevealMarkdown, RevealHighlight, RevealNotes],
}).then(() => {
  if (isLocalServer) {
    // only applies to presentation version
    Reveal.configure({ controls: false, fragments: true });

    document.querySelectorAll("li").forEach((n) => n.classList.add("fragment"));
    // document.querySelectorAll("li.fragment:first-child").forEach((n) => {
    //   n.classList.remove("fragment");
    //   n.parentNode.classList.add("fragment");
    // });

    // when using code blocks with markdown like this:
    //  ```java fragment
    //  the code elements gets the "fragment" class
    document.querySelectorAll("code.fragment").forEach((n) => {
      n.classList.remove("fragment");
      n.parentNode.classList.add("fragment");
    });
    document.querySelectorAll("pre.fragment").forEach((n) => {
      n.classList.remove("fragment");
      const div = document.createElement("div");
      div.classList.add("fragment");
      n.parentNode.insertBefore(div, n);
      div.appendChild(n);
    });

    document.querySelectorAll("li > pre.code-wrapper").forEach((n) => {
      n.parentNode.classList.add("no-icon");
    });
  } else {
    // public version
    document.querySelectorAll(".demo .demo").forEach((n) => n.remove());
    document.querySelectorAll(".todo").forEach((n) => n.remove());
  }

  // document.querySelectorAll("code[data-line-numbers]").forEach((el) => {
  //   delete el.dataset.lineNumbers;
  //   console.log(el.dataset);
  // });

  // open all externals link in new tab
  document.querySelectorAll('a:not([href^="#"])').forEach((i) => {
    i.setAttribute("target", "_blank");
  });

  document.querySelectorAll("pre code").forEach((n) => {
    n.setAttribute("contenteditable", "true");
  });
});
