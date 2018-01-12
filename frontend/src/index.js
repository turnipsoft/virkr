import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import thunkMiddleware from 'redux-thunk';
import logger from 'redux-logger'
import { routerMiddleware, ConnectedRouter } from 'react-router-redux'
import createHistory from 'history/createHashHistory'
//import createHistory from 'history/createBrowserHistory'
import Main from './containers/main';
import './images/regnskab-baggrund.png'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'font-awesome-webpack'
import './main.css';
import virkrReducers from './reducers';

export const history = createHistory();

const middleware = [
  thunkMiddleware,
  routerMiddleware(history),
  logger
]

const store = createStore(virkrReducers, {}, applyMiddleware(...middleware));

ReactDOM.render(
  <Provider store={store}>
    <ConnectedRouter history={history} >
      <div>
        <Main />
      </div>
    </ConnectedRouter>
  </Provider>,
  document.getElementById('react')
);
