import React, { Component } from 'react';
import Ejer from './ejer';

export default class Ejere extends React.Component {

  render() {
    const ejere = this.props.cvrdata.ejere;

    if (ejere && ejere.length>0) {
      return(
        <div className="ejere">
          <div className="row">
            <div className="col-12"><h2>Ejere</h2></div>
          </div>
          {ejere.map((ejer) => {
            return (<Ejer key={ejer.forretningsnoegle} ejer={ejer} opdaterCvrNummer={this.props.opdaterCvrNummer} />)
          })}
        </div>
      )
    }

    return null;
  }

}
