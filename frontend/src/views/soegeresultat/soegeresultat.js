import React, { Component } from 'react';
import Card from './soegeresultat-card';
import DeltagerCard from './soegeresultat-deltager-card';


export default class Soegeresultat extends Component {

  render() {

    const { soegning } = this.props;
    const { virksomheder, deltagere } = this.props.soegeresultat;

    if (virksomheder && virksomheder.length==0 && deltagere && deltagere.length==0) {
      return (
        <div className="topMargin">
          <br/>
          <div className="row">
            <div className="col-2" />
            <div className="col-8 text-center">
              Der blev ikke fundet noget resultat på søgningen : <b>{soegning}</b>
            </div>
            <div className="col-2" />
          </div>
        </div>
      )
    }

    return (
      <div className="top-margin">
        {this._renderVirksomheder(this.props.soegeresultat.virksomheder)}
        {this._renderPersoner(this.props.soegeresultat.deltagere)}
      </div>
    );
  }

  _renderVirksomheder(virksomheder) {
    return (virksomheder !== null && virksomheder.length>0) ?
      <div className="soegeresultat-virksomheder">
        <h3>Virksomheder</h3>
        {virksomheder.map((s) => {
          return (
            <Card virksomhed={s} key={s.cvrnr} onClick={this.props.visVirksomhed} />
          );
        })}
      </div> : null;
  }

  _renderPersoner(personer) {
    return (personer !== null && personer.length>0) ?
      <div className="soegeresultat-personer">
        <h3>Personer</h3>
        {personer.map((s) => {
          return (
            <DeltagerCard deltager={s} key={s.enhedsNummer} onClick={this.props.visDeltager} />
          );
        })}
      </div> : null;
  }
}
