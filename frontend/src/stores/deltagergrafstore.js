import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class DeltagerGrafStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  getDeltagerGraf(enhedsnummer) {
    api.hentDeltagerGraf(enhedsnummer)
      .then((data) => actions.deltagerGrafResponse(data));
  }

  deltagerGrafResponse(response) {
    this.state = response;
    this.emit("change");
  }

  clearData() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'GET_DELTAGERGRAF':
        this.getDeltagerGraf(action.enhedsnummer);
        break;
      case 'DELTAGERGRAF':
        this.deltagerGrafResponse(action.response);
        break;
    }
  }

}

const store = new DeltagerGrafStore();
dispatcher.register(store.handleActions.bind(store));

export default store;
