import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class NoegletalStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  getNoegletal(cvr) {
    api.hentNoegletal(cvr)
      .then((data) => actions.noegletalResponse(data));
  }

  noegletal(response) {
    this.state = response.regnskabsdata;
    this.emit("change");
  }

  clear() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'GET_NOEGLETAL':
        this.getNoegletal(action.cvr);
        break;
      case 'NOEGLETAL':
        this.noegletal(action.response);
        break;
      case 'SEARCH':
        this.clear();
        break;
    }
  }
}

const store = new NoegletalStore;
dispatcher.register(store.handleActions.bind(store));

export default store;
