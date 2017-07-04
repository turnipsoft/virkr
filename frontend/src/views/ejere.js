import React, { Component } from 'react';
import Ejer from './ejer';

export default class Ejere extends React.Component {

  render() {
    const {cvrdata, visEjerGraf} = this.props;

    const ejere = cvrdata.ejere;

    if (ejere && ejere.length>0) {
      return(

        <div className="ejere">
          <div className="row">
            <div className="col-12 section-header">
              <a href="#" title="Vis komplet ejergraf"><span className="fa fa-sitemap" onClick={ () => visEjerGraf(cvrdata.cvrNummer)} /></a> &nbsp; Ejere
            </div>
          </div>
          <br/>
          {ejere.map((ejer) => {
            return (<Ejer key={ejer.enhedsnummer} ejer={ejer} opdaterCvrNummer={this.props.opdaterCvrNummer} />)
          })}
        </div>

      )
    }

    return null;
  }

}
