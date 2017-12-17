import React, {Component} from 'react';

export default class Revision extends Component {

  render() {

    const {revision} = this.props;

    const ingenRevision = revision.ingenRevision?'Ingen revision af regnskabet': '';

    return (<div className="revision">
      {this._renderRow(null, revision.assistancetype)}
      {this._renderRow(null, ingenRevision)}
      {this._renderRow(null, revision.revisionsfirmaNavn)}
      {this._renderCvrNummer(revision)}
      {this._renderEmptyRow()}
      {this._renderRow(null, revision.beskrivelseAfRevisor)}
      {this._renderRow(null, revision.navnPaaRevisor)}
      {this._renderRow(null, revision.mnenummer)}
    </div>)
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
