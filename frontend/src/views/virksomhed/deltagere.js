import React, { Component } from 'react';
import Deltager from './deltager';
import SectionHeader from '../common/sectionheader';

export default class Ejere extends React.Component {

  constructor(props) {
    super(props);
    this.state = {visLedelse: true};
  }

  render() {

    if (!this.state.visLedelse) {
      return (
        <div>
          <SectionHeader label="Ledelse" detail="Oplysninger om ledelsen" iconClass="fa fa-sitemap white"
                         onClick={() => this.setState({visLedelse: !this.state.visLedelse})} />
          <br/>
        </div>
      );
    }

    const {cvrdata, visDeltager} = this.props;

    const deltagere = cvrdata.deltagerRelation;

    if (deltagere && deltagere.length>0) {
      return(
        <div>
          <div className="deltagere">
            <SectionHeader label="Ledelse" detail="Oplysninger om ledelsen" iconClass="fa fa-sitemap white"
                           onClick={() => this.setState({visLedelse: !this.state.visLedelse})} />
            <br/>
            {deltagere.map((deltager) => {
              if (deltager.ledelse) {
                return (<Deltager key={deltager.deltager.enhedsNummer} deltager={deltager} visDeltager={visDeltager}/>)
              } else {
                return null
              }
            })}
          </div>
          <br/>
        </div>
      )
    } else {
      return (<div>Der er ikke registreret nogen aktuel ledelse på virksomheden</div>)
    }

    return null;
  }

}
