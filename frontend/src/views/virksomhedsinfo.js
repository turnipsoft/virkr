import React from 'react';

const VirksomhedsInfo = (props) => {

  const vdata = props.data.virksomhedMetadata;

  const navn = vdata.nyesteNavn.navn;

  const {
    vejadresselinie,
    byLinje
  } = vdata.nyesteBeliggenhedsadresse;

  return (
    <div className="row justify-content-center">
      <div className="col">
        <p>
          <strong>{navn}</strong>
          <br/>
          {vejadresselinie}
          <br />
          {byLinje}
        </p>
      </div>
    </div>
  );
}

export default VirksomhedsInfo;
