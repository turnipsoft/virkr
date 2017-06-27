import React, { Component } from 'react';
import CvrSoegebox from '../views/cvrsoegebox';
import CvrVisning from '../views/cvrvisning';
import Soegeresultat from '../views/soegeresultat';

import cvrstore from '../stores/cvrstore';
import searchstore from '../stores/searchstore';
import noegletalstore from '../stores/noegletalstore';
import spinnerstore from '../stores/spinnerstore';
import * as actions from '../actions';

export default class Virksomhed extends Component {

  constructor() {
    super();

    this._opdaterCvrNummer = this._opdaterCvrNummer.bind(this);
    this._visSoegeresultat = this._visSoegeresultat.bind(this);

    this.state = {
      visSpinner: spinnerstore.getState(),
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

    spinnerstore.on('change', () => {
      this.setState({ visSpinner: spinnerstore.getState() })
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

    if (visSpinner) {
      return (
        <div className="virksomhed-spinner">
          <span className="fa fa-spinner fa-spin fa-3x fa-fw"></span>
          <span className="sr-only">Loading...</span>
        </div>
      )
    }

    return (
      <div className="virksomhed">

        <div className="hidden-sm-up virkr-header">
          <div className="row">
            <div className="col">
              <p>Virkr - Nøgletal om virksomheder</p>
            </div>
          </div>
        </div>

        <div className="hidden-xs-down virkr-header">
          <div className="row">
            <div className="col">
              <h1><a href="/">Virkr</a></h1>
              <p>Nøgletal om virksomheder</p>
            </div>
          </div>
        </div>

        <div className="row">
          <div className="col col-sm-6 offset-sm-3 justify-content-center top-margin">
            <CvrSoegebox opdaterCvr={this._visSoegeresultat} />
          </div>
        </div>

        {this._renderCvrVisning(cvrnummer, cvrdata, regnskaber)}
        {this._renderSoegeresultat(soegeresultat)}

      </div>);
  }

  _renderSoegeresultat(soegeresultat) {
    if (soegeresultat === null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <Soegeresultat soegeresultat={soegeresultat} opdaterCvrNummer={this._opdaterCvrNummer} />
        </div>
      </div>
    );
  }

  _renderCvrVisning(cvrnummer, cvrdata, regnskaber) {
    if (cvrdata === null && regnskaber === null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <CvrVisning cvrnummer={cvrnummer} regnskaber={regnskaber} cvrdata={cvrdata} />
        </div>
      </div>
    )
  }

}
