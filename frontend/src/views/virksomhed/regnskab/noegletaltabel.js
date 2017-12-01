import React, {Component} from 'react';
import NoegletalRaekke from './noegletalraekke';
import RegnskabLinkRaekke from './regnskablinkraekke';
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

  render() {

    const {regnskaber} = this.props;
    const selected = this.state.selectedFelt;

    return (
      <div className="card noegletal-tabel-card">
        <div className="card-block">
          <table className="table table-hover table-no-bordered noegletal-tabel">
            <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber}/>
            <tbody>

            <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.omsaetning"
                             label="Omsætning"
                             style="noegletal-label-bold"
                             regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.omsaetningTal.omsaetning')} />
            {selected==='resultatopgoerelse.omsaetningTal.omsaetning' &&
            <NoegletalGraf regnskaber={regnskaber} label="Omsætning"
                           felt="resultatopgoerelse.omsaetningTal.omsaetning"/>}

            <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.vareforbrug" label="Vareforbrug" negative={true}
                             regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.omsaetningTal.vareforbrug')} />
            {selected==='resultatopgoerelse.omsaetningTal.vareforbrug' &&
            <NoegletalGraf regnskaber={regnskaber} label="Vareforbrug"
                           felt="resultatopgoerelse.omsaetningTal.vareforbrug"/>}

            <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.driftsindtaegter" label="Andre driftsindtægter"
                             regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.omsaetningTal.driftsindtaegter')} />
            {selected==='resultatopgoerelse.omsaetningTal.driftsindtaegter' &&
            <NoegletalGraf regnskaber={regnskaber} label="Andre driftsindtægter"
                           felt="resultatopgoerelse.omsaetningTal.driftsindtaegter"/>}

            <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.eksterneomkostninger" label="Eksterne omkostninger"
                             negative={true}
                             regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.omsaetningTal.eksterneomkostninger')} />
            {selected==='resultatopgoerelse.omsaetningTal.eksterneomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Eksterne omkostninger"
                           felt="resultatopgoerelse.omsaetningTal.eksterneomkostninger"/>}


            <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.variableomkostninger" label="Variable omkostninger"
                             negative={true}
                             regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.omsaetningTal.variableomkostninger')} />
            {selected==='resultatopgoerelse.omsaetningTal.variableomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Variable omkostninger"
                           felt="resultatopgoerelse.omsaetningTal.variableomkostninger"/>}

            {this.emptyRow()}

            <NoegletalRaekke style="noegletal-label-bold" label="Bruttofortjeneste"
                             felt="resultatopgoerelse.bruttoresultatTal.bruttofortjeneste"
                             regnskaber={regnskaber} highlight={true} onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.bruttofortjeneste')} />
            {selected==='resultatopgoerelse.bruttoresultatTal.bruttofortjeneste' && <NoegletalGraf regnskaber={regnskaber} label="Bruttofortjeneste"
                           felt="resultatopgoerelse.bruttoresultatTal.bruttofortjeneste"/>}

            <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.kapitalandeleiassocieredevirksomheder"
                             label="Indtægter af kapitalandele" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.kapitalandeleiassocieredevirksomheder')} />
            {selected==='resultatopgoerelse.bruttoresultatTal.kapitalandeleiassocieredevirksomheder' &&
                  <NoegletalGraf regnskaber={regnskaber} label="Indtægter af kapitalandele"
                                 felt="resultatopgoerelse.bruttoresultatTal.kapitalandeleiassocieredevirksomheder"/>}

            <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger"
                             label="Kapacitetsomkostninger" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger')} />
            {selected==='resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Kapacitetsomkostninger"
                           felt="resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger"/>}


            <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger"
                             label="Afskrivninger" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger')} />
            {selected==='resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Afskrivninger"
                           felt="resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger"/>}

            <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.lokalomkostninger"
                             label="Ejendomsomkostninger" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.lokalomkostninger')}/>
            {selected==='resultatopgoerelse.bruttoresultatTal.lokalomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Ejendomsomkostninger"
                           felt="resultatopgoerelse.bruttoresultatTal.lokalomkostninger"/>}

            <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.administrationsomkostninger"
                             label="Administrative omkostninger" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.bruttoresultatTal.administrationsomkostninger')}/>
            {selected==='resultatopgoerelse.bruttoresultatTal.administrationsomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Administrative omkostninger"
                           felt="resultatopgoerelse.bruttoresultatTal.administrationsomkostninger"/>}


            {this.emptyRow()}

            <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.driftsresultat"
                             style="noegletal-label-bold" highlight={true}
                             label="Resultat før finansielle poster" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.nettoresultatTal.driftsresultat')}/>
            {selected==='resultatopgoerelse.nettoresultatTal.driftsresultat' &&
            <NoegletalGraf regnskaber={regnskaber} label="Resultat før finansielle poster"
                           felt="resultatopgoerelse.nettoresultatTal.driftsresultat"/>}

            <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.finansielleindtaegter"
                             label="Finansielle indtægter" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.nettoresultatTal.finansielleindtaegter')}/>
            {selected==='resultatopgoerelse.nettoresultatTal.finansielleindtaegter' &&
            <NoegletalGraf regnskaber={regnskaber} label="Finansielle indtægter"
                           felt="resultatopgoerelse.nettoresultatTal.finansielleindtaegter"/>}

            <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.finansielleomkostninger"
                             label="Finansielle omkostninger" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.nettoresultatTal.finansielleomkostninger')} />
            {selected==='resultatopgoerelse.nettoresultatTal.finansielleomkostninger' &&
            <NoegletalGraf regnskaber={regnskaber} label="Finansielle omkostninger"
                           felt="resultatopgoerelse.nettoresultatTal.finansielleomkostninger"/>}

            {this.emptyRow()}

            <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.resultatfoerskat"
                             style="noegletal-label-bold" highlight={true}
                             label="Årets resultat før skat" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.aaretsresultatTal.resultatfoerskat')}/>
            {selected==='resultatopgoerelse.aaretsresultatTal.resultatfoerskat' &&
            <NoegletalGraf regnskaber={regnskaber} label="Årets resultat før skat"
                           felt="resultatopgoerelse.aaretsresultatTal.resultatfoerskat"/>}

            <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat"
                             label="Skat af årets resultat" regnskaber={regnskaber} negative={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat')} />
            {selected==='resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat' &&
            <NoegletalGraf regnskaber={regnskaber} label="Skat af årets resultat"
                           felt="resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat"/>}

            {this.emptyRow()}

            <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.aaretsresultat"
                             style="noegletal-label-bold"
                             label="Årets resultat" regnskaber={regnskaber} highlight={true}
                             onClick={()=>this.selectNoegletal('resultatopgoerelse.aaretsresultatTal.aaretsresultat')} />
            {selected==='resultatopgoerelse.aaretsresultatTal.aaretsresultat' &&
            <NoegletalGraf regnskaber={regnskaber} label="Årets resultat"
                           felt="resultatopgoerelse.aaretsresultatTal.aaretsresultat"/>}

            {this.emptyRow()}
            <NoegletalRaekke felt="balance.passiver.egenkapital"
                             label="Egenkapital" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('balance.passiver.egenkapital')} />
            {selected==='balance.passiver.egenkapital' &&
            <NoegletalGraf regnskaber={regnskaber} label="Egenkapital"
                           felt="balance.passiver.egenkapital"/>}

            <NoegletalRaekke felt="balance.passiver.gaeldsforpligtelser"
                             label="Gældsforpligtelser" regnskaber={regnskaber}
                             onClick={()=>this.selectNoegletal('balance.passiver.gaeldsforpligtelser')} />
            {selected==='balance.passiver.gaeldsforpligtelser' &&
            <NoegletalGraf regnskaber={regnskaber} label="Gældsforpligtelser"
                           felt="balance.passiver.gaeldsforpligtelser"/>}

            {this.emptyRow()}

            <RegnskabLinkRaekke regnskaber={regnskaber}/>
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

