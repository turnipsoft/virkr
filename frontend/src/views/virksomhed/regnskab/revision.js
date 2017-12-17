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
      {this._renderTooltipRow('Grundlag for konklusion', revision.grundlagForKonklusion)}
      {this._renderTooltipRow('Fravalg af revision', revision.ingenRevision)}
      {this._renderTooltipRow('Supplerende Information', revision.supplerendeInformationOmAndreForhold)}
      {this._renderTooltipRow('Supplerende Information', revision.supplerendeInformationOmAarsrapport)}
    </div>)
  }

  _renderTooltipRow(vaerdi, tooltip) {
    if (tooltip) {
      return (
        <div className="row"><div className="col"><span title={tooltip}>{vaerdi}&nbsp;</span><span title={tooltip} className="fa fa-exclamation red" /></div></div>
      )
    }
  }

  _renderEmptyRow() {
    return (<div className="row"><div className="col">&nbsp;</div> </div> )
  }

  _renderRow(label, vaerdi) {
    const labelMarkup = label? <div className="col"><b>{label}</b></div>:'';

    if (vaerdi) {
      return (
        <div className="row">{labelMarkup}<div className="col">{vaerdi}</div></div>
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
