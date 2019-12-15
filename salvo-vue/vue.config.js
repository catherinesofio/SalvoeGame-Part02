module.exports = {
  devServer: {
    proxy: {
      "/api": {
        target: "https://neko-voe.herokuapp.com/",
        changeOrigin: true
      }
    }
  }
}