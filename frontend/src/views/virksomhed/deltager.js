import React, { Component } from 'react';
import DetaljeLinie from '../common/detaljelinie';

export default class Deltager extends React.Component {

  render() {
    const {deltager} = this.props;

    let adrmouseover = ""
    deltager.adresser.forEach( (adr) => {
      const fra = adr.periode.gyldigFra ? adr.periode.gyldigFra : "          "
      const til = adr.periode.gyldigTil ? adr.periode.gyldigTil : "          "
      adrmouseover += fra + " - "+til+ " "+adr.adresselinie +"\n"
    })

    const adresseMarkup = <div>{deltager.nyesteAdresse.adresselinie}
        &nbsp; <span title={adrmouseover} className="badge badge-pill badge-default">{deltager.adresser.length}</span>
    </div>

    return(
      <div className="card deltagercard" onClick={ () => this._visDeltager(deltager) }>
        <div className="card-block resizable-block">
          <DetaljeLinie text="Navn" value={deltager.nyesteNavn} />
          <DetaljeLinie text="Adresse" custom={adresseMarkup} />
          <DetaljeLinie text="Roller" value={deltager.rollerAsString} />
        </div>
      </div>
    )
  }

  _visGamleAdresser(deltager) {
    alert(deltager.adresser);
  }

  _visDeltager(deltager) {
    this.props.visDeltager(deltager.deltager.enhedsNummer, true);
  }

}
