import React from 'react';
import Card from './soegeresultat-card';

const Soegeresultat = (props) => {
  return (
    <div className="top-margin">
      {props.soegeresultat.map((s) => {
        return (
          <Card virksomhed={s} callback={props.opdaterCvrNummer} key={s.cvrNummer} />
        );
      })}
    </div>
  );
}

export default Soegeresultat;
