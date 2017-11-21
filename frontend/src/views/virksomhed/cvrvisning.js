import React, { Component } from 'react';
import Regnskabstal from '../regnskabstal';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';
import Ejere from './ejere';
import Graf from './graf';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber, cvrdata } = this.props;

    return (
      <div className="top-margin">
        {this._renderVirksomhedsInfo(cvrdata)}
        {this._renderRegnskaber(regnskaber.regnskabsdata)}
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
    return (cvrdata!==null && cvrdata.ejere!==null) ? <Ejere cvrdata={cvrdata} opdaterCvrNummer={this.props.opdaterCvrNummer}
                                                             opdaterDeltager={this.props.opdaterDeltager}
                                                             visEjerGraf={this.props.visEjerGraf}/> : null
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
          <div className="row">
            <div className="col-12 section-header">
              <span className="fa fa-file-pdf-o" /> &nbsp; Regnskaber
            </div>
          </div>
          <br/>
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

