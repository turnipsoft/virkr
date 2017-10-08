import React from 'react';

const DeltagerInfo = (props) => {

  const deltager = props.deltager;
  const navn = deltager.navn;

  const {
    adresselinie,
    bylinie,
    postnr
  } = deltager;

  return (
    <div className="row justify-content-center">
      <div className="col justify-content-center">

        <p><strong>{navn}</strong></p>
          {adresselinie?<div>{adresselinie}</div>:null}
          {postnr && postnr!='0'?<div>{bylinie}</div>:null}

      </div>
    </div>
  );
}

export default DeltagerInfo;
