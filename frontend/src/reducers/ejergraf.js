const ejerGraf = (state = {}, action) => {
  switch (action.type) {
    case 'GET_EJER_GRAF':
      return {
        isFetching: true,
        cvrnummer: action.cvrnummer
      }

    case 'EJER_GRAF_RESULT':
      return {
        isFetching: false,
        ejerGraf: action.ejerGraf,
        deltagerGraf: action.deltagerGraf
      }
    case 'EJER_GRAF_ERROR':
      return {
        hasError: true,
        error: action.error,
        cvrnummer: action.cvrnummer
      }
    default:
      return state
  }
}

export default ejerGraf;
