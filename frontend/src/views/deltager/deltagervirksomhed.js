import React, { Component } from 'react';
import DetaljeLinie from '../common/detaljelinie';

export default class DeltagerVirksomhed extends Component {

  render() {
    const {virksomhed, onClick} = this.props;

    return(
      <div className="card virksomhedcard" onClick={ () => onClick(virksomhed.cvrnr, true) }>
        <div className="card-block resizable-block">
          <DetaljeLinie text="Navn" value={virksomhed.navn} detalje={virksomhed.cvrnr} />
          <DetaljeLinie text="Virksomhedsstatus" value={virksomhed.status} />
          <DetaljeLinie text="Roller" value={virksomhed.roller} />

          {virksomhed.ejerandeliprocent?
            <div>
              <br/>
              <h6>Legale ejerandele</h6>
              <DetaljeLinie text="Ejerandel" value={virksomhed.ejerandeliprocent} />
              <DetaljeLinie text="Stemmeret" value={virksomhed.stemmeretiprocent} />

            </div> : null
          }
        </div>
      </div>
    )

  }

}
