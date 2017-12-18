import React, {Component} from 'react';
import NoegletalRaekke from './noegletalraekke';
import RegnskabLinkRaekke from './regnskablinkraekke';
import RevisionsRaekke from './revisionsraekke';
import NoegletalGraf from './noegletalgraf';

export default class NoegletalTabel extends Component {

  constructor(props) {
    super(props);
    this.state = { selectedFelt: null }
    this.selectNoegletal = this.selectNoegletal.bind(this);
  }

  selectNoegletal(felt) {
    if (this.state.selectedFelt === felt) {
      this.setState({selectedFelt: null });
    } else {
      this.setState({selectedFelt: felt});
    }
  }

  _renderNoegletal(felt, label, style=null, negative=false, skat=false) {
    const {regnskaber} = this.props;


    const graf = this._renderNoegletalGraf(regnskaber, label, felt)
    return (
      [
        <NoegletalRaekke felt={felt}
                         label={label}
                         style={style}
                         regnskaber={regnskaber}
                         negative={negative}
                         skat={skat}
                         key={felt}
                         onClick={()=>this.selectNoegletal(felt)} />,
        graf
      ]
    )
  }

  _renderNoegletalGraf(regnskaber, label, felt) {
    const selected = this.state.selectedFelt;

    if (selected === felt) {
      return <NoegletalGraf regnskaber={regnskaber} label={label}
                            felt={felt} key={label}/>;
    }

    return null;

  }

  render() {
    const {regnskaber} = this.props;

    return (
      <div className="card noegletal-tabel-card">
        <div className="card-block table-responsive">
          <table className="table table-hover noegletal-tabel">
            <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber}/>
            <tbody>

            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.omsaetning","Omsætning","noegletal-label-bold")}
            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.vareforbrug","Vareforbrug",null, true)}
            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.andreeksterneomkostninger","Andre Eksterne omkostninger",null, true)}
            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.driftsindtaegter","Andre driftsindtægter")}
            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.eksterneomkostninger","Eksterne omkostninger", null, true)}
            {this._renderNoegletal("resultatopgoerelse.omsaetningTal.variableomkostninger","Variable omkostninger", null, true)}

            {this.emptyRow()}

            {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.bruttofortjeneste","Bruttofortjeneste","noegletal-label-bold")}
            {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger","Personaleomkostninger",null, true)}
            {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger","Afskrivninger",null, true)}
            {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.lokalomkostninger","Ejendomsomkostninger",null, true)}
            {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.administrationsomkostninger","Administrative omkostninger",null, true)}

            {this.emptyRow()}
            {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.driftsresultat","Resultat før finansielle poster","noegletal-label-bold")}
            {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.kapitalandeleiassocieredevirksomheder","Kapitalandele i associerede virksomheder")}
            {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.kapitalandeleitilknyttedevirksomheder","Kapitalandele i tilknyttede virksomheder")}
            {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.finansielleindtaegter","Finansielle indtægter")}
            {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.finansielleomkostninger","Finansielle omkostninger", null, true)}

            {this.emptyRow()}
            {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.resultatfoerskat","Årets resultat før skat","noegletal-label-bold")}
            {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat","Skat af årets resultat", null, false, true)}

            {this.emptyRow()}

            {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.aaretsresultat","Årets resultat","noegletal-label-bold")}

            {this.emptyRow()}
            {this._renderNoegletal("balance.passiver.egenkapital","Egenkapital")}
            {this._renderNoegletal("balance.passiver.gaeldsforpligtelser","Gældsforpligtelser")}

            {this.emptyRow()}

            <RegnskabLinkRaekke regnskaber={regnskaber} />
            {this.emptyRow()}
            <RevisionsRaekke regnskaber={regnskaber} />
            </tbody>

          </table>
        </div>
      </div>
    )
  }

  emptyRow() {
    return (
      <tr><td>&nbsp;</td></tr>
    );
  }
}

