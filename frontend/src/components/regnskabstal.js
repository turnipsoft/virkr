import React, { Component } from 'react';

export default class Regnskabstal extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { aar,
      startDato,
      slutDato,
      bruttofortjeneste,
      driftsresultat,
      resultatfoerskat,
      skatafaaretsresultat,
      aaretsresultat,
      gaeldsforpligtelser,
      egenkapital,
      pdfUrl} = this.props.data;

    return (
      <div className="card">
        <div className="card-header" id="regnskab-header">
          <b>{aar}</b> <span className="small">({startDato} - {slutDato})</span>
        </div>
        <div className="card-block">
          <table className="table table-striped">
            <tbody>
              <tr scope="row">
                <td>Bruttofortjeneste </td><td className="beloeb">{this._komma(bruttofortjeneste)}</td>
              </tr>
              <tr scope="row">
                <td>Driftsresultat </td><td>{this._komma(driftsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Resultat før skat </td><td>{this._komma(resultatfoerskat)}</td>
              </tr>
              <tr scope="row">
                <td>Skat af årets resultat </td><td>{this._komma(skatafaaretsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Årets resultat </td><td>{this._komma(aaretsresultat)}</td>
              </tr>
              <tr scope="row">
                <td>Gældsforpligtelser </td><td>{this._komma(gaeldsforpligtelser)}</td>
              </tr>
              <tr scope="row">
                <td>Egenkapital </td><td>{this._komma(egenkapital)}</td>
              </tr>
            </tbody>
          </table>
          <a href={pdfUrl} target="_blank" className="btn btn-primary">Hent regnskab som PDF</a>
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
