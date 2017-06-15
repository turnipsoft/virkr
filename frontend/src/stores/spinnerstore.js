import { EventEmitter } from 'events';
import dispatcher from '../dispatcher';

class SpinnerStore extends EventEmitter {
  constructor() {
    super();
    this.state = false;
  }

  getState() {
    return this.state;
  }

  _onOff(state) {
    this.state = state;
    this.emit('change')
  }

  handleActions(action) {
    switch (action.type) {
      case 'SEARCH':
      case 'GET_CVR':
      case 'GET_NOEGLETAL':
        this._onOff(true);
        break;
      case 'SEARCH_DATA':
      case 'CVR':
      case 'NOEGLETAL':
        this._onOff(false);
        break;
    }
  }
}

const store = new SpinnerStore;
dispatcher.register(store.handleActions.bind(store));

export default store;
