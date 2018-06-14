import React, { Component } from 'react';
import DetaljeLinie from '../common/detaljelinie';
import SectionHeader from '../common/sectionheader';

export default class VirksomhedsDetaljer extends Component {

  constructor(props) {
    super(props);

    this.state = { visStamdata: true };
  }

  render() {

    if (!this.state.visStamdata) {
      return (
        <div>
          <SectionHeader label="Stamdata" detail="Stamdata om virksomheden" iconClass="fa fa-industry white"
                         onClick={() => this.setState({visStamdata: !this.state.visStamdata})} />
          <br/>
        </div>
      );
    }

    const cvrdata = this.props.cvrdata;

    const vdata = cvrdata.virksomhedMetadata;

    const navn = vdata.nyesteNavn.navn;

    const {
      vejadresselinie,
      byLinje
    } = vdata.nyesteBeliggenhedsadresse;

    const branche = vdata.nyesteHovedbranche? vdata.nyesteHovedbranche.branchetekst + " (" + vdata.nyesteHovedbranche.branchekode + ")" : "Ukendt branche";

    let email = "";
    if (cvrdata.nyesteEmail) {
      email = cvrdata.nyesteEmail;
    }

    let www = "";
    if (cvrdata.nyesteHjemmeside) {
      www = cvrdata.nyesteHjemmeside;
    }

    let telefon = "";
    if (cvrdata.nyesteTelefonNummer) {
      telefon = cvrdata.nyesteTelefonNummer
    }

    let telefax = "";
    if (cvrdata.nyesteTelefaxNummer) {
      telefax = cvrdata.nyesteTelefaxNummer
    }

    const virksomhedsform = vdata.nyesteVirksomhedsform.virksomhedsformkode + " - "+ vdata.nyesteVirksomhedsform.langBeskrivelse;

    const status = cvrdata.nyesteStatus

    const ophoersdato = cvrdata.nyesteLivsforloeb.periode.gyldigTil

    const apikey = "AIzaSyCkZhz21v6u43m7qMUZiFe6obxHoTLhvgE";
    const q = vejadresselinie+", "+byLinje
    const mapsurl =  "https://www.google.com/maps/embed/v1/place?q="+q+"&key="+apikey;

    return (
      <div>
        <SectionHeader label="Stamdata" detail="Stamdata om virksomheden" iconClass="fa fa-industry white"
                       onClick={() => this.setState({visStamdata: !this.state.visStamdata})} />
        <br/>
        <div className="card">
          <div className="virksomhedsdetaljer">
            <div className="card-block resizable-block">

              <div className="row">
                <div className="col col-12 virksomhedsinfo">
                  <div className="row">
                    <div className="col col-12">
                      <b>{navn}</b>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col col-12">
                      {vejadresselinie}
                    </div>
                  </div>
                  <div className="row">
                    <div className="col col-12">
                      {byLinje}
                    </div>
                  </div>
                  <br/>
                  <DetaljeLinie text="CVR-Nummer" value={cvrdata.cvrNummer} />
                  <DetaljeLinie text="Virksomhedsform" value={virksomhedsform} />
                  <DetaljeLinie text="Telefon" value={telefon} />
                  <DetaljeLinie text="Telefax" value={telefax} />
                  <DetaljeLinie text="Email" value={email} />
                  <DetaljeLinie text="Hjemmeside" value={www} />
                  <DetaljeLinie text="Branche" value={branche} />
                  <DetaljeLinie text="Kapital" value={cvrdata.kapital} />
                  <DetaljeLinie text="Tegningsregel" value={cvrdata.tegningsregel} />
                  <DetaljeLinie text="Formål" value={cvrdata.formaal} />
                  <DetaljeLinie text="Status" value={status} />
                  <DetaljeLinie text="Ophørsdato" value={ophoersdato} />
                </div>
              </div>
              <br/>
              <hr/>

              <div className="row">
                <div className="col col-12 map">
                  <span>
                    <iframe height="100%" width="100%"
                      frameBorder="0" style={{border:1}}
                      src={mapsurl} allowFullScreen>
                    </iframe>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    );
  }

}
