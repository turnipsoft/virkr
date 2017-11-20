import { combineReducers } from 'redux'
import soegning from './soegning'
import virksomhed from './virksomhed'
import deltager from './deltager'
import { routerReducer } from 'react-router-redux'

const virkrReducers = combineReducers({
  soegning,
  virksomhed,
  deltager,
  router: routerReducer
})

export default virkrReducers
