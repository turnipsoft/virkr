import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class DeltagerStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  getDeltagerData(enhedsnummer) {
    api.hentDeltager(enhedsnummer)
      .then((data) => actions.deltagerResponse(data));
  }

  deltagerData(response) {
    this.state = response;
    this.emit("change");
  }

  clearData() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'GET_DELTAGER':
        this.getDeltagerData(action.enhedsnummer);
        break;
      case 'DELTAGER':
        this.deltagerData(action.response);
        break;
      case 'SEARCH':
        this.clearData();
        break;
    }
  }

}

const store = new DeltagerStore();
dispatcher.register(store.handleActions.bind(store));

export default store;
