const soegning = (state = {}, action) => {
  switch (action.type) {
    case 'SEARCH':
      return {
          isFetching: true,
          soegetext: action.text
        }

    case 'SEARCH_RESULT':
      return {
        isFetching: false,
        soegeresultat: action.soegeresultat,
        soegetext: action.soegetext
      }
    case 'SEARCH_RESULT_ERROR':
      return {
        isFetching: false,
        error: action.error,
        soegetext: action.soegetext
      }
    default:
      return state
  }
}

export default soegning
