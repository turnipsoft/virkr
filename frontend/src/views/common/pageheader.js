import React from 'react';

const PageHeader = ({ headerText, iconClassName }) => {

  return(
    <div className="menuBar">
      <div className="row">
        <div className="col-12 section-header">
          <span className={iconClassName} /> &nbsp; {headerText?headerText:null}
        </div>
      </div>
    </div>
  )
};

export default PageHeader;
