import React, {Component} from 'react';

export default class RegnskabLinkRaekke extends Component {

  render () {
    const {regnskaber} = this.props;

    const links = regnskaber.map((regnskab) => {
      return {xbrl: regnskab.xbrlurl, pdf: regnskab.pdfurl }
    });

    const linksMarkup = links.map((link)=> {
      return this._renderButtons(link)
    });

    return (
      <tr>
        <td>XBRL og PDF Regnskab</td>
        {linksMarkup}
      </tr>
    )
  }

  _renderButtons(links) {
    return (
      <td className="noegletal-vaerdi" key={links.xbrl}>
        {links.xbrl && <a href={links.xbrl} title="Hent regnskab i XBRL format" target="_blank" className="btn btn-primary btn-sm"><span className="fa fa-file-code-o"/></a>}
        &nbsp;
        {links.pdf && <a href={links.pdf} title="Hent regnskab i PDF format" target="_blank" className="btn btn-primary btn-sm"><span className="fa fa-file-pdf-o"/></a>}
      </td>
    );
  }
}
