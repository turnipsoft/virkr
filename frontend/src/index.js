import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import { HashRouter } from 'react-router-dom';
import thunkMiddleware from 'redux-thunk';
import Main from './containers/main';
import Header from './views/common/header';
import App from './containers/app';
import './images/regnskab-baggrund.png'
import './main.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'font-awesome-webpack'
import virkrReducers from './reducers';

const store = createStore(virkrReducers, {}, applyMiddleware(thunkMiddleware));

ReactDOM.render(
  <Provider store={store}>
    <HashRouter>
      <div>
        <Header />
        <Main />
      </div>
    </HashRouter>
  </Provider>,
  <App />,
  document.getElementById('react')
);
