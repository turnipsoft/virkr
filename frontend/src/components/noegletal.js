import React, { Component } from 'react';
export default class Noegletal extends React.Component {

  render() {
    if (this.props.noegletal) {
      let text = this.props.text;
      let tal = this.props.noegletal;

      if (this.props.negative) {
        tal = -tal;
      }

      tal = this._komma(tal);
      if (this.props.b) {
        text = <b>{text}</b>
        tal = <b>{tal}</b>
      }

      if (this.props.h) {
        text = <h4>{text}</h4>
        tal = <h4>{tal}</h4>
      }

      let bottomClass = "col col-4";
      if (this.props.underline) {
        bottomClass += " bottom";
      }

      return (
        <div className="row">
          <div className="col col-8">
            {text}
          </div>
          <div className={bottomClass}>
            <span className="pull-right">{tal} </span>
          </div>
        </div>
      );

    }

    return(null);
  }

  _komma(vaerdi) {
    if (vaerdi) {
      const v = vaerdi.toLocaleString();
      return v.replace(/,/g, ".");
    }

    return vaerdi;
  }
}
