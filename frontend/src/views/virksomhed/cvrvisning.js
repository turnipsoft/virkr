import React, { Component } from 'react';
import Regnskabstal from './regnskab/regnskabstal';
import NoegletalTabel from './regnskab/noegletaltabel';
import VirksomhedsInfo from './virksomhedsinfo';
import VirksomhedsDetaljer from './virksomhedsdetaljer';
import Ejere from './ejere';
import Graf from './regnskab/graf';
import SectionHeader from '../common/sectionheader';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
    this.state = {visGraf: false};
  }

  render() {
    const { regnskaber, cvrdata } = this.props;

    return (
      <div className="top-margin">
        {this._renderVirksomhedsInfo(cvrdata)}
        <br/>
        {this._renderRegnskaber(regnskaber.regnskabsdata, cvrdata.alleRevisorer)}
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

  _renderNoegletalHeader() {
    const viserGraf = this.state.visGraf;

    const icon = viserGraf? 'fa-minus' : 'fa-line-chart';
    const className = 'fa '+icon;
    const detail = viserGraf? 'Tryk for at folde grafen ind' : 'Tryk for at folde grafen ud';

    return (
      <SectionHeader iconClass={className} onClick={ () => this.setState({visGraf: !viserGraf})}
       label="Nøgletalsgraf" detail={detail} />
    );
  }

  _renderRegnskaber(regnskaber, alleRevisorer) {
    if (regnskaber === null) {
      return null;
    }

    const sorteredeRegnskaber = regnskaber.slice().reverse();

    if (regnskaber.length > 0) {
      const start = regnskaber[0].aar;
      const slut = regnskaber[regnskaber.length-1].aar;

      const detail = "Virksomhedens indberettede digitale regnskaber for perioden "+ start+" - "+slut;
      return (
        <div>
          <SectionHeader label="Regnskaber" detail={detail} iconClass="fa fa-file-pdf-o white"/>
          <br />
          <div className="hide-on-portrait">
            <NoegletalTabel regnskaber={sorteredeRegnskaber} revisorer={alleRevisorer}/>
          </div>

          <br/>
          {this._renderNoegletalHeader()}
          {this.state.visGraf && <Graf regnskaber={regnskaber} />}
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

