import React from 'react';

const SoegeresultatCard = ({ virksomhed, onClick } ) => {
  const navn = virksomhed.navn;
  const cvrnummer = virksomhed.cvrnr;

  return (
    <div className="card soegeresultatcard" onClick={() => onClick(cvrnummer, true)}>
      <div className="card-block">
        <h5 className="card-title">{navn}</h5>
        <div className="card-text">CVR {cvrnummer}</div>
      </div>
    </div>
  )
}

export default SoegeresultatCard;
