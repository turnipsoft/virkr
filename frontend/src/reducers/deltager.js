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
    default:
      return state
  }
}

export default deltager
