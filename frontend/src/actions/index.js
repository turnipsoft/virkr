import api from '../utils/apihelper';
import { push } from 'react-router-redux';

// Soegning
function henterSoegeresultat(text) {
  return {
    type: 'SEARCH',
    text
  }
}

function soegeresultat(soegetext, soegeresultat) {
  return {
    type: 'SEARCH_RESULT',
    soegetext,
    soegeresultat
  }
}

export function soeg(text) {
  return dispatch => {
    dispatch(henterSoegeresultat(text));
    dispatch(push(`/soegeresultat/${text}`));
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

export function visDeltager(enhedsnummer, navigate) {
  return dispatch => {
    dispatch(henterDeltager(enhedsnummer))
    // henter graf i baggrund så den bliver cachet, uden promise da vi er ligeglad med resultatet p.t.
    api.hentDeltagerGraf(enhedsnummer);
    if (navigate) {
      dispatch(push(`/deltager/${enhedsnummer}`));
    }
    return api.hentDeltager(enhedsnummer)
      .then(data => dispatch(deltagerResultat(enhedsnummer, data)))
  }
}

// Virksomhed
function henterVirksomhed(cvrnummer) {
  return {
    type: 'GET_VIRKSOMHED',
    cvrnummer
  }
}

function virksomhedResultat(cvrnummer, cvrdata, regnskaber) {
  return {
    type: 'VIRKSOMHED_RESULT',
    cvrnummer,
    cvrdata,
    regnskaber
  }
}

function virksomhedError(cvrnummer, error) {
  return {
    type: 'VIRKSOMHED_ERROR',
    cvrnummer,
    error
  }
}

function ejerGrafError(cvrnummer, error) {
  return {
    type: 'EJERGRAF_ERROR',
    cvrnummer,
    error
  }
}

export function visVirksomhed(cvrnummer, navigate) {
  return dispatch => {
    dispatch(henterVirksomhed(cvrnummer));
    // henter ejergraf og virksomhedsgraf i baggrund, men er ligeglad med promiset.. TODO, gem graferne så den kan bruges senere
    api.hentEjerGraf(cvrnummer);
    api.hentVirksomhedGraf(cvrnummer);
    if (navigate) {
      dispatch(push(`/virksomhed/${cvrnummer}`));
    }
    return api.hentVirksomhedsdata(cvrnummer)
      .then(data => {
        api.hentNoegletal(cvrnummer)
          .then(noegletal=> dispatch(virksomhedResultat(cvrnummer, data, noegletal)))
          .catch(error => dispatch(virksomhedError(cvrnummer, error)))
      })
      .catch(error => dispatch(virksomhedError(cvrnummer, error)))
  }
}

// DeltagerGraf
function henterDeltagerGraf(enhedsnummer) {
  return {
    type: 'GET_DELTAGER_GRAF',
    enhedsnummer
  }
}

function deltagerGrafResultat(enhedsnummer, deltagerGraf) {
  return {
    type: 'DELTAGER_GRAF_RESULT',
    enhedsnummer,
    deltagerGraf
  }
}

export function visDeltagerGraf(enhedsnummer, navigate) {
  return dispatch => {
    dispatch(henterDeltagerGraf(enhedsnummer))
    if (navigate) {
      dispatch(push(`/deltagergraf/${enhedsnummer}`));
    }
    return api.hentDeltagerGraf(enhedsnummer)
      .then(data => dispatch(deltagerGrafResultat(enhedsnummer, data)))
  }
}

// EjerGraf
function henterEjerGraf(cvrnummer) {
  return {
    type: 'GET_EJER_GRAF',
    cvrnummer
  }
}

function ejerGrafResultat(cvrnummer, ejerGraf, deltagerGraf) {
  return {
    type: 'EJER_GRAF_RESULT',
    cvrnummer,
    ejerGraf,
    deltagerGraf
  }
}

export function visEjerGraf(cvrnummer, navigate) {
  return dispatch => {
    dispatch(henterEjerGraf(cvrnummer))
    if (navigate) {
      dispatch(push(`/ejergraf/${cvrnummer}`));
    }

    return api.hentEjerGraf(cvrnummer)
      .then(data => {
        api.hentVirksomhedGraf(cvrnummer)
          .then(deltagergraf=> dispatch(ejerGrafResultat(cvrnummer, data, deltagergraf)))
          .catch(error => dispatch(ejerGrafError(cvrnummer, error)))
      })
      .catch(error => dispatch(ejerGrafError(cvrnummer, error)))
  }
}
