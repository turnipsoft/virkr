import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import Graf from './graf';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const { regnskaber } = this.props;

    if (regnskaber && regnskaber.length>0) {
      const virksomhedsdata = regnskaber[regnskaber.length-1].virksomhedsdata;

      return (

        <div className="virksomhedsdata">
          <h5>Nøgletal for {virksomhedsdata.navn}</h5>
          <br/>
          {(regnskaber.length > 0) ? <Graf regnskaber={regnskaber}/> : null}
          <br />
          <br />
          <h5>Regnskaber</h5>
          {regnskaber.slice().reverse().map((regnskab) => {
            return <Regnskabstal key={regnskab.id} regnskab={regnskab}/>
          })}
        </div>);
    }

    return (null);
  }

}
