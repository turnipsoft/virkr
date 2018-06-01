const deltagerGraf = (state = {}, action) => {
  switch (action.type) {
    case 'GET_DELTAGER_GRAF':
      return {
        isFetching: true,
        enhedsnummer: action.enhedsnummer
      }

    case 'DELTAGER_GRAF_RESULT':
      return {
        isFetching: false,
        deltagerGraf: action.deltagerGraf
      }
    case 'DELTAGER_GRAF_ERROR':
      return {
        isFetching: false,
        error: action.error,
        enhedsnumnner: action.enhedsnummer
      }
    default:
      return state
  }
}

export default deltagerGraf;
