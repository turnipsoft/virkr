import React from 'react';
import SectionHeader from '../common/sectionheader';

const VirksomhedsInfo = (props) => {

  const vdata = props.data.virksomhedMetadata;

  const navn = vdata.nyesteNavn.navn;

  const {
    adresselinie
  } = vdata.nyesteBeliggenhedsadresse;

  return (
    <div className="info-card">
      <SectionHeader iconClass="fa fa-building" label={navn} detail={adresselinie} headerClass="vheader-orange" headerClassCircle="vheader-icon-circle-orange" labelClass="vheader-label-orange"/>
    </div>
  );
}

export default VirksomhedsInfo;
