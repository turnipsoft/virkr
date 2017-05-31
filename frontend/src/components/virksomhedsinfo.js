import React from 'react';

const VirksomhedsInfo = (props) => {
  const {
    navn,
    vejnavn,
    husnr,
    postnr,
    bynavn
  } = props.data;

  return (
    <div className="row">
      <div className="col">
        <div style={{ textAlign: 'center' }}>
          <h4>{navn}</h4>
            <p>
              {vejnavn} {husnr}
              <br />
              {postnr} {bynavn}
            </p>
          </div>
        </div>
      </div >
  );
}

export default VirksomhedsInfo;
