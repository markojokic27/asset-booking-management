# Frontend Setup and Running

This project uses **React + Vite + TypeScript + Tailwind CSS**.

---

Before running the project, make sure you have installed:

* **Node.js** (recommended version: 18+)
* **npm** (comes with Node.js)

Check versions:

```bash
node -v
npm -v
```

---

## Installation

Navigate to the frontend directory:

```bash
cd frontend
```

Install all required packages:

```bash
npm install
```

---

## Key Dependencies

The project uses the following main packages:

* react
* react-dom
* vite
* typescript
* tailwindcss
* @tailwindcss/postcss
* postcss
* autoprefixer

All dependencies are installed automatically via `npm install`.

---

## Run the Application

Start the development server:

```bash
npm run dev
```

The app will be available at:

```
http://localhost:5173
```

---

## Production Build

Build the app:

```bash
npm run build
```

Preview the build:

```bash
npm run preview
```

---
##  Recommended VS Code Extension

For a better development experience with Tailwind CSS, install:

  Tailwind CSS IntelliSense

---

## Notes


* If you encounter issues, try:

```bash
rm -rf node_modules package-lock.json
npm install
```

* Restart the server after configuration changes (e.g., Tailwind).

---

  Deafult ReadMe file:
  

# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Oxc](https://oxc.rs)
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/)

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...

      // Remove tseslint.configs.recommended and replace with this
      tseslint.configs.recommendedTypeChecked,
      // Alternatively, use this for stricter rules
      tseslint.configs.strictTypeChecked,
      // Optionally, add this for stylistic rules
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
