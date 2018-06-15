import React, { Component } from 'react';
import Card from './soegeresultat-card';
import DeltagerCard from './soegeresultat-deltager-card';
import Navigation from './navigation';

export default class Soegeresultat extends Component {

  render() {

    const { soegning, soeg } = this.props;
    const { virksomhedSoegeresultat, deltagerSoegeresultat, meta } = this.props.soegeresultat;

    const virksomheder = virksomhedSoegeresultat.virksomheder;
    const deltagere = deltagerSoegeresultat.deltagere;

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
        <Navigation links={meta.links} antalVirksomheder={meta.virksomhedHits} antalDeltagere={meta.deltagerHits} soeg={soeg} soegning={soegning} />
        {this._renderVirksomheder(virksomheder)}
        {this._renderPersoner(deltagere)}
        <Navigation links={meta.links} antalVirksomheder={meta.virksomhedHits} antalDeltagere={meta.deltagerHits} soeg={soeg} soegning={soegning} />
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
