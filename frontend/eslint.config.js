// For more info, see https://github.com/storybookjs/eslint-plugin-storybook#configuration-flat-config-format
import storybook from "eslint-plugin-storybook";

import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'
import prettier from 'eslint-config-prettier'
import pluginPrettier from 'eslint-plugin-prettier'
import { defineConfig, globalIgnores } from 'eslint/config'

export default defineConfig([globalIgnores(['dist']), // Prettier disable conflicting rules
prettier, {
  files: ['**/*.{ts,tsx}'],
  plugins: {
    prettier: pluginPrettier,
  },
  rules: {
    'prettier/prettier': 'error',
  },
  languageOptions: {
    ecmaVersion: 2020,
    globals: globals.browser,
  },
  extends: [
    js.configs.recommended,
    tseslint.configs.recommended,
    reactHooks.configs.flat.recommended,
    reactRefresh.configs.vite,
  ],
}, ...storybook.configs["flat/recommended"]])