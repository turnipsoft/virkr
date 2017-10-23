import React, { Component } from 'react';
import CvrSoegebox from '../views/cvrsoegebox';
import CvrVisning from '../views/cvrvisning';
import DeltagerVisning from '../views/deltager';
import Soegeresultat from '../views/soegeresultat';
import EjerGraf from '../views/ejergraf'
import DeltagerGraf from '../views/deltagergraf';

import cvrstore from '../stores/cvrstore';
import searchstore from '../stores/searchstore';
import noegletalstore from '../stores/noegletalstore';
import spinnerstore from '../stores/spinnerstore';
import ejergrafstore from '../stores/ejergrafstore';
import deltagerstore from '../stores/deltagerstore';
import deltagergrafstore from '../stores/deltagergrafstore';
import * as actions from '../actions';

export default class Virksomhed extends Component {

  constructor() {
    super();

    this._opdaterCvrNummer = this._opdaterCvrNummer.bind(this);
    this._visSoegeresultat = this._visSoegeresultat.bind(this);
    this._visEjerGraf = this._visEjerGraf.bind(this);
    this._visDeltagerGraf = this._visDeltagerGraf.bind(this);
    this._opdaterDeltager = this._opdaterDeltager.bind(this);

    this.state = {
      visSpinner: spinnerstore.getState(),
      cvrnummer: '',
      regnskaber: noegletalstore.getState(),
      cvrdata: cvrstore.getState(),
      deltager: deltagerstore.getState(),
      soegeresultat: searchstore.getState(),
      ejergraf: ejergrafstore.getState(),
      deltagergraf : deltagergrafstore.getState()
    };
  }

  reset() {
    this.setState({soegeresultat:null, regnskaber: null, cvrdata: null, ejergraf:null, deltager:null, deltagergraf:null});
  }

  componentDidMount() {
    searchstore.on('change', () => {
      this.setState({ soegeresultat: searchstore.getState(), ejergraf: null, deltagergraf:null })
    });

    noegletalstore.on('change', () => {
      this.setState({ regnskaber: noegletalstore.getState(), ejergraf: null, deltagergraf:null })
    });

    cvrstore.on('change', () => {
      this.setState({ cvrdata: cvrstore.getState(), ejergraf: null , deltager: null, deltagergraf:null})
    });

    spinnerstore.on('change', () => {
      this.setState({ visSpinner: spinnerstore.getState() })
    });

    ejergrafstore.on('change', () => {
      this.reset();
      this.setState( { ejergraf: ejergrafstore.getState() })
    })

    deltagerstore.on('change', () => {
      this.reset();
      this.setState( {deltager: deltagerstore.getState() });
    })

    deltagergrafstore.on('change', () => {
      this.reset();
      this.setState( {deltagergraf: deltagergrafstore.getState()});
    })

  }

  _visSoegeresultat(soegning) {
    if (soegning === null) {
      return
    }
    actions.search(soegning);
  }

  _opdaterCvrNummer(cvrnr) {
    if (!cvrnr || cvrnr.length!=8) {
      return
    }
    actions.getVirksomhed(cvrnr);
  }

  _opdaterDeltager(enhedsnummer) {
    if (!enhedsnummer || enhedsnummer.length!=10) {
      return
    }

    actions.getDeltager(enhedsnummer)
  }

  _visEjerGraf(cvrnr) {
    if (!cvrnr || cvrnr.length!=8) {
      return
    }
    actions.getEjerGraf(cvrnr);
  }

  _visDeltagerGraf(enhedsnummer) {
    if (!enhedsnummer) {
      return
    }

    actions.getDeltagerGraf(enhedsnummer)
  }

  render() {
    const { cvrnummer, regnskaber, visSpinner, cvrdata, soegeresultat, ejergraf, deltager, deltagergraf } = this.state;

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
        {this._renderDeltagerVisning(deltager)}
        {this._renderSoegeresultat(soegeresultat)}
        {this._renderEjerGraf(cvrnummer, ejergraf)}
        {this._renderDeltagerGraf(deltagergraf)}

      </div>);
  }

  _renderSoegeresultat(soegeresultat) {
    if (soegeresultat === null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <Soegeresultat soegeresultat={soegeresultat} opdaterCvrNummer={this._opdaterCvrNummer} opdaterDeltager={this._opdaterDeltager} />
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
          <CvrVisning cvrnummer={cvrnummer} regnskaber={regnskaber} cvrdata={cvrdata}
                      opdaterCvrNummer={this._opdaterCvrNummer} visEjerGraf={this._visEjerGraf}
                      opdaterDeltager={this._opdaterDeltager}/>
        </div>
      </div>
    )
  }

  _renderDeltagerVisning(deltager) {
    if (deltager===null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <DeltagerVisning deltager={deltager} opdaterCvrNummer={this._opdaterCvrNummer} visDeltagerGraf={this._visDeltagerGraf} />
        </div>
      </div>);
  }


  _renderEjerGraf(cvrnummer, ejergraf) {
    if (ejergraf==null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <EjerGraf cvrnummer={cvrnummer} ejergraf={ejergraf} opdaterCvrNummer={this._opdaterCvrNummer}
                    opdaterDeltager={this._opdaterDeltager} />
        </div>
      </div>
    )
  }

  _renderDeltagerGraf(deltagergraf) {
    if (deltagergraf==null) {
      return null;
    }

    return (
      <div className="row">
        <div className="col">
          <DeltagerGraf deltagerGraf={deltagergraf} opdaterCvrNummer={this._opdaterCvrNummer}
                    opdaterDeltager={this._opdaterDeltager} />
        </div>
      </div>
    )
  }

}
