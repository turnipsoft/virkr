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
    default:
      return state
  }
}

export default soegning
