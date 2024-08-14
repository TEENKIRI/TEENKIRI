// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })

module.exports = {
  
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  },
  // configureWebpack: {
  //   // Feature flags 설정
  //   define: {
  //     __VUE_PROD_DEVTOOLS__: JSON.stringify(false),
  //     __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false)
  //   }
  // }
};
