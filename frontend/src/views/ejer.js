import React, { Component } from 'react';
import DetaljeLinie from './detaljelinie';

export default class Ejer extends React.Component {

  render() {
    const {ejer, opdaterCvrNummer} = this.props;

    return(
      <div className="card ejercard" onClick={ () => opdaterCvrNummer(this.props.ejer.forretningsnoegle) }>
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

}
