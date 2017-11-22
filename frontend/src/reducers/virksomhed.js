const virksomhed = (state = {}, action) => {
  switch (action.type) {
    case 'GET_VIRKSOMHED':
      return {
        isFetching: true,
        cvrnummer: action.cvrnummer
      }

    case 'VIRKSOMHED_RESULT':
      return {
        isFetching: false,
        cvrdata: action.cvrdata,
        regnskaber: action.regnskaber
      }
    case 'VIRKSOMHED_ERROR':
      return {
        hasError: true,
        error: action.error,
        cvrnummer: action.cvrnummer
      }
    default:
      return state
  }
}

export default virksomhed
