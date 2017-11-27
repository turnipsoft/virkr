import React, { Component } from 'react';
import DeltagerInfo from './deltagerinfo';
import DeltagerVirsomheder from './deltagervirksomheder';

export default class DeltagerVisning extends Component {

  render() {
    const {deltager} = this.props;

    return (
      <div className="top-margin">
        {this._renderDeltagerInfo(deltager)}
        <br/>
        {this._renderAssocieredeVirksomheder(deltager)}
      </div>
    )
  }

  _renderDeltagerInfo(deltager) {
    return (deltager !== null) ? <DeltagerInfo deltager={deltager} /> : null
  }

  _renderAssocieredeVirksomheder(deltager) {
    return (
      <DeltagerVirsomheder deltager={deltager} onVirksomhedClick={this.props.onVirksomhedClick} visDeltagerGraf={this.props.visDeltagerGraf} />
    )
  }
}
