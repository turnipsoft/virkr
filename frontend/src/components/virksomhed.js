import React, { Component } from 'react';
import CvrSoegebox from './cvrsoegebox';
import CvrVisning from './cvrvisning';

import APIHelper from '../utils/apihelper.js';

export default class Virksomhed extends Component {

  constructor() {
    super();

    this._opdaterCvrNummer = this._opdaterCvrNummer.bind(this);

    this.state = {
      henterNoegletal: false,
      cvrnummer: '',
      regnskaber: [],
      cvrdata: null
    };
  }

  _opdaterCvrNummer(cvrnr) {
    let _regnskabsdata;

    this.setState({ henterNoegletal: true }, () => {
      APIHelper.hentNoegletal(cvrnr)
        .then((data) => {
          _regnskabsdata = data.regnskabsdata
          return APIHelper.hentVirksomhedsdata(cvrnr);
        })
        .then((_cvrdata) => {
          this.setState({ henterNoegletal: false, cvrnummer: cvrnr, regnskaber: _regnskabsdata, cvrdata: _cvrdata })
        })
        .catch((err) => {
          alert(err)
        })
    })
  }

  render() {
    const { cvrnummer, regnskaber, henterNoegletal, cvrdata } = this.state;

    return (<div className="virksomhed">
      <div className="row">
        <div className="col">
          <h1 id="virkr-header"><a href="/" id="virk-header-a">Virkr</a></h1>
          <p id="virkr-tagline">Nøgletal om virksomheder</p>
        </div>
      </div>
      <div className="row">
        <div className="col">
          <CvrSoegebox opdaterCvr={this._opdaterCvrNummer} />
        </div>
      </div>
      <div className="row">
        <div className="col">
          {(cvrnummer !== '') ? <CvrVisning cvrnummer={cvrnummer} regnskaber={regnskaber} spinner={henterNoegletal}
            cvrdata={cvrdata} /> : null}
        </div>
      </div>
    </div>);
  }

}
