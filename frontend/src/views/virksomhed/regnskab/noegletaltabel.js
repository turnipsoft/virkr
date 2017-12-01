import React from 'react';
import NoegletalRaekke from './noegletalraekke';
import RegnskabLinkRaekke from './regnskablinkraekke';

const NoegletalTabel = ({regnskaber}) => {
  return (
    <div className="card noegletal-tabel-card">
      <div className="card-block">
        <table className="table table-hover table-no-bordered noegletal-tabel" border="0" >
          <NoegletalRaekke header={true} label="År" felt="aar" regnskaber={regnskaber} />

          <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.omsaetning"
                           label="Omsætning"
                           style="noegletal-label-bold"
                            regnskaber={regnskaber} />

          <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.vareforbrug" label="Vareforbrug" negative={true}
                           regnskaber={regnskaber} />
          <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.driftsindtaegter" label="Andre driftsindtægter"
                           regnskaber={regnskaber} />
          <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.eksterneomkostninger" label="Eksterne omkostninger" negative={true}
                           regnskaber={regnskaber} />
          <NoegletalRaekke felt="resultatopgoerelse.omsaetningTal.variableomkostninger" label="Variable omkostninger" negative={true}
                           regnskaber={regnskaber} />

          {emptyRow()}

          <NoegletalRaekke style="noegletal-label-bold" label="Bruttofortjeneste" felt="resultatopgoerelse.bruttoresultatTal.bruttofortjeneste"
                           regnskaber={regnskaber} highlight={true}/>
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.kapitalandeleiassocieredevirksomheder"
                           label="Indtægter af kapitalandele" regnskaber={regnskaber} />
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.medarbejderomkostninger"
                           label="Kapacitetsomkostninger" regnskaber={regnskaber} negative={true} />
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.regnskabsmaessigeafskrivninger"
                           label="Afskrivninger" regnskaber={regnskaber} negative={true} />
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.lokalomkostninger"
                           label="Ejendomsomkostninger" regnskaber={regnskaber} negative={true} />
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.administrationsomkostninger"
                           label="Administrative omkostninge" regnskaber={regnskaber} negative={true} />
          <NoegletalRaekke felt="resultatopgoerelse.bruttoresultatTal.administrationsomkostninger"
                           label="Administrative omkostninger" regnskaber={regnskaber} negative={true} />

          {emptyRow()}

          <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.driftsresultat"
                           style="noegletal-label-bold" highlight={true}
                           label="Resultat før finansielle poster" regnskaber={regnskaber} />

          <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.finansielleindtaegter"
                           label="Finansielle indtægter" regnskaber={regnskaber} />
          <NoegletalRaekke felt="resultatopgoerelse.nettoresultatTal.finansielleomkostninger"
                           label="Finansielle omkostninger" regnskaber={regnskaber} />

          {emptyRow()}

          <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.resultatfoerskat"
                           style="noegletal-label-bold" highlight={true}
                           label="Årets resultat før skat" regnskaber={regnskaber} />

          <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.skatafaaretsresultat"
                           label="Skat af årets resultat" regnskaber={regnskaber} negative={true}  />

          {emptyRow()}

          <NoegletalRaekke felt="resultatopgoerelse.aaretsresultatTal.aaretsresultat"
                           style="noegletal-label-bold"
                           label="Årets resultat" regnskaber={regnskaber} highlight={true} />

          {emptyRow()}
          <NoegletalRaekke felt="balance.passiver.egenkapital"
                           label="Egenkapital" regnskaber={regnskaber} />
          <NoegletalRaekke felt="balance.passiver.gaeldsforpligtelser"
                           label="Gældsforpligtelser" regnskaber={regnskaber} />
          {emptyRow()}
          <RegnskabLinkRaekke regnskaber={regnskaber} />

        </table>
      </div>
    </div>
  );
}

const emptyRow = () => {
  return (
    <tr><td>&nbsp;</td></tr>
  );
}

export default NoegletalTabel;
