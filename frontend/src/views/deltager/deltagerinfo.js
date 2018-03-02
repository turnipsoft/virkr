import React, { Component } from 'react';
import SectionHeader from '../common/sectionheader';


export default class DeltagerInfo extends Component {

  constructor(props) {
    super(props)
    this.state = { visHistoriskeAdresser: true }
  }

  render() {
    const deltager = this.props.deltager;
    const navn = deltager.navn;

    const {
      adresselinie,
      bylinie,
      postnr
    } = deltager;

    let detail = adresselinie ? adresselinie : '';
    detail += (postnr && postnr !== '0' && adresselinie) ? ', ' : '';
    detail += (postnr && postnr !== '0') ? bylinie : '';

    return (
      <div className="info-card margin-bottom">
        <SectionHeader iconClass="fa fa-user" label={navn} detail={detail} headerClass="vheader-orange"
                       headerClassCircle="vheader-icon-circle-orange" labelClass="vheader-label-orange"/>
        {this._renderDeltagerAdresser(deltager)}
      </div>
    );
  }

  _renderDeltagerAdresser(deltager) {
    if (deltager.adresser && deltager.adresser.length!==1) {
      const detail = deltager.navn + "'s tidligere adresser"
      const adresser = deltager.adresser.slice(0,-1).reverse()
      return (
        <div>
          <br/>
          <SectionHeader iconClass="fa fa-user" label="Tidligere adresser" detail={detail}
                         onClick={()=> this.setState({visHistoriskeAdresser: !this.state.visHistoriskeAdresser}) }/>
          {this.state.visHistoriskeAdresser && this._renderAdresser(adresser)}
        </div>
      )
    }
  }

  _renderAdresser(adresser) {
    return(
      <div> <br/>
      <div className="card adressercard">
        <div className="card-block resizable-block">
        {adresser.map((adr) => {
          return (
            <div key={adr.adresselinie} className="row">
              <div className="col-4">{adr.periode.gyldigFra} - {adr.periode.gyldigTil} </div>
              <div className="col-8">{adr.adresselinie}</div>
            </div>
          )
        })}
        </div>
      </div>
      </div>
    )
  }
}

