import React from 'react';
import ReactDOM from 'react-dom';
import jQuery from 'jquery';
import { LineChart, Line, CartesianGrid, YAxis, XAxis, Tooltip, Legend} from 'recharts';

class Virksomhed extends React.Component {

  constructor() {
    super();

    this.state = {
      cvrnummer : '',
      regnskaber : []
    };
  }

  render() {
    return (<div className="virksomhed">
              <div className="row">
                <div className="col">
                  <h1 id="virkr-header">Virkr</h1>
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
    this.setState( {
      cvrnummer : cvrnr
    });

    if (cvrnr && cvrnr.length==8) {
      this._hentNoegletal(cvrnr);
    } else {
      this.setState({
        regnskaber: []
      })
    }
  }

  _hentNoegletal(cvrnummer) {
      jQuery.ajax({
        method: 'GET',
        url: '/regnskab/'+cvrnummer,
        success : (rs) => {
          this.setState({
            regnskaber : rs.regnskabsdata
          })
        },
        error : (xhr, msg, err) => {
          alert(msg + err + xhr);
        }
      });
  }
}

class CvrSoegebox extends React.Component {

  render() {
    return (<div className="cvr-input">
      <form>
        <div className="input-group input-group-lg">
          <span className="input-group-addon" id="sizing-addon1">CVR</span>
          <input type="text" className="form-control input-lg"
            id="cvr" ref={c => this._cvr= c} placeholder="Udfyld CVR Nummer"
            aria-describedby="sizing-addon1"
            onChange={this._opdaterCvrNummer.bind(this)} />
        </div>

      </form>
    </div>);
  }

  _opdaterCvrNummer() {
    this.props.opdaterCvr(this._cvr.value);
  }
}

class CvrVisning extends React.Component {

  componentDidUpdate() {

  }

  render() {
    const r = this._getRegnskabstal();
    const c = this._getLineChart();

    return(<div className="virksomhedsdata">
      {c}
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
               key={regnskab.startDato} />
    });
  }

  _getLineChart() {

    if (this.props.regnskaber.length>0) {
      return (
        <LineChart width={1000} height={300} data={this.props.regnskaber}
            margin={{top: 5, right: 30, left: 100, bottom: 20}}>
            <XAxis dataKey="aar"/>
            <YAxis/>
            <CartesianGrid strokeDasharray="3 3"/>
            <Tooltip/>
            <Legend />
            <Line type="monotone" dataKey="aaretsresultat" name="Årets resultat" stroke="#8884d8" activeDot={{r: 8}}/>
            <Line type="monotone" dataKey="bruttofortjeneste" name="Bruttofortjeneste" stroke="#82ca9d" />
            <Line type="monotone" dataKey="egenkapital" name="Egenkapital" stroke="#113d84" />
      </LineChart>
      );
    }
    return;
  }

}

class Regnskabstal extends React.Component {

  render() {
    return(
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
          <a href="#" className="btn btn-primary">Hent regnskab som PDF</a>
        </div>
      </div>
    );
  }

  _komma(vaerdi) {
    if (vaerdi) {
      const v = vaerdi.toLocaleString();
      return v.replace(/,/g ,".");
    }

    return vaerdi;
  }
}


jQuery(function() {
  ReactDOM.render(
    <Virksomhed />,
    document.getElementById('react')
  );
})
