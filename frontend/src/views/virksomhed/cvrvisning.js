import React, { Component } from 'react';
import Regnskabstal from './regnskab/regnskabstal';
import NoegletalTabel from './regnskab/noegletaltabel';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';
import Ejere from './ejere';
import Graf from './regnskab/graf';

export default class CvrVisning extends Component {

  render() {
    const { regnskaber, cvrdata } = this.props;

    return (
      <div className="top-margin">
        {this._renderVirksomhedsInfo(cvrdata)}
        {this._renderRegnskaber(regnskaber.regnskabsdata)}
        {this._renderEjere(cvrdata)}
        {this._renderCvrData(cvrdata)}
        <br />
      </div>
    )
  }

  _renderVirksomhedsInfo(cvrdata) {
    return (cvrdata !== null) ? <VirksomhedsInfo data={cvrdata} /> : null
  }

  _renderEjere(cvrdata) {
    return (cvrdata!==null && cvrdata.ejere!==null) ? <Ejere cvrdata={cvrdata} visVirksomhed={this.props.visVirksomhed}
                                                             visDeltager={this.props.visDeltager}
                                                             visEjerGraf={this.props.visEjerGraf} /> : null
  }

  _renderCvrData(cvrdata) {
    return (cvrdata !== null) ? <VirksomhedsDetaljer cvrdata={cvrdata} /> : null
  }

  _renderRegnskaber(regnskaber) {
    if (regnskaber === null) {
      return null;
    }

    const sorteredeRegnskaber = regnskaber.slice().reverse();

    if (regnskaber.length > 0) {
      return (
        <div>
          <div className="row">
            <div className="col-12 section-header">
              <span className="fa fa-file-pdf-o" /> &nbsp; Regnskaber
            </div>
          </div>

          <br/>
          <Graf regnskaber={regnskaber} />
          <br />
          <div className="hidden-sm-down">
            <NoegletalTabel regnskaber={sorteredeRegnskaber} />
          </div>
          <br />
          {sorteredeRegnskaber.map((regnskab) => {
            return <Regnskabstal key={regnskab.id} regnskab={regnskab} />
          })}
          <br/>


        </div>
      );
    } else {
      return (
        <div className="alert alert-warning">
          Der er ikke registreret nogen digitale regnskaber for denne virksomhed
      </div>
      )
    }
  }

}

