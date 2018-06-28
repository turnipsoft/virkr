import React, {Component} from 'react';

export default class RevisionSection extends Component {

  render() {
    const {regnskaber, sz, revisorer} = this.props;

    console.log(revisorer);

    return ([this._renderRevisionstype(regnskaber, sz, revisorer),
      this._renderRevisionsselskab(regnskaber, sz),
      this._renderRevisor(regnskaber, sz),
      this._renderFravalgAfRevision(regnskaber, sz),
      this._renderKonklusion(regnskaber,sz),
      this._renderForhold(regnskaber,sz),
      this._renderUnderskrivere(regnskaber,sz)]);

  }

  _renderRevisionstype(regnskaber, sz, revisorer) {
    const szp = `${sz}%`;
    let index=0;
    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'revisionstype_'+regnskab.aar;
      const revision = regnskab.aktueltAarsregnskab.revision
      let value = revision.assistancetype;
      index++;
      if (value===null) value = '-';
      return (
        <td width={szp} className="noegletal-vaerdi" key={key}>
          {this._renderValue(value, key+index)}
          {this._renderUoverenstemmeseMedRegistreretRevisorOgIngenBistand(revision, revisorer, regnskab)}
        </td>
      )
    });

    return (
      <tr key='revisionstype'>
        <td width="20%"><b>Revisionstype</b></td>
        {revisionsMarkup}

      </tr>
    )
  }

  _renderRevisionsselskab(regnskaber, sz) {
    const szp = `${sz}%`;
    let index = 0;
    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'revisionsfirma'+regnskab.aar;
      const revision = regnskab.aktueltAarsregnskab.revision;
      index++;
      return (
        <td width={szp} className="noegletal-vaerdi" key={key}>
          {this._renderCvrNummer(revision)}
          {this._renderValue(revision.revisionsfirmaNavn, key+index)}
        </td>
      )

    });

    return (
      <tr key='revisionsselskab'>
        <td width="20%"><b>Revisionsvirksomhed</b></td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderUoverenstemmeseMedRegistreretRevisorOgIngenBistand(revision, revisorer, regnskab) {
    if (revision.assistancetype && revision.assistancetype == 'Ingen bistand') {
      let fundet = null;
      revisorer.forEach((revisor) => {
        if (revisor.periode.gyldigFra < regnskab.slutdato &&
          ( (revisor.periode.gyldigTil && revisor.periode.gyldigTil > regnskab.slutdato) || revisor.periode.gyldigTil===null)) {
          fundet = revisor;
        }
      });

      if (fundet) {
        return (
          <div className="row">
            <div className="col">
              Revisoren {fundet.navn} er registreret som revisor for virksomheden siden {fundet.periode.gyldigFra} selvom der ikke er ydet bistand til udarbejdelse af årsrapporten<span className="fa fa-exclamation red fa-lg" />
            </div>
          </div>
        )
      }
    }

    return null;
  }

  _renderKonklusion(regnskaber, sz) {
    const szp = `${sz}%`;

    let konklusionSat = false;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'konklusion_'+regnskab.aar;
      const revision = regnskab.aktueltAarsregnskab.revision;
      if (revision.konklusionMedForbehold!==null && revision.konklusionMedForbehold!==undefined) {
        konklusionSat = true;
        return (
          <td width={szp} className="noegletal-vaerdi" key={key}>
            {this._renderTooltipValue(revision.konklusionMedForbehold, revision.konklusionMedForbehold,'rev_'+key)}
            {this._renderTooltipValue('Grundlag for konklusion', revision.grundlagForKonklusion,'grund_'+key)}
          </td>
        )
      } else {
        return (<td width={szp} className="noegletal-vaerdi" key={key}>
          &nbsp;
        </td>);
      }


    });

    if (konklusionSat) {
      return (
        <tr key='konklusion'>
          <td width="20%"><b>Konklusion</b></td>
          {revisionsMarkup}
        </tr>
      )
    }
  }

  getForhold(revision) {
    let resultat = {};

    if (revision.supplerendeInformationOmAndreForhold) {
      resultat.andreforhold = {
        text : 'Fremhævelse af andre forhold',
        value : revision.supplerendeInformationOmAndreForhold,
        key : 'andreforhold'
      }
    }

    if (revision.supplerendeInformationOmAarsrapport) {
      resultat.aarsrapport = {
        text : 'Fremhævelse af forhold i regnskabet',
        value : revision.supplerendeInformationOmAarsrapport,
        key : 'aarsrapport'
      }
    }

    if (revision.supplerendeInformationOmRevision) {
      resultat.revision = {
        text : 'Fremhævelse af forhold vedrørende revisionen',
        value : revision.supplerendeInformationOmRevision,
        key : 'revision'
      }
    }

    if (revision.vaesentligUsikkerhedVedrFortsatDrift) {
      resultat.drift = {
        text : 'Væsentligt usikkerhed  vedr. fortsat drift',
        value : revision.vaesentligUsikkerhedVedrFortsatDrift,
        key : 'drift'
      }
    }

    if (revision.ingenRevision) {
      resultat.ingenrevision = {
        text : 'Ingen Revision',
        value : revision.ingenRevision,
        key : 'ingenrevision'
      }
    }

    return resultat;
  }

  _renderForhold(regnskaber, sz) {
    const szp = `${sz}%`;

    let forholdSat = false;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'forhold_'+regnskab.aar;
      const revision = regnskab.aktueltAarsregnskab.revision;
      const forhold = this.getForhold(revision);
      const forholdValues = Object.values(forhold);

      if (forholdValues!==null && forholdValues.length>0) {
        forholdSat = true;
        const forholdMarkup = forholdValues.map( (v) => {
          return this._renderTooltipValue(v.text, v.value, v.key+'_'+key);
        })
        return (
          <td width={szp} className="noegletal-vaerdi" key={key}>
            {forholdMarkup}
          </td>
        )
      } else {
        return (<td width={szp} className="noegletal-vaerdi" key={key}>
          &nbsp;
        </td>);
      }


    });

    if (forholdSat) {
      return (
        <tr key='forhold'>
          <td width="20%"><b>Forhold</b></td>
          {revisionsMarkup}
        </tr>
      )
    }
  }

  _renderUnderskrivere(regnskaber, sz) {
    const szp = `${sz}%`;
    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'underskrivere_'+regnskab.aktueltAarsregnskab.aar;
      let index=0;
      const underskriverMarkup = regnskab.aktueltAarsregnskab.revision.underskrivere.map((u)=>{
        index++;
        return this._renderValue(u, key+index);
      })
      return (
        <td width={szp} className="noegletal-vaerdi" key={key}>
          {underskriverMarkup}
        </td>
      );
    })

    return (
      <tr key='underskrivere'>
        <td width="20%"><b>Underskrivere</b></td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderFravalgAfRevision(regnskaber, sz) {

    const szp = `${sz}%`;
    const fravalg = regnskaber.filter( (regnskab) => regnskab.aktueltAarsregnskab.revision.ingenRevision);
    if (fravalg.length>0) {
      const revisionsMarkup = regnskaber.map((regnskab)=> {
        const key = 'fravalg_'+regnskab.aar;
        const revision = regnskab.aktueltAarsregnskab.revision;
        return (
          <td width={szp} className="noegletal-vaerdi" key={key}>
            {this._renderTooltipValue('Fravalg af revision', revision.ingenRevision,key)}
          </td>
        )

      });

      return (
        <tr key='fravalg'>
          <td width="20%"><b>Fravalg af revision</b></td>
          {revisionsMarkup}
        </tr>
      )

    }

    return null;
  }

  _renderRevisor(regnskaber, sz) {

    const szp = `${sz}%`;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      const key = 'revisor_'+regnskab.aar;
      const revision = regnskab.aktueltAarsregnskab.revision;
      let index=0;

      if (revision.navnPaaRevisor!==null) {
        index++;
        return (
          <td width={szp} className="noegletal-vaerdi" key={key}>
            {this._renderValue(revision.beskrivelseAfRevisor, key+'100'+index)}
            {this._renderValue(revision.navnPaaRevisor, key+'200'+index)}
            {this._renderValue(revision.mnenummer, key+'300'+index)}
          </td>
        )
      } else {
        return (
          <td width={szp} className="noegletal-vaerdi" key={key}>
            -
          </td>
        )
      }

    });

    return (
      <tr key="revisor">
        <td width="20%"><b>Revisor</b></td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderCvrNummer(revision) {
    if (revision.revisionsfirmaCvrnummer) {
      const href = './#/virksomhed/'+revision.revisionsfirmaCvrnummer
      return (
        <div className="row">
          <div className="col">
            <a href={href} target="_blank">{revision.revisionsfirmaCvrnummer}</a>
          </div>
        </div>
      )
    }
  }


  _renderTdValue(key, vaerdi, sz) {
    const szp = `${sz}%`;

    if (vaerdi) {
      return (
        <td width={szp} className="noegletal-vaerdi" key={key}>
          {this._renderValue(vaerdi)}
        </td>
      )
    }
  }

  _renderValue(vaerdi, key='id') {
    if (vaerdi) {
      return (
        <div className="row" key={key} >
          <div className="col">{vaerdi}</div>
        </div>
      )
    }
  }

  _renderTooltipValue(vaerdi, tooltip, n) {
    const id = 'popup_'+n;
    if (tooltip) {
      return (
        <div className="row" key={n}><div className="col">
          <div className="clickable">
            <span title={tooltip} onClick={() => this._showPopup(id)}>{vaerdi}&nbsp;</span><span title={tooltip} onClick={() => this._showPopup(id)} className="fa fa-exclamation fa-lg red" />
          </div>
          <div id={id} className="overlay">
            <div className="popup">
              <h2>{vaerdi}</h2>
              <span className="close" onClick={() => this._hidePopup(id)}>&times;</span>
              <div className="content">
                <br/>
                {tooltip}
              </div>
            </div>
          </div>
        </div></div>
      )
    }
  }

  _showPopup(elementId) {
    let elem = document.getElementById(elementId);
    elem.style.visibility = 'visible';
    elem.style.opacity = 1;
  }

  _hidePopup(elementId) {
    let elem = document.getElementById(elementId);
    elem.style.visibility = 'hidden';
    elem.style.opacity = 0;
  }
}
