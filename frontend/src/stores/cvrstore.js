import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class CVRStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  getCvrData(cvr) {
    api.hentVirksomhedsdata(cvr)
      .then((data) => actions.cvrResponse(data));
  }

  cvrData(response) {
    this.state = response;
    this.emit("change");
  }

  clearData() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'GET_CVR':
        this.getCvrData(action.cvr);
        break;
      case 'CVR':
        this.cvrData(action.response);
        break;
      case 'SEARCH':
        this.clearData();
        break;
    }
  }

}

const store = new CVRStore;
dispatcher.register(store.handleActions.bind(store));

export default store;
