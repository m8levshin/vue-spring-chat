const path = require("path");

module.exports = {

    outputDir: path.resolve(__dirname, "../webapp"),

    css: {
        extract: false
    },

    chainWebpack: config => {
        config.optimization.delete('splitChunks');

        config.module
            .rule('images')
            .use('url-loader')
            .loader('url-loader')
            .tap(options => Object.assign(options, {limit: 10240}));
    },

    devServer:{
        port: 8081
    }
};
