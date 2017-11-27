const path = require('path');

const debug = process.env.NODE_ENV !== "production";
const HTMLWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

const apiHost = debug ? 'slamhost' : 'virkr.dk';

module.exports = {
  devtool: debug ? "inline-sourcemap" : null,

  entry: './src/index.js',

  output: {
    filename: "bundle.js",
    path: path.join(__dirname, 'build')
  },

  plugins: [
    new HTMLWebpackPlugin(
      {
        inject: true,
        template: './src/index.html'
      }
    ),
    new webpack.DefinePlugin({
      __APIHOST__: apiHost
    })
  ],

  resolve: {
    extensions: ['.js', '.jsx']
  },

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loaders: ["eslint-loader", "react-hot-loader", "babel-loader"],
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


}
