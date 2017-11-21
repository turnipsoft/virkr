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
    default:
      return state
  }
}

export default virksomhed
