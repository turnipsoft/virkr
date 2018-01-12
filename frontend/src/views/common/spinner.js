import React from 'react';

const Spinner = () => {
  return (
    <div className="virksomhed-spinner">
      <br/>
      <br/>
      <span className="fa fa-spinner fa-spin fa-3x fa-fw orange"></span>
      <span className="sr-only">Loading...</span>
    </div>
  );
}

export default Spinner;
