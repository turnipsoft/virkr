import React, { Component } from 'react';
import DetaljeLinie from './detaljelinie';

export default class EjerVisning extends React.Component {

  render() {
    const {ejer, opdaterCvrNummer, opdaterDeltager} = this.props;

    return(

        <div className="card-block resizable-block">
          {ejer.ejertype==='PERSON'?
            (<div>
              <DetaljeLinie text="Navn" value={ejer.navn} detalje={ejer.ejertype} link={opdaterDeltager} linkKey={ejer.enhedsnummer} />
            </div>) :
            (<div>
              <DetaljeLinie text="Navn" value={ejer.navn} detalje={ejer.ejertype} />
              <DetaljeLinie text="CVR-Nummer" value={ejer.forretningsnoegle} link={opdaterCvrNummer} linkKey={ejer.forretningsnoegle} />

            </div>)
          }
          <DetaljeLinie text="Adresse" value={ejer.adresse} />
          <br/>
          <h5>Ejerandele i virksomheder ( direkte og indirekte )</h5>
          <hr/>
          {ejer.reelleEjerandele.map((re) => {
            return (
              <div>
                <DetaljeLinie text="Virksomhed" value={re.virksomhedsnavn} detalje={re.cvrnummer} key={re.virksomhedsnavn} link={opdaterCvrNummer} linkKey={re.cvrnummer}  />
                <DetaljeLinie text="Ejerandel" value={re.andelInterval} key={re.cvrnummer} />
                <hr/>
              </div>
            )
          })}
        </div>

    )

  }

}
