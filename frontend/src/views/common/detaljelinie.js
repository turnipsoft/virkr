import React, { Component } from 'react';

export default class DetaljeLinie extends Component {

  render() {

    const detalje = this.props.detalje? " ("+this.props.detalje.toLowerCase()+")" : ""

    var val;

    if (this.props.link) {
      val = <a href="#" onClick={ () => this.props.link(this.props.linkKey)}>{this.props.value}</a>
    } else {
      val = this.props.value;
    }

    if (this.props.value) {
      return (
        <div className="row">
          <div className="col col-4">
            {this.props.text}:
          </div>
          <div className="col col-8">
            {val}
            <i>{detalje}</i>
          </div>
        </div> );
    }

    return(null);
  }
}
