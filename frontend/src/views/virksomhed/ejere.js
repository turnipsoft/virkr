import React, { Component } from 'react';
import Ejer from './ejer';
import SectionHeader from '../common/sectionheader';

export default class Ejere extends React.Component {

  render() {

    console.log(this.props);

    const {cvrdata, visEjerGraf, visVirksomhed, visDeltager} = this.props;

    const ejere = cvrdata.aktuelleEjere;
    const historiskeEjere = cvrdata.historiskeEjere;

    if (ejere && ejere.length>0) {
      return(
        <div>
          <div className="ejere">
            <SectionHeader iconClass="fa fa-sitemap white"
                           onClick={ () => visEjerGraf(cvrdata.cvrNummer, true)}
                           label="Legale Ejere" detail="Tryk for at se komplet ejergraf"/>

            <br/>
            {ejere.map((ejer) => {
              return (<Ejer key={ejer.enhedsnummer} ejer={ejer} visVirksomhed={visVirksomhed} visDeltager={visDeltager} />)
            })}
          </div>
          {this._renderHitoriskeEjere(historiskeEjere)}
        </div>

      )
    }

    return null;
  }

  _renderHitoriskeEjere(ejere) {
    if (ejere && ejere.length>0) {
      const { visVirksomhed, visDeltager} = this.props;

      return (
        <div className="ejere">
          <SectionHeader iconClass="fa fa-sitemap white"
                         label="Historisk Legale Ejere" detail="Ejere der tidligere har haft andel i virksomheden"/>
          <br/>
          {ejere.map((ejer) => {
            return (<Ejer key={ejer.enhedsnummer} ejer={ejer} visVirksomhed={visVirksomhed} visDeltager={visDeltager} />)
          })}
        </div>

      )
    }
  }


}
