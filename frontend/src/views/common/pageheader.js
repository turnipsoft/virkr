import React from 'react';

const PageHeader = ({ headerText }) => {
  return(
    <div className="menuBar">
      <div className="row">
        <div className="col-12 section-header">
          {headerText?headerText:null}
        </div>
      </div>
    </div>
  )
};

export default PageHeader;
