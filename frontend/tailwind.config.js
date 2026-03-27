export default {
  darkMode: 'class',
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  safelist: [
    {
      pattern: /w-column-(1|2|3|4|5|6|7|8|9|10|11|12)/,
      variants: ["sm", "md", "lg", "xl"],
    },
    {
      pattern: /offset-(1|2|3|4|5|6|7|8|9|10|11)/,
      variants: ["sm", "md", "lg", "xl"],
    },
  ],
  theme: {
    extend: {
      screens: {
        xl: "1296px",
      },
    },
  },
  plugins: [],
};