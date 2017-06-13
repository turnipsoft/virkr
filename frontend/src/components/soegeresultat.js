import React, { Component } from 'react';

export default class Soegeresultat extends Component {

  constructor(props) {
    super(props);
    this._visVirksomhed = this._visVirksomhed.bind(this);
  }

  _visVirksomhed(e) {
    const cvrnummer = e.currentTarget.value;

    this.props.opdaterCvrNummer(cvrnummer);
  }

  render() {
    const { soegeresultat } = this.props;

    return(
      <div>
        <div className="row">
          <div className="col-6 offset-3">
          <h5>Vælg virksomhed</h5>
          </div>
        </div>

    {soegeresultat.map((soegeresultat) => {
      return (
        <div key={soegeresultat.cvrNummer} className="row">
          <div className="col col-6 offset-3">
            <button type="button" className="btn btn-secondary btn-lg btn-block" onClick={this._visVirksomhed}
            value={soegeresultat.cvrNummer}>
              <span className="pull-left btn-virksomhedsnavn">{soegeresultat.virksomhedMetadata.nyesteNavn.navn}</span>
              <span className="pull-right btn-cvrnummer">{soegeresultat.cvrNummer}</span>
            </button>
          </div>

        </div>
      );})}
    </div>);
  }

}
