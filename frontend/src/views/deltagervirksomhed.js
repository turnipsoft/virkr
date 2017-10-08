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

          {virksomhed.ejerandeliprocent?
            <div>
              <br/>
              <h6>Reelle ejerandele</h6>
              <DetaljeLinie text="Ejerandel" value={virksomhed.ejerandeliprocent} />
              <DetaljeLinie text="Stemmeret" value={virksomhed.stemmeretiprocent} />

            </div> : null
          }
        </div>
      </div>
    )

  }

}
