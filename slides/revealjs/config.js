// More info about initialization & config:
// - https://revealjs.com/initialization/
// - https://revealjs.com/config/

const isLocalServer =
  window.location.hostname.indexOf("localhost") !== -1 ||
  window.location.hostname.indexOf("127.0.0.1") !== -1;

Reveal.addEventListener("ready", function (event) {
  console.log("CONFIGURE.........................", isLocalServer)

  if (isLocalServer) {
    // only applies to presentation version
    Reveal.configure({ controls: false });
  }

  // open all externals link in new tab
  document.querySelectorAll('a:not([href^="#"])').forEach(
    i => {
      i.setAttribute("target", "_blank")
    }
  )

  document.querySelectorAll('pre code').forEach(
    n => {
      n.setAttribute("contenteditable", "true");
//      n.setAttribute("data-trim", "")
    }
  )
});



//$('a:not([href^="#"])').attr("target", "_blank");
//$("pre code").attr("contenteditable", "true");
//$("pre code").attr("data-trim", "");

Reveal.initialize({
  hash: true,

//  highlight: {
//    beforeHighlight: hljs => hljs.registerLanguage(/*...*/)
//  },

  // Learn about plugins: https://revealjs.com/plugins/
  plugins: [RevealMarkdown, RevealHighlight, RevealNotes]
});


