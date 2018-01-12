import React, { Component } from 'react';
import DeltagerVirksomhed from './deltagervirksomhed';
import SectionHeader from '../common/sectionheader';

export default class DeltagerVirksomheder extends Component {

  render() {
    const {deltager, visDeltagerGraf} = this.props;

    if (deltager.virksomheder && deltager.virksomheder.length>0) {
      return(

        <div className="virksomheder">
          <SectionHeader onClick={ () => visDeltagerGraf(deltager.enhedsNummer, true)}
                         label="Associerede Virksomheder" detail="Tryk for at se komplet graf over associerede virksomheder"
                         iconClass="fa fa-sitemap" />
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
