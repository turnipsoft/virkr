import React, { Component } from 'react';
import DetaljeLinie from './detaljelinie';

export default class EjerVisning extends React.Component {

  render() {
    const {ejer, visVirksomhed, visDeltager, fraGraf} = this.props;

    let ejertype = (ejer.ejertype=='ROD' ? 'VIRKSOMHED' : ejer.ejertype);

    let cvrnummer = ejer.forretningsnoegle?ejer.forretningsnoegle:ejer.cvrnummer;

    if(cvrnummer && cvrnummer.length>8) {
      cvrnummer = null;
    }

    if(ejertype=='VIRKSOMHED' && !cvrnummer) {
      // Så er det en udenlands virksomhed og dem har ikke lyst til at linke til, de har ingen data
      // Angiver også at det er udenlandsk
      ejertype = 'UDENLANDSK';
    }

    let href = null;
    if (fraGraf) {
      href = ejer.ejertype === 'PERSON' ? './#/deltager/' + ejer.enhedsnummer : './#/virksomhed/' + cvrnummer;
    }

    return(
        <div className="card-block resizable-block">
          {ejer.ejertype==='PERSON'?
            (<div>
              <DetaljeLinie text="Navn" value={ejer.navn} detalje={ejer.ejertype} link={visDeltager} linkKey={ejer.enhedsnummer} href={href}/>
            </div>) :
            (<div>
              <DetaljeLinie text="Navn" value={ejer.navn} detalje={ejertype} />
              {ejertype=='VIRKSOMHED' && <DetaljeLinie text="CVR-Nummer" value={cvrnummer} link={visVirksomhed} linkKey={cvrnummer} href={href} />}
            </div>)
          }
          <DetaljeLinie text="Adresse" value={ejer.adresse} />
          <br/>
          {ejer.ejedeVirksomheder? (
            <div>
              {ejer.ejedeVirksomheder.length>0?(<div><h5>Ejerandele i virksomheder</h5><hr/></div>):null}
              {ejer.ejedeVirksomheder.map((re) => {
                return (
                  <div>
                    <DetaljeLinie text="Virksomhed" value={re.virksomhedsnavn} detalje={re.cvrnummer} key={re.virksomhedsnavn} link={visVirksomhed} linkKey={re.cvrnummer} href={href}  />
                    <DetaljeLinie text="Ejerandel" value={re.ejer.andelInterval} key={re.cvrnummer} />
                    {re.ejer && re.ejer.ophoersdato && <DetaljeLinie text="Ophørsdato" value={re.ejer.ophoersdato} key={re.cvrnummer+re.ejer.ophoersdato} />}
                    <hr/>
                  </div>
                )
              })}
            </div>) :
            (
              <div>
                {ejer.reelleEjerandele.length>0?(<div><h5>Ejerandele i virksomheder</h5><hr/></div>):null}
                {ejer.reelleEjerandele.map((re) => {
                  return (
                    <div key={re.cvrnummer}>
                      <DetaljeLinie text="Virksomhed" value={re.virksomhedsnavn} detalje={re.cvrnummer} key={re.virksomhedsnavn} link={visVirksomhed} linkKey={re.cvrnummer} href={href} />
                      <DetaljeLinie text="Ejerandel" value={re.andelInterval} key={re.cvrnummer} />
                      {re.ejer && re.ejer.ophoersdato && <DetaljeLinie text="Ophørsdato" value={re.ejer.ophoersdato} key={re.cvrnummer+re.ejer.ophoersdato} />}
                      <hr/>
                    </div>
                  )
                })}
              </div>)}

        </div>

    )

  }

}
