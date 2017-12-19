import React, {Component} from 'react';
import NoegletalRaekke from './noegletalraekke';
import RegnskabLinkRaekke from './regnskablinkraekke';
import RevisionsRaekke from './revisionsraekke';
import NoegletalGraf from './noegletalgraf';

export default class NoegletalTabel extends Component {

  constructor(props) {
    super(props);
    this.state = { selectedFelt: null,
      resultatopgoerelseVis: true,
      aktiverVis: true,
      passiverVis: true,
      revisionVis: true
    }
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

    const roClass = this.state.resultatopgoerelseVis ? 'fa fa-minus' : 'fa fa-plus';
    const aClass = this.state.aktiverVis ? 'fa fa-minus' : 'fa fa-plus';
    const pClass = this.state.passiverVis ? 'fa fa-minus' : 'fa fa-plus';
    const rClass = this.state.revisionVis ? 'fa fa-minus' : 'fa fa-plus';

    return (
      <div className="aarsregnskab">
        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4> <span className={roClass} onClick={()=> this.setState({resultatopgoerelseVis : !this.state.resultatopgoerelseVis})} /> Resultatopgørelse </h4>

            {this.state.resultatopgoerelseVis &&
            <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse />
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
              <RegnskabLinkRaekke regnskaber={regnskaber} />
              </tbody>

            </table>}
          </div>
        </div>

        <br/>

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4> <span className={aClass} onClick={()=> this.setState({aktiverVis : !this.state.aktiverVis})} />Aktiver </h4>

            {this.state.aktiverVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse/>
              <tbody>

              {this.header('Anlægsaktiver')}
              {this.emptyRow()}

              {this._renderNoegletal("balance.aktiver.faerdiggjorteudviklingsprojekter","Færdiggjorte udviklingsprojekter")}
              {this._renderNoegletal("balance.aktiver.erhvervedeimmaterielleanlaegsaktiver","Erhvervede immaterielle anlægsaktiver")}
              {this._renderNoegletal("balance.aktiver.materielleanlaegsaktiverunderudfoerelse","Materielle anlægsaktiver under udførelse")}
              {this._renderNoegletal("balance.aktiver.immaterielleanlaegsaktiver","Immaterielle anlægsaktiver ialt","noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.andreanlaegdriftoginventar","Andre anlæg, driftsmateriel, inventar")}
              {this._renderNoegletal("balance.aktiver.materielleanlaegsaktiver","Materielle anlægsaktiver","noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.langsigtedekapitalandeleitilknyttedevirksomheder","Kapitalandele")}
              {this._renderNoegletal("balance.aktiver.andretilgodehavender","Andre tilgodehavender")}
              {this._renderNoegletal("balance.aktiver.finansielleanlaegsaktiver","Finansielle anlægsaktiver ialt", "noegletal-label-bold")}

              {this.emptyRow()}

              {this._renderNoegletal("balance.aktiver.anlaegsaktiver","Anlægsaktiver ialt", "noegletal-label-bold")}

              {this.emptyRow()}
              {this.header('Omsætningsaktiver')}
              {this._renderNoegletal("balance.aktiver.raavareroghjaelpematerialer","Råvarer og hjælpematerialer")}
              {this._renderNoegletal("balance.aktiver.fremstilledevareroghandelsvarer","Fremstillede varer og handelsvarer")}
              {this._renderNoegletal("balance.aktiver.varebeholdninger","Varebeholdninger ialt",'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.tilgodehavenderfrasalogtjenesteydelser","Tilgodehavende fra salg og tjenesteydelser")}
              {this._renderNoegletal("balance.aktiver.tilgodehaverhostilknyttedevirksomheder","Tilgodehavende fra tilknyttede virksomheder")}
              {this._renderNoegletal("balance.aktiver.andretilgodehavenderomsaetningaktiver","Andre tilgodehavender")}
              {this._renderNoegletal("balance.aktiver.periodeafgraensningsposter","Periodeafgrænsningsposter")}
              {this._renderNoegletal("balance.aktiver.tilgodehavenderialt","Tilgodehavender ialt",'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.andrevaerdipapirerogkapitalandele","Andre værdipapirer og kapitalandele")}
              {this._renderNoegletal("balance.aktiver.vaerdipapirerialt","Værdipapirer ialt","noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.likvidebeholdninger","Likvide beholdninger")}
              {this.emptyRow()}
              {this._renderNoegletal("balance.aktiver.omsaetningsaktiver","Omsætningsaktiver ialt", 'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.aktiver","Aktiver ialt", 'noegletal-label-bold')}

              </tbody>

            </table>}
          </div>
        </div>

        <br />

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4><span className={pClass} onClick={()=> this.setState({passiverVis : !this.state.passiverVis})} /> Passiver </h4>

            {this.state.passiverVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse/>
              <tbody>

              {this.header('Egenkapital')}
              {this.emptyRow()}

              {this._renderNoegletal("balance.passiver.virksomhedskapital","Virksomhedskapital")}
              {this._renderNoegletal("balance.passiver.overfoertresultat","Overført resultat")}
              {this._renderNoegletal("balance.passiver.udbytte","Foreslået udbytte")}

              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.egenkapital","Egenkapital ialt", "noegletal-label-bold")}

              {this.emptyRow()}
              {this.header('Hensatte Forpligtelser')}
              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.hensaettelserforudskudtskat","Hensættelser til udskudt skat")}
              {this._renderNoegletal("balance.passiver.andrehensaettelser","Andre hensatte forpligtelser")}

              {this._renderNoegletal("balance.passiver.hensatteforpligtelser","Hensatte forpligtelser ialt", "noegletal-label-bold")}
              {this.emptyRow()}

              {this.header('Gældsforpligtelser')}
              {this._renderNoegletal("balance.passiver.gaeldtilrealkredit","Gæld til realkreditinstitutter")}
              {this._renderNoegletal("balance.passiver.andenlangfristetgaeld","Anden langfristet gæld")}
              {this._renderNoegletal("balance.passiver.deposita","Deposita")}
              {this._renderNoegletal("balance.passiver.langfristedegaeldsforpligtelser","Langfristede gældsforpligtelser ialt","noegletal-label-bold")}
              {this.emptyRow()}

              {this._renderNoegletal("balance.passiver.igangvaerendearbejderforfremmedregning","Igangværende arbejde for fremmed regning")}
              {this._renderNoegletal("balance.passiver.kortsigtedegaeldsforpligtelser","Gældsforpligtelser")}
              {this._renderNoegletal("balance.passiver.gaeldsforpligtelsertilpengeinstitutter","Gældsforpligtelser til pengeinsitutter")}
              {this._renderNoegletal("balance.passiver.leverandoereraftjenesteydelser","Leverandører af tjenester og ydelser")}
              {this._renderNoegletal("balance.passiver.gaeldtiltilknyttedevirksomheder","Gæld til tilknyttede virksomheder")}
              {this._renderNoegletal("balance.passiver.kortfristetskyldigskat","Kortfristet skyldig til skat")}
              {this._renderNoegletal("balance.passiver.andregaeldsforpligtelser","Anden gæld")}
              {this._renderNoegletal("balance.passiver.modtagneforudbetalingerfrakunder","Modtagne forudbetalinger")}

              {this._renderNoegletal("balance.passiver.periodeafgraensningsposter","Periodeafgrænsningsposter")}
              {this._renderNoegletal("balance.passiver.kortfristedegaeldsforpligtelserialt","Kortfristede gældsforpligtelser i alt")}
              {this._renderNoegletal("balance.passiver.gaeldsforpligtelser","Gældsforpligtelser ialt","noegletal-label-bold")}
              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.passiverialt","Passiver ialt","noegletal-label-bold")}

              </tbody>

            </table>}
          </div>
        </div>

        <br />

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4><span className={rClass} onClick={()=> this.setState({revisionVis : !this.state.revisionVis})} /> Revision </h4>

            {this.state.revisionVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse/>
              <tbody>
              {this.state.revisionVis && <RevisionsRaekke regnskaber={regnskaber} />}
              </tbody>

            </table>}
          </div>
        </div>


      </div>
    )
  }

  header(header) {
    return (
      <tr><td className="noegletal-table-header">{header}</td><td>&nbsp;</td></tr>
    );
  }

  emptyRow() {
    return (
      <tr><td>&nbsp;</td></tr>
    );
  }
}

