import React from 'react';

const SoegeresultatCard = ({ virksomhed, onClick } ) => {
  const {navn, cvrnr, adresseTekst} = virksomhed;

  return (
    <div className="card soegeresultatcard" onClick={() => onClick(cvrnr, true)}>
      <div className="card-block">
        <h5 className="card-title">{navn}</h5>
        <div className="card-text">
          {adresseTekst} (CVR-Nr: {cvrnr})
        </div>
      </div>
    </div>
  )
}

export default SoegeresultatCard;
