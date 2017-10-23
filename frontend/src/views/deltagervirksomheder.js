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
              <a href="#" title="Vis komplet ejergraf"><span className="fa fa-sitemap" onClick={ () => visDeltagerGraf(deltager.enhedsNummer)} /></a> &nbsp;Associerede Virksomheder <a href="#" title="Vis komplet ejergraf" onClick={ () => visDeltagerGraf(deltager.enhedsNummer)} >(Se komplet ejergraf)</a>
            </div>
          </div>
          <br/>
          {deltager.virksomheder.map((virksomhed) => {
            return (<DeltagerVirksomhed key={virksomhed.enhedsnummer} virksomhed={virksomhed} opdaterCvrNummer={this.props.opdaterCvrNummer} />)
          })}
        </div>

      )
    }

    return null;
  }
}
