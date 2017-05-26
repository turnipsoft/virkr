import React from 'react';
import ReactDOM from 'react-dom';
import { Line } from 'react-chartjs-2';
import 'whatwg-fetch';
import './main.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'font-awesome-webpack'

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
      <div className="row">
        <div className="col-xs-4"/>
        <div className="col-xs-4">
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
        </div>
        <div className="col-xs-4" />

      </div>
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
        regnskab={regnskab}
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

class Noegletal extends React.Component {

  render() {
    if (this.props.noegletal) {
      let text = this.props.text;
      let tal = this.props.noegletal;

      if (this.props.negative) {
        tal = -tal;
      }

      tal = this._komma(tal);
      if (this.props.b) {
        text = <b>{text}</b>
        tal = <b>{tal}</b>
      }

      if (this.props.h) {
        text = <h4>{text}</h4>
        tal = <h4>{tal}</h4>
      }

      let bottomClass = "col-xs-2";
      if (this.props.underline) {
        bottomClass += " bottom";
      }

      return (
        <div className="row">
          <div className="col-xs-2"></div>
          <div className="col-xs-6">
            {text}
          </div>
          <div className={bottomClass}>
            <u><span className="pull-right">{tal} </span></u>
          </div>
          <div className="col-xs-2"></div>
        </div>
      );

    }

    return(null);
  }

  _komma(vaerdi) {
    if (vaerdi) {
      const v = vaerdi.toLocaleString();
      return v.replace(/,/g, ".");
    }

    return vaerdi;
  }
}

class Regnskabstal extends React.Component {

  render() {
    return (

      <div className="panel panel-primary">
        <div className="panel-heading" id="regnskab-header">
          <b>{this.props.regnskab.aar}</b> <span className="small">({this.props.regnskab.startDato} - {this.props.regnskab.slutDato})</span>
        </div>
        <div className="panel-body">

          <Noegletal noegletal={this.props.regnskab.omsaetning} text="Omsætning"/>
          <Noegletal noegletal={this.props.regnskab.bruttofortjeneste} text="Bruttofortjeneste" b={true} />
          <br/>
          <Noegletal noegletal={this.props.regnskab.driftsresultat} text="Driftsresultat" b={true} />
          <br/>

          <Noegletal noegletal={this.props.regnskab.finansielleIndtaegter} text="Andre finansielle indtægter" />
          <Noegletal noegletal={this.props.regnskab.finansielleOmkostninger} text="Andre finansielle omkostninger"
                     negative={true} underline={true}/>
          <Noegletal noegletal={this.props.regnskab.resultatfoerskat} text="Årets resultat før skat" b={true} />

          <br/>
          <Noegletal noegletal={this.props.regnskab.skatafaaretsresultat} text="Skat af årets resultat" negative={true}
                     underline={true}/>

          <br/>
          <Noegletal noegletal={this.props.regnskab.aaretsresultat} text="Årets resultat" h={true} />

          <br/>
          <Noegletal noegletal={this.props.regnskab.egenkapital} text="Egenkapital" b={true} />

          <br/>
          <Noegletal noegletal={this.props.regnskab.gaeldsforpligtelser} text="Gældsforpligtelser" b={true} />

          <br/>

          <div className="row">
            <div className="col-xs-2" />
            <div className="col-xs-10" >
              <a href={this.props.regnskab.pdfUrl} target="_blank" className="btn btn-primary">Hent regnskab som PDF</a>
            </div>
          </div>
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
