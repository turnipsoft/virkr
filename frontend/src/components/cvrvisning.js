import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber } = this.props;

    return (
      <div className="virksomhedsdata">
        {(regnskaber.length > 0) ? <Graf regnskaber={regnskaber} /> : null}
        <br />
        <br />
        {regnskaber.reverse().map((regnskab) => {
          return <Regnskabstal key={regnskab.id} regnskab={regnskab} />
        })}
      </div>);
  }

}
