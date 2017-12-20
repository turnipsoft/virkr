import React, {Component} from 'react';

export default class Revision extends Component {

  render() {

    const {revision} = this.props;

    return (<div className="revision">
      {this._renderRow(null, revision.assistancetype)}
      {this._renderRow(null, revision.revisionsfirmaNavn)}
      {this._renderCvrNummer(revision)}
      {this._renderEmptyRow()}
      {this._renderRow(null, revision.beskrivelseAfRevisor)}
      {this._renderRow(null, revision.navnPaaRevisor)}
      {this._renderRow(null, revision.mnenummer)}
      {this._renderEmptyRow()}
      {this._renderTooltipRow(revision.konklusionMedForbehold, revision.konklusionMedForbehold)}

      {(revision.konklusionMedForbehold!==null && revision.konklusionMedForbehold!==undefined ) && this._renderTooltipRow('Grundlag for konklusion', revision.grundlagForKonklusion)}
      {this._renderTooltipRow('Fremhævelse af andre forhold', revision.supplerendeInformationOmAndreForhold)}
      {this._renderTooltipRow('Fremhævelse af forhold i regnskabet', revision.supplerendeInformationOmAarsrapport)}
      {this._renderTooltipRow('Fremhævelse af forhold vedrørende revisionen', revision.supplerendeInformationOmRevision)}
      {this._renderTooltipRow('Væsentligt usikkerhed  vedr. fortsat drift', revision.vaesentligUsikkerhedVedrFortsatDrift)}
      {this._renderTooltipRow('Fravalg af revision', revision.ingenRevision)}
      {this._renderEmptyRow()}
      {this._renderUnderskrivere(revision)}
    </div>)
  }

  _renderUnderskrivere(revision) {
    return [
      this._renderRow(null, 'Underskrivere:', true),
      ...revision.underskrivere.map((u)=>{
        return this._renderRow(null, u);
      })
    ];
  }

  _renderTooltipRow(vaerdi, tooltip) {
    if (tooltip) {
      return (
        <div className="row"><div className="col"><span title={tooltip}>{vaerdi}&nbsp;</span><span title={tooltip} className="fa fa-exclamation fa-lg red" /></div></div>
      )
    }
  }

  _renderEmptyRow() {
    return (<div className="row"><div className="col">&nbsp;</div> </div> )
  }

  _renderRow(label, vaerdi, bold) {
    const labelMarkup = label? <div className="col"><b>{label}</b></div>:'';

    const v = bold?<b>{vaerdi}</b>:vaerdi

    if (vaerdi) {
      return (
        <div className="row" key={vaerdi}>{labelMarkup}<div className="col">{v}</div></div>
      )
    }
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
}
