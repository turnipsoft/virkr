import React, {Component} from 'react';
import Revision from './revision';

export default class RevisionsRaekke extends Component {

  render () {
    const {regnskaber, sz, revisorer} = this.props;

    let rowIndex=0;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      rowIndex++;
      return this._renderRevision(regnskab.aktueltAarsregnskab, sz, revisorer, rowIndex)
    });

    return (
      <tr>
        <td width="20%">Revision</td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderRevision(regnskab, sz, revisorer, rowIndex) {

    const revision = regnskab.revision
    const szp = `${sz}%`;
    return (
      <td width={szp} className="noegletal-vaerdi" key={regnskab.aar}>
        <Revision revision={revision} revisorer={revisorer} regnskab={regnskab} rowIndex={rowIndex} />
      </td>
    );
  }
}
