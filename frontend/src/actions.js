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


