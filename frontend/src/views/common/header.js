import React from 'react';

const Header = () => {
  return(
    <div>
      <div className="hidden-sm-up virkr-header">
        <div className="row">
          <div className="col">
            <p>Virkr - Regnskaber, nøgletal, ejerforhold</p>
          </div>
        </div>
      </div>

      <div className="hidden-xs-down virkr-header">
        <div className="row">
          <div className="col">
            <h1><a href="">Virkr</a></h1>
            <p>Regnskaber, nøgletal, ejerforhold</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Header;
