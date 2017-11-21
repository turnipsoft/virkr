import React from 'react';

const DeltagerCard = ({ deltager, onClick }) => {
  const navn = deltager.navn;
  var adresse = '';
  if (deltager.adresselinie) {
    adresse = deltager.adresselinie;
  }

  if (deltager.postnr && deltager.postnr!='0') {
    adresse += ', '+ deltager.bylinie;
  }

  const virksomheder = deltager.virksomhedsliste;

  return (
    <div className="card soegeresultatcard" onClick={() => onClick(deltager.enhedsNummer) } >
      <div className="card-block">
        <h5 className="card-title">{navn}</h5>
        <div className="card-text">
          {adresse? <p>{adresse}</p>:null}
          {virksomheder?<p><b>Associerede virksomheder:</b> {virksomheder}</p>:null}
        </div>
      </div>
    </div>
  )
}

export default DeltagerCard;
