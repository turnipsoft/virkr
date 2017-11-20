import api from '../utils/apihelper';


// Soegning
function henterSoegeresultat(text) {
  return {
    type: 'SEARCH',
    text
  }
}

function soegeresultat(soegning, soegeresultat) {
  return {
    type: 'SEARCH_RESULT',
    soegning,
    soegeresultat
  }
}

export function soeg(text) {
  return dispatch => {
    dispatch(henterSoegeresultat(text))
    return api.soegVirkr(text)
      .then(data => dispatch(soegeresultat(text, data)))
  }
}

// Deltager
function henterDeltager(enhedsnummer) {
  return {
    type: 'GET_DELTAGER',
    enhedsnummer
  }
}

function deltagerResultat(enhedsnummer, deltager) {
  return {
    type: 'DELTAGER_RESULT',
    enhedsnummer,
    deltager
  }
}

export function visDeltager(enhedsnummer) {
  return dispatch => {
    dispatch(henterDeltager(enhedsnummer))
    return api.hentDeltager(enhedsnummer)
      .then(data => dispatch(deltagerResultat(enhedsnummer, data)))
  }
}
