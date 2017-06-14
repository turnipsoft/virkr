import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber, spinner, cvrdata } = this.props;

    if (spinner) {
      return (
        <div className="virksomhed-spinner">

          <span className="fa fa-spinner fa-spin fa-3x fa-fw"></span>
          <span className="sr-only">Loading...</span>
        </div>
      )
    }

    if (cvrdata !== null || regnskaber !== null) {
      return (
        <div className="virksomhedsdata">
          {this._renderVirksomhedsInfo(cvrdata)}
          {this._renderRegnskaber(regnskaber)}
          {this._renderCvrData(cvrdata)}
          <br />

        </div>
      )
    } else {
      return null
    }
  }

  _renderVirksomhedsInfo(cvrdata) {
    return (cvrdata !== null) ? <VirksomhedsInfo data={cvrdata} /> : null
  }

  _renderCvrData(cvrdata) {
    return (cvrdata !== null) ? <VirksomhedsDetaljer cvrdata={cvrdata} /> : null
  }

  _renderRegnskaber(regnskaber) {
    if (regnskaber === null) {
      return null;
    }

    if (regnskaber.length > 0) {
      return (
        <div>
          <Graf regnskaber={regnskaber} />
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
          Der er ikke registreret nogen digitale regnskaber for denne virksomhed
      </div>
      )
    }
  }

}

