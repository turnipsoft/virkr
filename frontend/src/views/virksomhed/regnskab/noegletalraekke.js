import React, { Component } from 'react';
import { resolveJsonValue, komma } from '../../../utils/utils';

export default class NoegletalRaekke extends Component {

  render() {
    const { regnskaber, label, felt, style, header, negative, highlight, onClick } = this.props;

    const feltvaerdier = regnskaber.map((regnskab) => {
      const aktueltRegnskab = regnskab.aktueltAarsregnskab;

      let vaerdi = resolveJsonValue(felt, aktueltRegnskab);
      if (header) {
        return { vaerdi: vaerdi, start: aktueltRegnskab.startdato, slut: aktueltRegnskab.slutdato };
      }
      if (negative && vaerdi>0) {
        vaerdi = vaerdi * -1;
      }

      return { vaerdi: vaerdi };
    });

    // Ingen tomme rækker
    const empty = feltvaerdier.every((i) => { return (i.vaerdi === null || i.vaerdi === undefined)
      || i.vaerdi.vaerdi===null || i.vaerdi.vaerdi===undefined });

    if (empty && !header) {
      return null;
    }

    if (header) {
      return (
        <thead className="noegletal-header">
          <tr>
            <th>
              {label}
            </th>
            {this._renderVaerdier(feltvaerdier, header)}
          </tr>
        </thead>
      );
    }

    return (
      <tr className="noegletal-row" onClick={onClick} >
        <td className={style?style:''}>
          {label}
        </td>
        {this._renderVaerdier(feltvaerdier, header, highlight, style)}
      </tr>
    );
  }

  _renderVaerdier(vaerdier, header, highlight, style) {

    const className='noegletal-vaerdi '+style;

    let col = 0;

    return (
      vaerdier.map((vaerdi) => {
        col++;
        if (header) {
          return (
            <th key={col} className={className}>{komma(vaerdi.vaerdi)}
              <div className="header-periode">{vaerdi.start}</div>
              <div className="header-periode">{vaerdi.slut}</div>
            </th>
          );
        }

        let cname = className;
        if (highlight && vaerdi.vaerdi<0) {
          cname+= ' noegletal-color-negative';
        }
        return <td className={cname} key={col} >
          {komma(vaerdi.vaerdi.vaerdi)}
          {this._renderWarnings(vaerdi.vaerdi)}
          </td>
      })
    );
  }

  _renderWarnings(regnskabstal) {
    if (regnskabstal && regnskabstal.vaerdi && regnskabstal.metadata.kontroller.length>0) {
      let text = '';
      regnskabstal.metadata.kontroller.forEach ((kontrol) => {
          text+=kontrol.text+"\n";
        }
      );
      return(
        <span>&nbsp;<span className="fa fa-exclamation red" title={text} /></span>
      )
    }
  }
}

