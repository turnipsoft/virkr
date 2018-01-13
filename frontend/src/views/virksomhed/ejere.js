import React, { Component } from 'react';
import Ejer from './ejer';
import SectionHeader from '../common/sectionheader';

export default class Ejere extends React.Component {

  constructor(props) {
    super(props);
    this.state = { visHistoriskeEjere: true };
  }

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
                           label="Legale Ejere" detail="Tryk for at se den komplette ejergraf for alle legale ejere"/>

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

      if (!this.state.visHistoriskeEjere) {
        return (
          <div>
            <SectionHeader iconClass="fa fa-sitemap white" onClick={() => this.setState({visHistoriskeEjere: !this.state.visHistoriskeEjere})}
                           label="Historisk Legale Ejere" detail="Ejere der tidligere har haft andel i virksomheden"/>
            <br/>
          </div>
        )
      }

      return (
        <div className="ejere">
          <SectionHeader iconClass="fa fa-sitemap white" onClick={() => this.setState({visHistoriskeEjere: !this.state.visHistoriskeEjere})}
                         label="Historisk Legale Ejere" detail="Ejere der tidligere har haft andel i virksomheden"/>
          <br/>
          <div className="historiske-ejere">
            {ejere.map((ejer) => {
              return (<Ejer key={ejer.enhedsnummer} ejer={ejer} visVirksomhed={visVirksomhed} visDeltager={visDeltager} />)
            })}
          </div>
        </div>

      )
    }
  }


}
