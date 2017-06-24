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

          <div className="noegletal">
            <Noegletal noegletal={this.props.regnskab.omsaetning} text="Omsætning" b={true} />
            <Noegletal noegletal={this.props.regnskab.vareforbrug} text="Vareforbrug" negative={true} />
            <Noegletal noegletal={this.props.regnskab.driftsindtaegter} text="Andre driftsindtægter"/>
            <Noegletal noegletal={this.props.regnskab.eksterneomkostninger} text="Eksterne omkostninger"
                       negative={true} underline={true} />
            <Noegletal noegletal={this.props.regnskab.andreEksterneOmkostninger} text="Andre eksterne omkostninger"
                       negative={true} underline={true} />
            <Noegletal noegletal={this.props.regnskab.variableOmkostninger} text="Variable omkostninger"
                       negative={true} underline={true} />

            <br/>
            <Noegletal noegletal={this.props.regnskab.bruttofortjeneste} text="Bruttofortjeneste" b={true} />
            <Noegletal noegletal={this.props.regnskab.medarbejderOmkostninger} text="Kapacitetsomkostninger" negative={true} />
            <Noegletal noegletal={this.props.regnskab.regnskabsmaessigeAfskrivninger} text="Afskrivninger"
                       negative={true} underline={true} />
            <Noegletal noegletal={this.props.regnskab.lokalomkostninger} text="Ejendomsomkostninger"
                       negative={true} />
            <Noegletal noegletal={this.props.regnskab.administrationsomkostninger} text="Administrative omkostninger"
                       negative={true} underline={true} />
            <br/>
            <Noegletal noegletal={this.props.regnskab.driftsresultat} text="Resultat før finansielle poster" b={true} />
            <Noegletal noegletal={this.props.regnskab.finansielleIndtaegter} text="Finansielle indtægter" />
            <Noegletal noegletal={this.props.regnskab.finansielleOmkostninger} text="Finansielle omkostninger"
                       negative={true} underline={true}/>
            <br/>
            <Noegletal noegletal={this.props.regnskab.resultatfoerskat} text="Årets resultat før skat" b={true} />
            <Noegletal noegletal={this.props.regnskab.skatafaaretsresultat} text="Skat af årets resultat" negative={true}
                       underline={true}/>

            <br/>
            <Noegletal noegletal={this.props.regnskab.aaretsresultat} text="Årets resultat" h={true} />

            <br/>
            <Noegletal noegletal={this.props.regnskab.udbytte} text="Foreslået udbytte" />

            <br/>
            <Noegletal noegletal={this.props.regnskab.egenkapital} text="Egenkapital" b={true} />

            <br/>
            <Noegletal noegletal={this.props.regnskab.gaeldsforpligtelser} text="Gældsforpligtelser" b={true} />

            <br/>

            <div className="row">
              <div className="col col-12">
                <span className="pull-right">
                  <a href={this.props.regnskab.xbrlUrl} target="_blank" className="btn btn-primary">XBRL</a>
                  &nbsp;
                  <a href={this.props.regnskab.pdfUrl} target="_blank" className="btn btn-primary ">PDF</a>
                </span>
              </div>
            </div>

          </div>
        </div>
      </div>
    );
  }

}