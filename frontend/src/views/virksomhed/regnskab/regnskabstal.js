import React, { Component } from 'react';
import Noegletal from './noegletal';

export default class Regnskabstal extends Component {

  render() {
    const brutto = this.props.regnskab.resultatopgoerelse.bruttoresultatTal
    const aaretsresultat = this.props.regnskab.resultatopgoerelse.aaretsresultatTal
    const passiver = this.props.regnskab.balance.passiver

    return (
      <div className="card regnskabscard hidden-sm-up">
        <div className="card-header">
          <b>{this.props.regnskab.aar}</b> <span className="small">({this.props.regnskab.startdato} - {this.props.regnskab.slutdato})</span>
        </div>

        {/* Vis et sammendrag på xs */}
        <div className="card-block hidden-sm-up">
          <Noegletal noegletal={brutto.bruttofortjeneste} text="Bruttofortjeneste" />
          <br />
          <Noegletal noegletal={passiver.egenkapital} text="Egenkapital" />
          <br />
          <Noegletal noegletal={aaretsresultat.aaretsresultat} text="Årets resultat" />
          <div className="row">
            <div className="col col-12">
              <span className="pull-right">
                <a href={this.props.regnskab.xbrlurl} target="_blank" className="btn btn-primary">XBRL</a>
                &nbsp;
                <a href={this.props.regnskab.pdfurl} target="_blank" className="btn btn-primary ">PDF</a>
              </span>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

