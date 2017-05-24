import React, { Component } from 'react';

export default class Regnskabstal extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    return (
      <div className="card">
        <div className="card-header" id="regnskab-header">
          <b>{this.props.aar}</b> <span className="small">({this.props.startDato} - {this.props.slutDato})</span>
        </div>
        <div className="card-block">
          <table className="table table-striped">
            <tbody>
              <tr scope="row">
                <td>Bruttofortjeneste </td><td className="beloeb">{this._komma(this.props.bruttofortjeneste)}</td>
              </tr>
              <tr scope="row">
                <td>Driftsresultat </td><td>{this._komma(this.props.driftsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Resultat før skat </td><td>{this._komma(this.props.resultatfoerskat)}</td>
              </tr>
              <tr scope="row">
                <td>Skat af årets resultat </td><td>{this._komma(this.props.skatafaaretsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Årets resultat </td><td>{this._komma(this.props.aaretsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Gældsforpligtelser </td><td>{this._komma(this.props.gaeldsforpligtelser)}</td>
              </tr>
              <tr scope="row">
                <td>Egenkapital </td><td>{this._komma(this.props.egenkapital)}</td>
              </tr>
            </tbody>
          </table>
          <a href={this.props.pdfUrl} target="_blank" className="btn btn-primary">Hent regnskab som PDF</a>
        </div>
      </div>
    );
  }

  _komma(vaerdi) {
    if (vaerdi) {
      const v = vaerdi.toLocaleString();
      return v.replace(/,/g, ".");
    }

    return vaerdi;
  }
}
