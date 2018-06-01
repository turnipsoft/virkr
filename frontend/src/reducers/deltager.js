const deltager = (state = {}, action) => {
  switch (action.type) {
    case 'GET_DELTAGER':
      return {
        isFetching: true,
        enhedsnummer: action.enhedsnummer
      }

    case 'DELTAGER_RESULT':
      return {
        isFetching: false,
        deltager: action.deltager
      }
    case 'DELTAGER_RESULT_ERROR':
      return {
        isFetching: false,
        error: action.error,
        enhedsnummer: action.enhedsnummer
      }
    default:
      return state
  }
}

export default deltager
