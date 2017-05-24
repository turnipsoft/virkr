import React from 'react';
import ReactDOM from 'react-dom';
import { Line } from 'react-chartjs-2';
import 'whatwg-fetch';
import './main.css';

class Virksomhed extends React.Component {

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

class CvrSoegebox extends React.Component {

  render() {
    return (<div className="cvr-input">
      <form onSubmit={this._opdaterCvrNummer.bind(this)}>
        <div className="input-group">
          <input type="text" ref={c => this._cvr = c}
            className="form-control" placeholder="Indtast CVR-Nummer"
            maxLength="8" id="txtSearch" />
          <div className="input-group-btn">
            <button className="btn btn-primary" type="submit">
              <span className="fa fa-search"></span>
            </button>
          </div>
        </div>
      </form>
    </div>);
  }

  _opdaterCvrNummer(e) {
    e.preventDefault();
    const iv = parseInt(this._cvr.value);
    if (!Number.isInteger(iv)) {
      alert(this._cvr.value + " er ikke et CVR-Nummer")
      return;
    }
    if (this._cvr.value && this._cvr.value.length != 8) {
      alert("Et CVR-Nummer består altid af 8 cifre");
      return;
    }
    this.props.opdaterCvr(this._cvr.value);
  }
}

class CvrVisning extends React.Component {

  componentDidUpdate() {

  }

  render() {
    const r = this._getRegnskabstal();
    const c = this._getChartJsLineChart();

    return (<div className="virksomhedsdata">
      {c}
      <br />
      <br />
      {r}
    </div>);
  }

  _getRegnskabstal() {
    return this.props.regnskaber.slice().reverse().map((regnskab) => {
      return <Regnskabstal
        id={regnskab.id}
        aar={regnskab.aar}
        startDato={regnskab.startDato}
        slutDato={regnskab.slutDato}
        bruttofortjeneste={regnskab.bruttofortjeneste}
        driftsresultat={regnskab.driftsresultat}
        egenkapital={regnskab.egenkapital}
        aaretsresultat={regnskab.aaretsresultat}
        resultatfoerskat={regnskab.resultatfoerskat}
        skatafaaretsresultat={regnskab.skatafaaretsresultat}
        gaeldsforpligtelser={regnskab.gaeldsforpligtelser}
        pdfUrl={regnskab.pdfUrl}
        key={regnskab.startDato} />
    });
  }

  _getChartJsLineChart() {

    if (this.props.regnskaber.length > 0) {

      Chart.defaults.global.scaleLabel = function (label) {
        return label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
      };

      Chart.defaults.global.multiTooltipTemplate = function (label) {
        return label.datasetLabel + ': ' + label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
      };

      const options = {
        tooltips: {
          enabled: true,

          callbacks: {
            label: function (tooltipItems, data) {
              return data.datasets[tooltipItems.datasetIndex].label + ' : ' + tooltipItems.yLabel.toLocaleString().replace(/,/g, ".");
            }
          }
        },
        scales: {
          yAxes: [{
            ticks: {
              // Create scientific notation labels
              callback: function (value) {
                return value.toLocaleString().replace(/,/g, ".");
              }
            }
          }]
        }
      }

      const aarLabels = this.props.regnskaber.map((regnskab) => {
        return regnskab.aar
      });

      const bruttofortjenester = this.props.regnskaber.map((regnskab) => {
        return regnskab.bruttofortjeneste
      });

      const egenkapitaler = this.props.regnskaber.map((regnskab) => {
        return regnskab.egenkapital;
      });

      const aaretsresultater = this.props.regnskaber.map((regnskab) => {
        return regnskab.aaretsresultat;
      });

      const data = {
        labels: aarLabels,
        datasets: [
          {
            label: "Bruttofortjeneste",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "rgba(75,192,192,0.4)",
            borderColor: "rgba(75,192,192,1)",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "rgba(75,192,192,1)",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(75,192,192,1)",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: bruttofortjenester,
            spanGaps: false
          },
          {
            label: "Egenkapital",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "#113d84",
            borderColor: "#113d84",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "#113d84",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "#113d84",
            pointHoverBorderColor: "#113d84",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: egenkapitaler,
            spanGaps: false
          },
          {
            label: "Årets resultat",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "#8884d8",
            borderColor: "#8884d8",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "#8884d8",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "#8884d8",
            pointHoverBorderColor: "#8884d8",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: aaretsresultater,
            spanGaps: false
          }
        ]
      };

      return (
        <div>
          <h2>Nøgletal for virksomheden</h2>
          <Line data={data} options={options} />
        </div>
      );
    }
  }

}

class Regnskabstal extends React.Component {

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


ReactDOM.render(
  <Virksomhed />,
  document.getElementById('react')
);
