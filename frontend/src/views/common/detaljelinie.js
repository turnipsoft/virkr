import React, { Component } from 'react';

export default class DetaljeLinie extends Component {

  render() {

    const detalje = this.props.detalje? " ("+this.props.detalje.toLowerCase()+")" : ""

    var val;

    if (this.props.href) {
      val = <a href={this.props.href} target="_blank">{this.props.value}</a>
    } else if (this.props.link) {
      val = <span className="btn-link" onClick={ () => this.props.link(this.props.linkKey, true)}>{this.props.value}</span>
    } else if (this.props.custom) {
      val = this.props.custom;
    }
    else {
      val = this.props.value;
    }

    if (this.props.value || this.props.custom) {
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
