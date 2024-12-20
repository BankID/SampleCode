import react from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import _import from "eslint-plugin-import";
import jsxA11Y from "eslint-plugin-jsx-a11y";
import licenseHeader from "eslint-plugin-license-header";
import { fixupPluginRules } from "@eslint/compat";
import globals from "globals";
import path from "node:path";
import { fileURLToPath } from "node:url";
import js from "@eslint/js";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
    baseDirectory: __dirname,
    recommendedConfig: js.configs.recommended,
    allConfig: js.configs.all
});

export default [{
    ignores: ["**/eslint.config.mjs", "**/license.js", "target/*", "build/*"],
}, ...compat.extends("plugin:react/recommended"), {
    plugins: {
        react,
        "react-hooks": fixupPluginRules(reactHooks),
        import: fixupPluginRules(_import),
        "jsx-a11y": jsxA11Y,
        "license-header": licenseHeader,
    },

    settings: {
        react: {
          version: "detect",
        },
    },

    languageOptions: {
        globals: {
            ...globals.browser,
        },

        ecmaVersion: "latest",
        sourceType: "module",
    },

    rules: {
        "react/react-in-jsx-scope": 0,
        "jsx-quotes": ["error", "prefer-single"],

        "react/function-component-definition": [2, {
            namedComponents: "arrow-function",
        }],
        "no-console": 1,
        "react-hooks/exhaustive-deps": 1,
        "react/require-default-props": 0,
        "react/jsx-props-no-spreading": 0,
        "react/jsx-filename-extension": 0,
        "import/prefer-default-export": 0,
        "react/prop-types": 0,
        "license-header/header": ["error", "./license.js"],
    },
}];
