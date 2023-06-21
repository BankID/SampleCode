module.exports = {
  env: {
    browser: true,
    es2022: true,
  },
  parserOptions: {
    ecmaVersion: 'latest'
  },
  extends: [
    'plugin:react/recommended',
    'airbnb',
  ],
  plugins: [
    'react',
    'react-hooks',
    'import',
    'jsx-a11y',
    'license-header'
  ],
  rules: {
    'react/react-in-jsx-scope': 0,
    'jsx-quotes': ['error', 'prefer-single'],
    'react/function-component-definition': [2, { 'namedComponents': 'arrow-function' }],
    'react/require-default-props': 0,
    'react/jsx-props-no-spreading': 0,
    'react/jsx-filename-extension': 0,
    'import/prefer-default-export': 0,
    'react/prop-types': 0,
    'license-header/header': [ 'error', './license.js' ]
  },
  ignorePatterns: [
    '.eslintrc.js',
    'license.js',
    'target/*',
    'build/*',
  ],
};
