const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
    if (process.env.REACT_APP_NEXT_DEV === "false") {
        app.use(
            createProxyMiddleware("/challenge-api", {
                target: process.env.REACT_APP_PROXY_FOR_API_URI,
                changeOrigin: true,
            })
        );
        app.use(
            createProxyMiddleware("/start", {
                target: process.env.REACT_APP_STREAM_URI,
                changeOrigin: true,
            })
        );
        app.use(
            createProxyMiddleware("/stop", {
                target: process.env.REACT_APP_STREAM_URI,
                changeOrigin: true,
            })
        );
    }
};
