import React from 'react';
import SectionHeader from '../common/sectionheader';

const DeltagerInfo = (props) => {

  const deltager = props.deltager;
  const navn = deltager.navn;

  const {
    adresselinie,
    bylinie,
    postnr
  } = deltager;

  let detail = adresselinie?adresselinie:'';
  detail += (postnr && postnr!=='0' && adresselinie)?', ':'';
  detail += (postnr && postnr!=='0')?bylinie:'';

  return (
    <div className="info-card">
      <SectionHeader iconClass="fa fa-user" label={navn} detail={detail} headerClass="vheader-orange" headerClassCircle="vheader-icon-circle-orange" labelClass="vheader-label-orange"/>
    </div>
  );
}

export default DeltagerInfo;
