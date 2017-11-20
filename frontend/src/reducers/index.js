import { combineReducers } from 'redux'
import soegning from './soegning'
import virksomhed from './virksomhed'
import deltager from './deltager'

const virkrReducers = combineReducers({
  soegning,
  virksomhed,
  deltager
})

export default virkrReducers
