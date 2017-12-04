import React from 'react';
import VaerdiGraf from './vaerdigraf';

const NoegletalGraf = ({regnskaber, felt, label}) => {

  const reversed = regnskaber.slice().reverse();

  return (
    <tr>
      <td colSpan={regnskaber.length+1}>
        <div className="row">
          <div className="col col-2 hidden-sm-down" />
          <div className="col col-8">
            <VaerdiGraf regnskaber={reversed} felt={felt} label={label} />
          </div>
          <div className="col col-2" />
        </div>
      </td>
    </tr>
  )
}

export default NoegletalGraf;
