import { combineReducers } from 'redux';
import soegning from './soegning';
import virksomhed from './virksomhed';
import deltager from './deltager';
import deltagerGraf from './deltagergraf';
import ejerGraf from './ejergraf';

import { routerReducer } from 'react-router-redux';

const virkrReducers = combineReducers({
  router: routerReducer,
  soegning,
  virksomhed,
  deltager,
  deltagerGraf,
  ejerGraf
})

export default virkrReducers
