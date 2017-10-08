import React, { Component } from 'react';
import DeltagerVirksomhed from './deltagervirksomhed';

export default class DeltagerVirksomheder extends Component {

  render() {
    const {virksomheder} = this.props;

    if (virksomheder && virksomheder.length>0) {
      return(

        <div className="virksomheder">
          <div className="row">
            <div className="col-12 section-header">
              Associerede virksomheder
            </div>
          </div>
          <br/>
          {virksomheder.map((virksomhed) => {
            return (<DeltagerVirksomhed key={virksomhed.enhedsnummer} virksomhed={virksomhed} opdaterCvrNummer={this.props.opdaterCvrNummer} />)
          })}
        </div>

      )
    }

    return null;
  }
}
