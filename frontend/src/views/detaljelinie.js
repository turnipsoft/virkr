import React, { Component } from 'react';

export default class DetaljeLinie extends Component {

  render() {

    const detalje = this.props.detalje? " ("+this.props.detalje.toLowerCase()+")" : ""

    if (this.props.value) {
      return (
        <div className="row">
          <div className="col col-4">
            {this.props.text}:
          </div>
          <div className="col col-8">
            {this.props.value} <i>{detalje}</i>
          </div>
        </div> );
    }

    return(null);
  }
}
