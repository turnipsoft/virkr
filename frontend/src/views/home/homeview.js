import React from 'react';
import PageHeader from '../common/pageheader';

import CvrSoegebox from './cvrsoegebox';

const HomeView = () => {
  return (
    <div>
      <PageHeader headerText="Home" />
      <div className="row">
        <div className="col col-sm-6 offset-sm-3 justify-content-center top-margin">
          <CvrSoegebox />
        </div>
      </div>
    </div>
  );
}

export default HomeView;
