import React, { Component } from 'react';

export default class CvrSoegebox extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { soegeresultat } = this.props;

    return(
      <div>

      <div className="row">
        <div className="col-8 offset-2">
          <h5>Vælg virksomhed</h5>
        </div>
      </div>

    {soegeresultat.map((soegeresultat) => {
      return (
        <div className="row">
          <div className="col-6 offset-2">
            <a href="#"> {soegeresultat.virksomhedMetadata.nyesteNavn.navn}</a>
          </div>
          <div className="col-2 pull-right">
            ( {soegeresultat.cvrNummer})
          </div>
        </div>
      );})}
    </div>);
  }

}
