import React, { Component } from 'react';
import Ejer from './ejer';

export default class Ejere extends React.Component {

  render() {

    console.log(this.props);

    const {cvrdata, visEjerGraf, visVirksomhed, visDeltager} = this.props;

    const ejere = cvrdata.aktuelleEjere;
    const historiskeEjere = cvrdata.historiskeEjere;

    if (ejere && ejere.length>0) {
      return(
        <div>
          <div className="ejere">
            <div className="row">
              <div className="col-12 section-header">
                <span className="btn-link fa fa-sitemap" onClick={ () => visEjerGraf(cvrdata.cvrNummer, true)} /> &nbsp; Ejere &nbsp;
                <span className="btn-link" onClick={ () => visEjerGraf(cvrdata.cvrNummer, true)} >(Se komplet ejergraf)</span>
              </div>
            </div>
            <br/>
            {ejere.map((ejer) => {
              return (<Ejer key={ejer.enhedsnummer} ejer={ejer} visVirksomhed={visVirksomhed} visDeltager={visDeltager} />)
            })}
          </div>
          {this._renderHitoriskeEjere(historiskeEjere)}
        </div>

      )
    }

    return null;
  }

  _renderHitoriskeEjere(ejere) {
    if (ejere && ejere.length>0) {
      const { visVirksomhed, visDeltager} = this.props;

      return (
        <div className="ejere">
          <div className="row">
            <div className="col-12 section-header">
              Historiske Ejere
            </div>
          </div>
          <br/>
          {ejere.map((ejer) => {
            return (<Ejer key={ejer.enhedsnummer} ejer={ejer} visVirksomhed={visVirksomhed} visDeltager={visDeltager} />)
          })}
        </div>

      )
    }
  }


}
