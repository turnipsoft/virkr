import React from 'react';

const SoegeresultatCard = (props) => {
  const { virksomhed, callback } = props;

  const navn = virksomhed.navn;
  const cvr = virksomhed.cvrnr;

  return (
    <div className="card soegeresultatcard" onClick={() => callback(cvr)}>
      <div className="card-block">
        <h5 className="card-title">{navn}</h5>
        <div className="card-text">CVR {cvr}</div>
      </div>
    </div>
  )
}

export default SoegeresultatCard;
