const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
    app.use(
        createProxyMiddleware("/challenge-api", {
            target: process.env.REACT_APP_API_URI,
            changeOrigin: true,
        })
    );

    app.use(
        createProxyMiddleware("/start", {
            target: process.env.REACT_APP_STREAM_URI,
            changeOrigin: true,
        })
    );
};
