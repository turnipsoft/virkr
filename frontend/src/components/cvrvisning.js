import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber, spinner, cvrdata } = this.props;

    if (spinner) {
      return (
        <div className="virksomhed-spinner">

          <span className="fa fa-spinner fa-spin fa-3x fa-fw"></span>
          <span className="sr-only">Loading...</span>
        </div>
      )
    }

    const renderedRegnskaber = regnskaber.length>0 ? this._renderRegnskaber(regnskaber) : this._renderIngenRegnskaber();

    return (
      <div className="virksomhedsdata">
        <VirksomhedsInfo data={cvrdata}/>
        {renderedRegnskaber}
        <VirksomhedsDetaljer cvrdata={cvrdata}/>
        <br/>

      </div>
    );
  }

  _renderIngenRegnskaber() {
    return (
      <div className="alert alert-warning">
        Der er ikke registreret nogen digitale regnskaber for denne virksomhed
      </div>
    )
  }

  _renderRegnskaber(regnskaber) {
    return (
      <div>
      <Graf regnskaber={regnskaber} />
        <br />
        <br />
        <h5>Regnskaber</h5>
        {regnskaber.slice().reverse().map((regnskab) => {
        return <Regnskabstal key={regnskab.id} regnskab={regnskab} />
        })}
      </div>
    );
  }

}
