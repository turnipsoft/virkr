import React, { Component } from 'react';
import { resolveJsonValue, komma } from '../../../utils/utils';

export default class NoegletalRaekke extends Component {

  render() {
    const { regnskaber, label, felt, style, header, negative, highlight, onClick, skat, inkluderRegnksabsklasse } = this.props;

    const feltvaerdier = regnskaber.map((regnskab) => {
      const aktueltRegnskab = regnskab.aktueltAarsregnskab;

      let vaerdi = resolveJsonValue(felt, aktueltRegnskab);
      if (header) {

        let regnskabsklasse = aktueltRegnskab.regnskabsklasse;
        if (regnskabsklasse && regnskabsklasse.indexOf(',')!==-1) {
          regnskabsklasse = regnskabsklasse.substring(0, regnskabsklasse.indexOf(','));
        }

        return { vaerdi: vaerdi, start: aktueltRegnskab.startdato, slut: aktueltRegnskab.slutdato, regnskabsklasse:
          inkluderRegnksabsklasse? regnskabsklasse: null };
      }

      // skat er lidt specielt så det hackes ind her, hved ikke hvordan vi ellers skal håndterer, pointen er jo at man kan have tilgodehavende hos skat
      if (!skat) {
        if (negative && vaerdi && vaerdi.vaerdi>0) {
          vaerdi.vaerdi = vaerdi.vaerdi * -1;
        }
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
        if (!vaerdi.vaerdi) {
          return <td key={col}></td>;
        }

        if (header) {
          const startaar = vaerdi.start.substring(0,4);
          const slutaar = vaerdi.slut.substring(0,4);

          const aar = (startaar!=slutaar) ? `${startaar}/${slutaar}` : slutaar
          return (
            <th key={col} className={className}>{aar}
              <div className="header-periode">{vaerdi.start}</div>
              <div className="header-periode">{vaerdi.slut}</div>
              {vaerdi.regnskabsklasse && <div className="header-periode">{vaerdi.regnskabsklasse}</div>}
            </th>
          );
        }

        let cname = className;
        if (highlight && vaerdi.vaerdi.vaerdi<0) {
          cname+= ' noegletal-color-negative';
        }

        const warningText = this._warningText(vaerdi.vaerdi);

        if (warningText) {
          return (<td className={cname} key={col} >
                    <span title={warningText} >{komma(vaerdi.vaerdi.vaerdi)}<span>&nbsp;<span className="fa fa-exclamation fa-lg red" title={warningText} /></span></span>
                 </td>)
        }

        return <td className={cname} key={col} >
          {komma(vaerdi.vaerdi.vaerdi)}
          </td>
      })
    );
  }

  _warningText(regnskabstal) {
    if (regnskabstal && regnskabstal.vaerdi && regnskabstal.metadata.kontroller.length>0) {
      let text = '';
      regnskabstal.metadata.kontroller.forEach((kontrol) => {
          text += kontrol.text + "\n";
        }
      );
      return text;
    }

    return null;
  }
}

