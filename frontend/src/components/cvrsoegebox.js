import React, { Component } from 'react';

export default class CvrSoegebox extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    return (<div className="cvr-input">
      <div className="row">
        <div className="col-xs-4"/>
        <div className="col-xs-4">
          <form onSubmit={this._opdaterCvrNummer.bind(this)}>
            <div className="input-group">
              <input type="text" ref={c => this._cvr = c}
                     className="form-control" placeholder="Indtast CVR-Nummer"
                     maxLength="8" id="txtSearch" />
              <div className="input-group-btn">
                <button className="btn btn-primary" type="submit">
                  <span className="fa fa-search"></span>
                </button>
              </div>
            </div>
          </form>
        </div>
        <div className="col-xs-4" />

      </div>
    </div>);
  }

  _opdaterCvrNummer(e) {
    e.preventDefault();
    const iv = parseInt(this._cvr.value);
    if (!Number.isInteger(iv)) {
      alert(this._cvr.value + " er ikke et CVR-Nummer")
      return;
    }
    if (this._cvr.value && this._cvr.value.length != 8) {
      alert("Et CVR-Nummer består altid af 8 cifre");
      return;
    }
    this.props.opdaterCvr(this._cvr.value);
  }
}
