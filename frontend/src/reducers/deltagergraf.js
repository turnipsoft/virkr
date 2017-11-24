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
    default:
      return state
  }
}

export default deltagerGraf;
