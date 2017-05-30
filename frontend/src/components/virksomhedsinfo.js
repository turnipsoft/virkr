import React, { Component } from 'react';

export default class VirksomhedsInfo extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { navn, vejnavn, husnr, postnr, bynavn } = this.props.data;

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

}
