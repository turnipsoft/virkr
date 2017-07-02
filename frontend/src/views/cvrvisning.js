import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';
import Ejere from './ejere'

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber, cvrdata } = this.props;

    return (
      <div className="top-margin">
        {this._renderVirksomhedsInfo(cvrdata)}
        {this._renderRegnskaber(regnskaber)}
        {this._renderEjere(cvrdata)}
        {this._renderCvrData(cvrdata)}
        <br />
      </div>
    )
  }

  _renderVirksomhedsInfo(cvrdata) {
    return (cvrdata !== null) ? <VirksomhedsInfo data={cvrdata} /> : null
  }

  _renderEjere(cvrdata) {
    return (cvrdata!==null && cvrdata.ejere!==null) ? <Ejere cvrdata={cvrdata} opdaterCvrNummer={this.props.opdaterCvrNummer} /> : null
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

