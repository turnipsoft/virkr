import React, { Component } from 'react';
import DetaljeLinie from './detaljelinie';

export default class DeltagerVirksomhed extends Component {

  render() {
    const {virksomhed, opdaterCvrNummer} = this.props;

    return(
      <div className="card virksomhedcard" onClick={ () => opdaterCvrNummer(virksomhed.cvrnr) }>
        <div className="card-block resizable-block">
          <DetaljeLinie text="Navn" value={virksomhed.navn} detalje={virksomhed.cvrnr} />
          <DetaljeLinie text="Roller" value={virksomhed.roller} />
        </div>
      </div>
    )

  }

}
