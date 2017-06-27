import React from 'react';

const SoegeresultatCard = (props) => {
  const { virksomhed, callback } = props;

  const navn = virksomhed.virksomhedMetadata.nyesteNavn.navn;
  const cvr = virksomhed.cvrNummer;

  return (
    <div className="card soegeresultatcard" onClick={ () => callback(cvr) }>
<div className="card-header">
<span className="pull-left">{navn}</span>
<span className="pull-right"><i className="fa fa-angle-right"></i> </span>

  </div>
      <div className="card-block">
        <div className="card-text">CVR {cvr}</div>
      </div>
    </div>
  )
}

export default SoegeresultatCard;
