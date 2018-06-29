import React, { Component } from 'react';
import Spinner from '../common/spinner';
import api from '../../utils/apihelper';
import PageHeader from '../common/pageheader';
import SectionHeader from '../common/sectionheader';

export default class DictionaryView extends Component {

  constructor(props) {
    super(props)
    this.state = {
      dictionary: null, fejl: null
    }
  }

  componentWillMount() {
    api.hentDictionary().then((response)=> this.setState({dictionary:response})).catch((error)=>this.setState({fejl: error}));
  }

  render() {
    if (this.state.dictionary!==null) {
      console.log(this.state.dictionary)
      return (
        <div>
          <PageHeader iconClassName="fa fa-user" headerText="Opslagsværk" context="dictionary" />
          {this.state.fejl!==null && <div className="alert alert-danger alert-margin-top" role="alert">Der er opstået en fejl under hentning af opslagsværket. {this.state.error}</div>}
          <br/>
          <div className="row">
            <div className="col">
              <SectionHeader iconClass="fa fa-book" label="XBRL Opslagsværk" detail="Her kan du se alle de XBRL elementer som virkr understøtter" headerClass="vheader-orange"
                             headerClassCircle="vheader-icon-circle-orange" labelClass="vheader-label-orange"/>
            </div>
          </div>
          <br/>
          <div className="row">
            <div className="col">
              <h4>Resultatopgørelse</h4>
              <div className="dictionary-table">
              {this._renderXbrlList(this.state.dictionary.resultatopgoerelse)}
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col">
              <h4>Balance - Aktiver</h4>
              <div className="dictionary-table">
                {this._renderXbrlList(this.state.dictionary.aktiver)}
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col">
              <h4>Balance - Passiver</h4>
              <div className="dictionary-table">
                {this._renderXbrlList(this.state.dictionary.passiver)}
              </div>
            </div>
          </div>
          <br/>
          <div className="row">
            <div className="col">
              <SectionHeader iconClass="fa fa-book" label="Regnskabsfelt Opslagsværk" detail="Her kan du se alle de felter fra regnskaber som virkr understøtter" headerClass="vheader-orange"
                             headerClassCircle="vheader-icon-circle-orange" labelClass="vheader-label-orange"/>
            </div>
          </div>
          <br/>
          <div className="row">
            <div className="col">
              <h4>Resultatopgørelse</h4>
              <div className="dictionary-table">
                {this._renderFeltList(this.state.dictionary.resultatopgoerelse)}
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col">
              <h4>Balance - Aktiver</h4>
              <div className="dictionary-table">
                {this._renderFeltList(this.state.dictionary.aktiver)}
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col">
              <h4>Balance - Passiver</h4>
              <div className="dictionary-table">
                {this._renderFeltList(this.state.dictionary.passiver)}
              </div>
            </div>
          </div>
        </div>
      )
    } else {
      return <Spinner />
    }
  }

  _renderXbrlList(list) {
    const sorted = list.sort((a,b)=>{
      if (a.xbrlElementNames>b.xbrlElementNames) { return 1 }
      if (a.xbrlElementNames<b.xbrlElementNames) { return -1 }
      return 0;
    })
    return (
      <table className="table table-striped table-responsive">
        <thead>
          <tr><th>XBRL Element</th>
          <th>Felt i regnskab</th></tr>
        </thead>
        <tbody>
        {sorted.map((element)=>{
          return (
            <tr key={element.xbrlElementNames}>
              <td >
                {element.xbrlElementNames}
              </td>
              <td className="dtd-left">
                {element.translatedDanish}
              </td>
            </tr>
          )
        })}
        </tbody>
      </table>
    )
  }

  _renderFeltList(list) {
    const sorted = list.sort((a,b)=>{
      if (a.translatedDanish>b.translatedDanish) { return 1 }
      if (a.translatedDanish<b.translatedDanish) { return -1 }
      return 0;
    })
    return (
      <table className="table table-striped table-responsive">
        <thead>
        <tr><th>Felt i regnskab</th><th>XBRL Element</th></tr>
        </thead>
        <tbody>
        {sorted.map((element)=>{
          return (
            <tr key={element.xbrlElementNames}>
              <td >
                {element.translatedDanish}
              </td>
              <td className="dtd-left" >
                {element.xbrlElementNames}
              </td>
            </tr>
          )
        })}
        </tbody>
      </table>
    )
  }
}
