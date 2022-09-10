const colors = require("tailwindcss/colors");
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    colors: {
      white: colors.white,
      "publy-gray": colors.gray,
      "publy-green": colors.green,
      "publy-blue": colors.sky,
      "publy-yellow": colors.yellow,
      fuchsia: colors.fuchsia,
      amber: colors.amber,
      orange: colors.orange,
      rose: colors.rose,
    },
    fontFamily: {
      "open-sans": ["Open Sans", "sans-serif"],
      metro: ["Metropolis", "sans-serif"],
      helvetica: ["Helvetica", "Arial", "sans-serif"],
    },
    extend: {
      //      colors: {
      //
      //        "publy-black": "#191e1e",
      //        "publy-gray-dark": "#333",
      //        "publy-blue": colors.blue["500"],
      //        "publy-green": "#22C55E",
      //        "publy-green-dark": "#16A34A",
      //        "publy-green-light": "#ECFDF5",
      //        "publy-red": "#FF3C38",
      //      },
      fontSize: {
        xxs: ".5rem",
      },
    },
  },
  //  plugins: [require("@tailwindcss/forms")],
  plugins: [],
};
