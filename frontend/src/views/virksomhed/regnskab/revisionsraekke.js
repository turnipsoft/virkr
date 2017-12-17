import React, {Component} from 'react';
import Revision from './revision';

export default class RevisionsRaekke extends Component {

  render () {
    const {regnskaber} = this.props;

    const revisionsMarkup = regnskaber.map((regnskab)=> {
      return this._renderRevision(regnskab)
    });

    return (
      <tr>
        <td>Revision</td>
        {revisionsMarkup}
      </tr>
    )
  }

  _renderRevision(regnskab) {

    const revision = regnskab.revision
    return (
      <td className="noegletal-vaerdi" key={regnskab.aar}>
        <Revision revision={revision} />
      </td>
    );
  }
}
