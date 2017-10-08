import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';
import api from '../utils/apihelper';
import * as actions from '../actions';

class SearchStore extends EventEmitter {

  constructor() {
    super();
    this.state = null;
  }

  getState() {
    return this.state;
  }

  search(term) {
    api.soegVirkr(term).then((data) => actions.searchResponse(data));
  }

  searchData(response) {
    this.state = response;
    this.emit("change");
  }

  clear() {
    this.state = null;
    this.emit("change");
  }

  handleActions(action) {
    switch (action.type) {
      case 'SEARCH':
        this.search(action.term);
        break;
      case 'SEARCH_DATA':
        this.searchData(action.response);
        break;
      case 'GET_CVR':
        this.clear();
        break;
    }
  }

}

const store = new SearchStore;
dispatcher.register(store.handleActions.bind(store));

// Nice for realtime hacking...
window.searchStore = store;

export default store;
