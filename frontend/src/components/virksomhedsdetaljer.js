import React, { Component } from 'react';

export default class VirksomhedsDetaljer extends Component {

  render() {

    const vdata = this.props.cvrdata.virksomhedMetadata;

    const navn = vdata.nyesteNavn.navn;

    const {
      vejadresselinie,
      byLinje
    } = vdata.nyesteBeliggenhedsadresse;

    const branche = vdata.nyesteHovedbranche.branchetekst;

    var email = "";
    if (this.props.cvrdata.elektroniskPost && this.props.cvrdata.elektroniskPost.length>0) {
      email = this.props.cvrdata.elektroniskPost[0].kontaktoplysning;
    }

    var www = "";
    if (this.props.cvrdata.hjemmeside && this.props.cvrdata.hjemmeside.length>0) {
      www = this.props.cvrdata.hjemmeside[0].kontaktoplysning;
    }

    var telefon = "";
    if (this.props.cvrdata.telefonNummer && this.props.cvrdata.telefonNummer.length>0) {
      telefon = this.props.cvrdata.telefonNummer[0].kontaktoplysning;
    }

    const apikey = "AIzaSyCkZhz21v6u43m7qMUZiFe6obxHoTLhvgE";
    const q = vejadresselinie+", "+byLinje
    const mapsurl =  "https://www.google.com/maps/embed/v1/place?q="+q+"&key="+apikey;

    return (
      <div className="card">
        <div className="virksomhedsdetaljer">
          <div className="card-block">

            <div className="row">
              <div className="col col-6 virksomhedsinfo">
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
                <DetaljeLinie text="Telefon" value={telefon} />
                <DetaljeLinie text="Email" value={email} />
                <DetaljeLinie text="Hjemmeside" value={www} />
                <DetaljeLinie text="Branche" value={branche} />
              </div>
              <div className="col col-6 map">
                <span className="pull-right">
                  <iframe height={300}
                    frameBorder="0" style={{border:1}}
                    src={mapsurl} allowFullScreen>
                  </iframe>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

    );
  }

}

class DetaljeLinie extends Component {

  render() {

    if (this.props.value) {
      return (
        <div className="row">
          <div className="col col-4">
            {this.props.text}:
          </div>
          <div className="col col-8">
            {this.props.value}
          </div>
        </div> );
    }

    return(null);
  }
}
