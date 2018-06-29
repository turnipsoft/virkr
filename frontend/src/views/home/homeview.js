import React from 'react';
import PageHeader from '../common/pageheader';

import CvrSoegebox from './cvrsoegebox';

const HomeView = () => {
  return (
    <div>
      <PageHeader iconClassName="fa fa-search" headerText="Søg" detailText="Søg på virksomheder og personer, indtast navn eller CVR-Nummer" />
      <div className="row">
        <div className="col col-sm-6 offset-sm-3 justify-content-center top-margin">
          <CvrSoegebox />
        </div>
      </div>
    </div>
  );
}

export default HomeView;
