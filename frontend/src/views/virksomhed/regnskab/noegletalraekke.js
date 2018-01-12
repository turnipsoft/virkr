import React, { Component } from 'react';
import { resolveJsonValue, komma } from '../../../utils/utils';

export default class NoegletalRaekke extends Component {

  render() {
    const { regnskaber, label, felt, style, header, negative, highlight, onClick, skat, inkluderRegnksabsklasse, sz } = this.props;

    const feltvaerdier = regnskaber.map((regnskab) => {
      const aktueltRegnskab = regnskab.aktueltAarsregnskab;

      let vaerdi = resolveJsonValue(felt, aktueltRegnskab);
      if (header) {

        let regnskabsklasse = aktueltRegnskab.regnskabsklasse;
        if (regnskabsklasse && regnskabsklasse.indexOf(',')!==-1) {
          regnskabsklasse = regnskabsklasse.substring(0, regnskabsklasse.indexOf(','));
        }

        return { vaerdi: vaerdi, start: aktueltRegnskab.startdato, slut: aktueltRegnskab.slutdato, regnskabsklasse:
          inkluderRegnksabsklasse? regnskabsklasse: null, omgoerelse: regnskab.omgoerelse, antal: regnskab.antalRegnskaber };
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
            <th width="20%">
              {label}
            </th>
            {this._renderVaerdier(feltvaerdier, header, sz)}
          </tr>
        </thead>
      );
    }

    return (
      <tr className="noegletal-row" onClick={onClick} >
        <td width="20%" className={style?style:''}>
          {label}
        </td>
        {this._renderVaerdier(feltvaerdier, header, sz, highlight, style)}
      </tr>
    );
  }

  _renderVaerdier(vaerdier, header, sz, highlight, style) {

    const className='noegletal-vaerdi '+style;

    let col = 0;
    const szp = `${sz}%`;

    return (
      vaerdier.map((vaerdi) => {
        col++;
        if (!vaerdi.vaerdi) {
          return <td key={col} width={szp} ></td>;
        }

        if (header) {
          const startaar = vaerdi.start.substring(0,4);
          const slutaar = vaerdi.slut.substring(0,4);

          const aar = (startaar!=slutaar) ? `${startaar}/${slutaar}` : slutaar
          const title = `${vaerdi.antal} regnskaber registreret`;
          const exclamation = vaerdi.antal>1? <span title={title} className="fa fa-exclamation fa-lg red"/> : ''
          return (
            <th width={szp} key={col} className={className}>{aar}{exclamation}
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
          const id = 'id_'+Math.abs(vaerdi.vaerdi.vaerdi);
          console.log(vaerdi);

          return (<td width={szp} className={cname} key={col} >
                    <span className="clickable" title={warningText} onClick={(e) => this._showPopup(id,e)}>{komma(vaerdi.vaerdi.vaerdi)}<span>&nbsp;<span onClick={(e) => this._showPopup(id,e)} className="clickable fa fa-exclamation fa-lg red" title={warningText} /></span></span>
                    <div id={id} className="overlay">
                      <div className="popup">
                        <h2>Afvigelse i nøgletal</h2>
                        <span className="close" onClick={() => this._hidePopup(id)}>&times;</span>
                        <div className="content">
                          <br/>
                          {warningText}
                        </div>
                      </div>
                    </div>
                  </td>)
        }

        return <td width={szp} className={cname} key={col} >
          {komma(vaerdi.vaerdi.vaerdi)}
          </td>
      })
    );
  }

  _showPopup(elementId, e) {
    e.preventDefault();
    let elem = document.getElementById(elementId);
    elem.style.visibility = 'visible';
    elem.style.opacity = 1;
  }

  _hidePopup(elementId) {
    let elem = document.getElementById(elementId);
    elem.style.visibility = 'hidden';
    elem.style.opacity = 0;
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

