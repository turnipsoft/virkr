import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class EjerGrafStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  getEjerGraf(cvr) {
    api.hentEjerGraf(cvr)
      .then((data) => actions.ejerGrafResponse(data));
  }

  ejerGrafResponse(response) {
    this.state = response;
    this.emit("change");
  }

  clearData() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'GET_EJERGRAF':
        this.getEjerGraf(action.cvr);
        break;
      case 'EJERGRAF':
        this.ejerGrafResponse(action.response);
        break;
    }
  }

}

const store = new EjerGrafStore();
dispatcher.register(store.handleActions.bind(store));

export default store;
