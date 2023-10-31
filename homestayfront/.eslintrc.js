module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/typescript/recommended'
  ],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    // 关闭名称校验
    'vue/multi-word-component-names': "off",
    'vue/no-unused-components': "off",
    'vue/no-unused-vars':"off",
    // 'returnCitySN':"off"
  }
  ,
  // 解决returnCitySN未定义问题
  // globals:{ 
  //   'returnCitySN': true
  // }
}
