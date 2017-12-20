import React, {Component} from 'react';
import Revision from './revision';

export default class RevisionsRaekke extends Component {

  render () {
    const {regnskaber, sz} = this.props;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      return this._renderRevision(regnskab.aktueltAarsregnskab, sz)
    });

    return (
      <tr>
        <td width="20%">Revision</td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderRevision(regnskab, sz) {

    const revision = regnskab.revision
    const szp = `${sz}%`;
    return (
      <td width={szp} className="noegletal-vaerdi" key={regnskab.aar}>
        <Revision revision={revision} />
      </td>
    );
  }
}
