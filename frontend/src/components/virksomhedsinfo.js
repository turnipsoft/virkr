import React from 'react';

const VirksomhedsInfo = (props) => {

  const vdata = props.data.virksomhedMetadata;

  const navn = vdata.nyesteNavn.navn;

  const {
    vejadresselinie,
    byLinje
  } = vdata.nyesteBeliggenhedsadresse;

  return (
    <div className="row">
      <div className="col">
        <div style={{ textAlign: 'center' }}>
          <h4>{navn}</h4>
            <p>
              {vejadresselinie}
              <br />
              {byLinje}
            </p>
          </div>
        </div>
      </div >
  );
}

export default VirksomhedsInfo;
