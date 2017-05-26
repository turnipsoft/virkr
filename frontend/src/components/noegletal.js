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

      let bottomClass = "col-xs-2";
      if (this.props.underline) {
        bottomClass += " bottom";
      }

      return (
        <div className="row">
          <div className="col-xs-2"></div>
          <div className="col-xs-6">
            {text}
          </div>
          <div className={bottomClass}>
            <u><span className="pull-right">{tal} </span></u>
          </div>
          <div className="col-xs-2"></div>
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
