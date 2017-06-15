import React, { Component } from 'react';
import CvrSoegebox from '../views/cvrsoegebox';
import CvrVisning from '../views/cvrvisning';
import Soegeresultat from '../views/soegeresultat';

import cvrstore from '../stores/cvrstore';
import searchstore from '../stores/searchstore';
import noegletalstore from '../stores/noegletalstore';
import * as actions from '../actions';

export default class Virksomhed extends Component {

  constructor() {
    super();

    this._opdaterCvrNummer = this._opdaterCvrNummer.bind(this);
    this._visSoegeresultat = this._visSoegeresultat.bind(this);

    this.state = {
      visSpinner: false,
      cvrnummer: '',
      regnskaber: noegletalstore.getState(),
      cvrdata: cvrstore.getState(),
      soegeresultat: searchstore.getState()
    };
  }

  componentDidMount() {
    searchstore.on('change', () => {
      this.setState({ soegeresultat: searchstore.getState() })
    });

    noegletalstore.on('change', () => {
      this.setState({ regnskaber: noegletalstore.getState() })
    });

    cvrstore.on('change', () => {
      this.setState({ cvrdata: cvrstore.getState() })
    });
  }

  _visSoegeresultat(soegning) {
    actions.search(soegning);
  }

  _opdaterCvrNummer(cvrnr) {
    actions.getVirksomhed(cvrnr);
  }

  render() {

    const { cvrnummer, regnskaber, visSpinner, cvrdata, soegeresultat } = this.state;

    return (<div className="virksomhed">
      <div className="row">
        <div className="col">
          <h1 id="virkr-header"><a href="/" id="virk-header-a">Virkr</a></h1>
          <p id="virkr-tagline">Nøgletal om virksomheder</p>
        </div>
      </div>
      <div className="row">
        <div className="col">
          <CvrSoegebox opdaterCvr={this._visSoegeresultat} />
        </div>
      </div>
      <div className="row">
        <div className="col">
          <CvrVisning cvrnummer={cvrnummer} regnskaber={regnskaber} spinner={visSpinner} cvrdata={cvrdata}/>
          <Soegeresultat soegeresultat={soegeresultat} opdaterCvrNummer={this._opdaterCvrNummer} />
        </div>
      </div>
    </div>);
  }

}
