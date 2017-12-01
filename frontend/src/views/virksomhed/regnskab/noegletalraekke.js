import React, { Component } from 'react';
import { resolveJsonValue, komma } from '../../../utils/utils';

export default class NoegletalRaekke extends Component {

  render() {
    const { regnskaber, label, felt, style, header, negative, highlight, onClick } = this.props;

    const feltvaerdier = regnskaber.map((regnskab) => {
      let vaerdi = resolveJsonValue(felt, regnskab);
      if (header) return vaerdi;
      if (negative && vaerdi>0) {
        vaerdi = vaerdi * -1;
      }
      return komma(vaerdi);
    });

    const empty = feltvaerdier.every((i) => { return (i === null || i === undefined) });
    if (empty) {
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
        {this._renderVaerdier(feltvaerdier, header, highlight)}
      </tr>
    );
  }

  _renderVaerdier(vaerdier, header, highlight) {

    const className='noegletal-vaerdi';

    let col = 0;

    return (
      vaerdier.map((vaerdi) => {
        col++;
        if (header) {
          return (
            <th key={col} className={className}>{vaerdi}</th>
          );
        }

        let cname = className;
        if (highlight && vaerdi<0) {
          cname+= ' noegletal-color-negative';
        }
        return <td className={cname} key={col} >{vaerdi}</td>
      })
    );
  }

}

