import React, { Component } from 'react';
import Noegletal from './noegletal';

export default class Regnskabstal extends Component {

  render() {
    const omsaetning = this.props.regnskab.resultatopgoerelse.omsaetningTal
    const brutto = this.props.regnskab.resultatopgoerelse.bruttoresultatTal
    const netto = this.props.regnskab.resultatopgoerelse.nettoresultatTal
    const aaretsresultat = this.props.regnskab.resultatopgoerelse.aaretsresultatTal
    const passiver = this.props.regnskab.balance.passiver

    return (
      <div className="card regnskabscard">
        <div className="card-header">
          <b>{this.props.regnskab.aar}</b> <span className="small">({this.props.regnskab.startdato} - {this.props.regnskab.slutdato})</span>
        </div>

        {/* Vis et sammendrag på xs */}
        <div className="card-block hidden-sm-up">
          <Noegletal noegletal={brutto.bruttofortjeneste} text="Bruttofortjeneste" />
          <br />
          <Noegletal noegletal={passiver.egenkapital} text="Egenkapital" />
          <br />
          <Noegletal noegletal={aaretsresultat.aaretsresultat} text="Årets resultat" />
        </div>

        {/* Fra sm og op kan vise noget mere */}
        <div className="card-block hidden-xs-down">
          <div className="regnskabsgruppe">
            <Noegletal noegletal={omsaetning.omsaetning} text="Omsætning" b={true} />
            <Noegletal noegletal={omsaetning.vareforbrug} text="Vareforbrug" negative={true} />
            <Noegletal noegletal={omsaetning.driftsindtaegter} text="Andre driftsindtægter" />
            <Noegletal noegletal={omsaetning.eksterneomkostninger} text="Eksterne omkostninger"
              negative={true} />
            <Noegletal noegletal={omsaetning.andreeksterneOmkostninger} text="Andre eksterne omkostninger"
              negative={true} />
            <Noegletal noegletal={omsaetning.variableomkostninger} text="Variable omkostninger"
              negative={true} />
          </div>

          <br />
          <div className="regnskabsgruppe">
            <Noegletal noegletal={brutto.bruttofortjeneste} text="Bruttofortjeneste" b={true} />
            <Noegletal noegletal={brutto.medarbejderomkostninger} text="Kapacitetsomkostninger" negative={true} />
            <Noegletal noegletal={brutto.regnskabsmaessigeafskrivninger} text="Afskrivninger"
              negative={true} />
            <Noegletal noegletal={brutto.lokalomkostninger} text="Ejendomsomkostninger"
              negative={true} />
            <Noegletal noegletal={brutto.administrationsomkostninger} text="Administrative omkostninger"
              negative={true} />
            <Noegletal noegletal={brutto.kapitalandeleiassocieredevirksomheder} text="Indtægter af kapitalandele" />
          </div>

          <br />

          <div className="regnskabsgruppe">
            <Noegletal noegletal={netto.driftsresultat} text="Resultat før finansielle poster" b={true} />
            <Noegletal noegletal={netto.finansielleindtaegter} text="Finansielle indtægter" />
            <Noegletal noegletal={netto.finansielleomkostninger} text="Finansielle omkostninger"
              negative={true} />
          </div>
          <br />
          <div className="regnskabsgruppe">
            <Noegletal noegletal={aaretsresultat.resultatfoerskat} text="Årets resultat før skat" b={true} />
            <Noegletal noegletal={aaretsresultat.skatafaaretsresultat} text="Skat af årets resultat" negative={true} />
          </div>

          <br />
          <Noegletal noegletal={aaretsresultat.aaretsresultat} text="Årets resultat" h={true} />


          <br />
          <Noegletal noegletal={passiver.egenkapital} text="Egenkapital" b={true} />

          <br />
          <Noegletal noegletal={passiver.gaeldsforpligtelser} text="Gældsforpligtelser" b={true} />

          <br />

          <div className="row">
            <div className="col col-12">
              <span className="pull-right">
                <a href={this.props.regnskab.xbrlurl} target="_blank" className="btn btn-primary">XBRL</a>
                &nbsp;
                  <a href={this.props.regnskab.pdfurl} target="_blank" className="btn btn-primary ">PDF</a>
              </span>
            </div>
          </div>
        </div>

      </div>
    );
  }

}

