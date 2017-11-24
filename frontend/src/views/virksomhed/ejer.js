import React, { Component } from 'react';
import DetaljeLinie from '../common/detaljelinie';

export default class Ejer extends React.Component {


  render() {
    const {ejer} = this.props;

    return(
      <div className="card ejercard" onClick={ () => this._visEjer(this.props.ejer) }>
        <div className="card-block resizable-block">
          <DetaljeLinie text="Navn" value={ejer.navn} detalje={ejer.ejertype} />
          <DetaljeLinie text="CVR-Nummer" value={ejer.forretningsnoegle} />
          <DetaljeLinie text="Adresse" value={ejer.adresse} />
          <DetaljeLinie text="Ejerandel" value={ejer.andelInterval} />
          <DetaljeLinie text="Stemmeandel" value={ejer.stemmeprocentInterval} />
        </div>
      </div>
    )

  }

  _visEjer(ejer) {
    if (ejer.ejertype==="PERSON") {
      this.props.visDeltager(ejer.enhedsnummer, true);
    } else {
      this.props.visVirksomhed(ejer.forretningsnoegle, true);
    }
  }

}
