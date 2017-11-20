import React from 'react';
import { Link } from 'react-router-dom';

const DeltagerCard = (props) => {
  const { deltager } = props;

  const navn = deltager.navn;
  //const enhedsnummer = deltager.enhedsNummer;
  var adresse = ""
  if (deltager.adresselinie) {
    adresse = deltager.adresselinie;
  }

  if (deltager.postnr && deltager.postnr!='0') {
    adresse += ", "+ deltager.bylinie;
  }

  const virksomheder = deltager.virksomhedsliste;

  const link = `/deltager/${deltager.enhedsNummer}`;
  return (
    <Link to={link} >
      <div className="card soegeresultatcard">
        <div className="card-block">
          <h5 className="card-title">{navn}</h5>
          <div className="card-text">
            {adresse? <p>{adresse}</p>:null}
            {virksomheder?<p><b>Associerede virksomheder:</b> {virksomheder}</p>:null}
          </div>
        </div>
      </div>
    </Link>
  )
}

export default DeltagerCard;
