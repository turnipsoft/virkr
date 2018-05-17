var debug = process.env.NODE_ENV !== "production";

var HTMLWebpackPlugin = require('html-webpack-plugin');
var webpack = require('webpack');

// this config can be in webpack.config.js or other file with constants
var API_URL = {
  production: JSON.stringify('http://virkr.dk'),
  development: JSON.stringify('http://localhost:8080')
}

// check environment mode
var environment = process.env.NODE_ENV === 'production' ? 'production' : 'development';

var HTMLWebpackPluginConfig = new HTMLWebpackPlugin({
  template: __dirname + '/src/index.html',
  filename: 'index.html',
  inject: 'body'
});

module.exports = {
  context: __dirname,

  devtool: debug ? "inline-sourcemap" : 'inline-sourcemap',

  entry: './src/index.js',

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loaders: ["eslint-loader","react-hot-loader/webpack", "babel-loader"],
      },
      {
        test: /\.css$/,
        loader: 'style-loader!css-loader'
      },
      {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: "url-loader?limit=10000&mimetype=application/font-woff"
      },
      {
        test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: "file-loader"
      },
      {
        test: /\.(png|jpg)$/,
        loader: 'url-loader?limit=8192'
      }
    ]
  },

  output: {
    filename: "bundle.js",
    path: __dirname + '/build'
  },

  plugins: [
    HTMLWebpackPluginConfig,
    new webpack.DefinePlugin({
      'API_URL': API_URL[environment]
    })
  ]

}
