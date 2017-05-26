import React, { Component } from 'react';
import Noegletal from './noegletal';

export default class Regnskabstal extends Component {

  render() {
    return (

      <div className="card">
        <div className="card-header" id="regnskab-header">
          <b>{this.props.regnskab.aar}</b> <span className="small">({this.props.regnskab.startDato} - {this.props.regnskab.slutDato})</span>
        </div>
        <div className="card-block">

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

}
