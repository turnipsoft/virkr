import React, { Component } from 'react';
import DeltagerVirksomhed from './deltagervirksomhed';

export default class DeltagerVirksomheder extends Component {

  render() {
    const {deltager, visDeltagerGraf} = this.props;

    if (deltager.virksomheder && deltager.virksomheder.length>0) {
      return(

        <div className="virksomheder">
          <div className="row">
            <div className="col-12 section-header">
              <span className="btn-link fa fa-sitemap" onClick={ () => visDeltagerGraf(deltager.enhedsNummer, true)} />
              &nbsp;Associerede Virksomheder
              <span className="btn-link" onClick={ () => visDeltagerGraf(deltager.enhedsNummer, true)} >(Se komplet ejergraf)</span>
            </div>
          </div>
          <br/>
          {deltager.virksomheder.map((virksomhed) => {
            return (<DeltagerVirksomhed key={virksomhed.enhedsNummer} virksomhed={virksomhed} onClick={this.props.onVirksomhedClick} />)
          })}
        </div>

      )
    }

    return null;
  }
}
