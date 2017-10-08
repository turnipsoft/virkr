import dispatcher from './dispatcher';

export function search(term) {
  dispatcher.dispatch({type: 'SEARCH', term });
}

export function searchResponse(response) {
  dispatcher.dispatch({type: 'SEARCH_DATA', response });
}

export function getVirksomhed(cvr) {
  dispatcher.dispatch({ type: 'GET_CVR', cvr });
  dispatcher.dispatch({ type: 'GET_NOEGLETAL', cvr });
}

export function cvrResponse(response) {
  dispatcher.dispatch({type: 'CVR', response });
}

export function noegletalResponse(response) {
  dispatcher.dispatch({type: 'NOEGLETAL', response });
}

export function getEjerGraf(cvr) {
  dispatcher.dispatch({ type: 'GET_EJERGRAF', cvr });
}

export function ejerGrafResponse(response) {
  dispatcher.dispatch({ type: 'EJERGRAF', response });
}

export function getDeltager(enhedsnummer) {
  dispatcher.dispatch( {type: 'GET_DELTAGER', enhedsnummer});
}

export function deltagerResponse(response) {
  dispatcher.dispatch( {type: 'DELTAGER', response});
}
