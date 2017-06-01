import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';
import VirksomhedsInfo from './virksomhedsinfo';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber, spinner } = this.props;

    if (spinner) {
      return (
        <div className="virksomhed-spinner">

          <span className="fa fa-spinner fa-spin fa-3x fa-fw"></span>
          <span className="sr-only">Loading...</span>
        </div>
      )
    }

    if (regnskaber.some(() => true)) {
      // seneste virksomhedsdata
      const { virksomhedsdata } = regnskaber.slice(-1)[0];

      return (
        <div className="virksomhedsdata">
          <VirksomhedsInfo data={virksomhedsdata}/>

          {(regnskaber.length > 0) ? <Graf regnskaber={regnskaber} /> : null}
          <br />
          <br />
          <h5>Regnskaber</h5>
          {regnskaber.slice().reverse().map((regnskab) => {
            return <Regnskabstal key={regnskab.id} regnskab={regnskab} />
          })}
        </div>
      );
    } else {
      return (
        <div className="alert alert-warning">
          Ingen regnskaber med det CVR nummer!
        </div>
      )
    }
  }

}
