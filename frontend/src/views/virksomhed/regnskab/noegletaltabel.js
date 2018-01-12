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

  _renderNoegletal(felt, label, sz, style=null, negative=false, skat=false) {
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
                         sz={sz}
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
    const {regnskaber, revisorer} = this.props;

    const roClass = this.state.resultatopgoerelseVis ? 'fa fa-minus o-fold' : 'fa fa-plus o-fold';
    const aClass = this.state.aktiverVis ? 'fa fa-minus o-fold' : 'fa fa-plus o-fold';
    const pClass = this.state.passiverVis ? 'fa fa-minus o-fold' : 'fa fa-plus o-fold';
    const rClass = this.state.revisionVis ? 'fa fa-minus o-fold' : 'fa fa-plus o-fold';
    const sz = 80/regnskaber.length;

    return (
      <div className="aarsregnskab">
        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4> <span className={roClass} onClick={()=> this.setState({resultatopgoerelseVis : !this.state.resultatopgoerelseVis})} /> Resultatopgørelse </h4>

            {this.state.resultatopgoerelseVis &&
            <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse sz={sz} />
              <tbody>

              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.omsaetning","Omsætning",sz,"noegletal-label-bold")}
              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.vareforbrug","Vareforbrug",sz,null, true)}
              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.andreeksterneomkostninger","Andre Eksterne omkostninger",sz,null, true)}
              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.driftsindtaegter","Andre driftsindtægter",sz)}
              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.eksterneomkostninger","Eksterne omkostninger",sz, null, true)}
              {this._renderNoegletal("resultatopgoerelse.omsaetningTal.variableomkostninger","Variable omkostninger",sz, null, true)}

              {this.emptyRow()}

              {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.bruttofortjeneste","Bruttofortjeneste",sz,"noegletal-label-bold")}
              {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger","Personaleomkostninger",sz,null, true)}
              {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger","Afskrivninger",sz,null, true)}
              {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.lokalomkostninger","Ejendomsomkostninger",sz,null, true)}
              {this._renderNoegletal("resultatopgoerelse.bruttoresultatTal.administrationsomkostninger","Administrative omkostninger",sz,null, true)}

              {this.emptyRow()}
              {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.driftsresultat","Resultat før finansielle poster",sz,"noegletal-label-bold")}
              {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.kapitalandeleiassocieredevirksomheder","Kapitalandele i associerede virksomheder",sz)}
              {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.kapitalandeleitilknyttedevirksomheder","Kapitalandele i tilknyttede virksomheder",sz)}
              {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.finansielleindtaegter","Finansielle indtægter",sz)}
              {this._renderNoegletal("resultatopgoerelse.nettoresultatTal.finansielleomkostninger","Finansielle omkostninger",sz, null, true)}

              {this.emptyRow()}
              {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.resultatfoerskat","Årets resultat før skat",sz,"noegletal-label-bold")}
              {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat","Skat af årets resultat",sz, null, false, true)}
              {this.emptyRow()}
              {this._renderNoegletal("resultatopgoerelse.aaretsresultatTal.aaretsresultat","Årets resultat",sz,"noegletal-label-bold")}
              {this.emptyRow()}
              <RegnskabLinkRaekke regnskaber={regnskaber} />
              </tbody>

            </table>}
          </div>
        </div>

        <br/>

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4> <span className={aClass} onClick={()=> this.setState({aktiverVis : !this.state.aktiverVis})} /> Aktiver </h4>

            {this.state.aktiverVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse sz={sz} />
              <tbody>

              {this.header('Anlægsaktiver')}
              {this.emptyRow()}

              {this._renderNoegletal("balance.aktiver.faerdiggjorteudviklingsprojekter","Færdiggjorte udviklingsprojekter",sz)}
              {this._renderNoegletal("balance.aktiver.erhvervedeimmaterielleanlaegsaktiver","Erhvervede immaterielle anlægsaktiver",sz)}
              {this._renderNoegletal("balance.aktiver.materielleanlaegsaktiverunderudfoerelse","Materielle anlægsaktiver under udførelse",sz)}
              {this._renderNoegletal("balance.aktiver.immaterielleanlaegsaktiver","Immaterielle anlægsaktiver ialt",sz,"noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.andreanlaegdriftoginventar","Andre anlæg, driftsmateriel, inventar",sz)}
              {this._renderNoegletal("balance.aktiver.grundeogbygninger","Grunde og bygninger",sz)}
              {this._renderNoegletal("balance.aktiver.materielleanlaegsaktiver","Materielle anlægsaktiver",sz,"noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.langsigtedekapitalandeleitilknyttedevirksomheder","Kapitalandele",sz)}
              {this._renderNoegletal("balance.aktiver.andretilgodehavender","Andre tilgodehavender",sz)}
              {this._renderNoegletal("balance.aktiver.finansielleanlaegsaktiver","Finansielle anlægsaktiver ialt",sz, "noegletal-label-bold")}

              {this.emptyRow()}

              {this._renderNoegletal("balance.aktiver.anlaegsaktiver","Anlægsaktiver ialt",sz, "noegletal-label-bold")}

              {this.emptyRow()}
              {this.header('Omsætningsaktiver')}
              {this._renderNoegletal("balance.aktiver.raavareroghjaelpematerialer","Råvarer og hjælpematerialer",sz)}
              {this._renderNoegletal("balance.aktiver.fremstilledevareroghandelsvarer","Fremstillede varer og handelsvarer",sz)}
              {this._renderNoegletal("balance.aktiver.varebeholdninger","Varebeholdninger ialt",sz,'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.tilgodehavenderfrasalogtjenesteydelser","Tilgodehavende fra salg og tjenesteydelser",sz)}
              {this._renderNoegletal("balance.aktiver.tilgodehaverhostilknyttedevirksomheder","Tilgodehavende fra tilknyttede virksomheder",sz)}
              {this._renderNoegletal("balance.aktiver.langfristedetilgodehavenderhosvirksomhedsdeltagereogledelse","Langfristede tilgodehavener fra virksomhed og ledelse",sz)}
              {this._renderNoegletal("balance.aktiver.kortfristedetilgodehavenderhosvirksomhedsdeltagereogledelse","Kortfristede tilgodehavener fra virksomhed og ledelse",sz)}
              {this._renderNoegletal("balance.aktiver.tilgodehavenderfravirksomhedsdeltagereogledelse","Tilgodehavener fra virksomhed og ledelse",sz)}

              {this._renderNoegletal("balance.aktiver.andretilgodehavenderomsaetningaktiver","Andre tilgodehavender",sz)}
              {this._renderNoegletal("balance.aktiver.periodeafgraensningsposter","Periodeafgrænsningsposter",sz)}
              {this._renderNoegletal("balance.aktiver.tilgodehavenderialt","Tilgodehavender ialt",sz,'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.andrevaerdipapirerogkapitalandele","Andre værdipapirer og kapitalandele",sz)}
              {this._renderNoegletal("balance.aktiver.vaerdipapirerialt","Værdipapirer ialt",sz,"noegletal-label-bold")}
              {this._renderNoegletal("balance.aktiver.likvidebeholdninger","Likvide beholdninger",sz)}
              {this.emptyRow()}
              {this._renderNoegletal("balance.aktiver.omsaetningsaktiver","Omsætningsaktiver ialt",sz, 'noegletal-label-bold')}
              {this._renderNoegletal("balance.aktiver.aktiver","Aktiver ialt",sz, 'noegletal-label-bold')}

              </tbody>

            </table>}
          </div>
        </div>

        <br />

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4><span className={pClass} onClick={()=> this.setState({passiverVis : !this.state.passiverVis})} /> Passiver </h4>

            {this.state.passiverVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse sz={sz}/>
              <tbody>

              {this.header('Egenkapital')}
              {this.emptyRow()}

              {this._renderNoegletal("balance.passiver.virksomhedskapital","Virksomhedskapital",sz)}
              {this._renderNoegletal("balance.passiver.overfoertresultat","Overført resultat",sz)}
              {this._renderNoegletal("balance.passiver.udbytte","Foreslået udbytte",sz)}

              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.egenkapital","Egenkapital ialt",sz, "noegletal-label-bold")}

              {this.emptyRow()}
              {this.header('Hensatte Forpligtelser')}
              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.hensaettelserforudskudtskat","Hensættelser til udskudt skat",sz)}
              {this._renderNoegletal("balance.passiver.andrehensaettelser","Andre hensatte forpligtelser",sz)}

              {this._renderNoegletal("balance.passiver.hensatteforpligtelser","Hensatte forpligtelser ialt",sz, "noegletal-label-bold")}
              {this.emptyRow()}

              {this.header('Gældsforpligtelser')}
              {this._renderNoegletal("balance.passiver.gaeldtilrealkredit","Gæld til realkreditinstitutter",sz)}
              {this._renderNoegletal("balance.passiver.andenlangfristetgaeld","Anden langfristet gæld",sz)}
              {this._renderNoegletal("balance.passiver.deposita","Deposita",sz)}
              {this._renderNoegletal("balance.passiver.langfristedegaeldsforpligtelser","Langfristede gældsforpligtelser ialt",sz,"noegletal-label-bold")}
              {this.emptyRow()}

              {this._renderNoegletal("balance.passiver.igangvaerendearbejderforfremmedregning","Igangværende arbejde for fremmed regning",sz)}
              {this._renderNoegletal("balance.passiver.kortsigtedegaeldsforpligtelser","Gældsforpligtelser",sz)}
              {this._renderNoegletal("balance.passiver.gaeldsforpligtelsertilpengeinstitutter","Gældsforpligtelser til pengeinsitutter",sz)}
              {this._renderNoegletal("balance.passiver.leverandoereraftjenesteydelser","Leverandører af tjenester og ydelser",sz)}
              {this._renderNoegletal("balance.passiver.gaeldtiltilknyttedevirksomheder","Gæld til tilknyttede virksomheder",sz)}
              {this._renderNoegletal("balance.passiver.kortfristetskyldigskat","Kortfristet skyldig til skat",sz)}
              {this._renderNoegletal("balance.passiver.andregaeldsforpligtelser","Anden gæld",sz)}
              {this._renderNoegletal("balance.passiver.modtagneforudbetalingerfrakunder","Modtagne forudbetalinger",sz)}

              {this._renderNoegletal("balance.passiver.periodeafgraensningsposter","Periodeafgrænsningsposter",sz)}
              {this._renderNoegletal("balance.passiver.kortfristedegaeldsforpligtelserialt","Kortfristede gældsforpligtelser i alt",sz)}
              {this._renderNoegletal("balance.passiver.gaeldsforpligtelser","Gældsforpligtelser ialt",sz,"noegletal-label-bold")}
              {this.emptyRow()}
              {this._renderNoegletal("balance.passiver.passiverialt","Passiver ialt",sz,"noegletal-label-bold")}

              </tbody>

            </table>}
          </div>
        </div>

        <br />

        <div className="card noegletal-tabel-card">
          <div className="card-block table-responsive">
            <h4><span className={rClass} onClick={()=> this.setState({revisionVis : !this.state.revisionVis})} /> Revision </h4>

            {this.state.revisionVis && <table className="table table-hover noegletal-tabel">
              <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} inkluderRegnksabsklasse sz={sz} />
              <tbody>
              {this.state.revisionVis && <RevisionsRaekke regnskaber={regnskaber} sz={sz} revisorer={revisorer} />}
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

