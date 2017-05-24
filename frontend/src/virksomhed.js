import React, {Component} from 'react';
import CvrSoegebox from './cvrsoegebox';
import CvrVisning from './cvrvisning';
import 'whatwg-fetch';

export default class Virksomhed extends Component {

  constructor() {
    super();

    this.state = {
      cvrnummer: '',
      regnskaber: []
    };
  }

  render() {
    return (<div className="virksomhed">
      <div className="row">
        <div className="col">
          <h1 id="virkr-header"><a href="/" id="virk-header-a">Virkr</a></h1>
          <p id="virkr-tagline">Nøgletal om virksomheder</p>
        </div>
      </div>
      <div className="row">
        <div className="col">
          <CvrSoegebox opdaterCvr={this._opdaterCvrNummer.bind(this)} />
        </div>
      </div>
      <div className="row">
        <div className="col">
          <CvrVisning cvrnummer={this.state.cvrnummer}
            regnskaber={this.state.regnskaber} />
        </div>
      </div>
    </div>);
  }

  _opdaterCvrNummer(cvrnr) {
    this.setState({
      cvrnummer: cvrnr
    });

    if (cvrnr && cvrnr.length == 8) {
      this._hentNoegletal(cvrnr);
    } else {
      this.setState({
        regnskaber: []
      })
    }
  }

  _hentNoegletal(cvrnummer) {
    fetch('http://localhost:9092/regnskab/' + cvrnummer, { mode: 'cors' })
      .then((response) => {
        if (response.ok) {
          return response.json()
        } else {
          console.error(response);
          throw new Error("Kan ikke hente data");
        }
      })
      .then((json) => this.setState({ regnskaber: json.regnskabsdata }))
      .catch((error) => alert(error))
  }
}
