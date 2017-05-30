import React, { Component } from 'react';

export default class CvrSoegebox extends Component {

  constructor(props) {
    super(props)
    this.state = { cvrnummer: '', fejl: false };

    this.changeCvrNummer = this.changeCvrNummer.bind(this);
    this.submitCvrNummer = this.submitCvrNummer.bind(this);
  }

  changeCvrNummer(event) {
    this.setState({ cvrnummer: event.target.value });
  }

  submitCvrNummer(e) {
    e.preventDefault();

    const { cvrnummer } = this.state;

    const harFejl = (!Number.isInteger(parseInt(cvrnummer)) || cvrnummer.length != 8);

    if (harFejl) {
      this.setState({ fejl: true });
    } else {
      this.setState({ fejl: false }, this.props.opdaterCvr(cvrnummer));
    }
  }

  render() {
    return (
      <div className="cvr-input">
        <div className="row">
          <div className="col col-4 offset-4">
            <form onSubmit={this.submitCvrNummer} className={this.state.fejl ? 'has-warning' : null}>
              <div className="input-group">
                <input type="text"
                  value={this.state.cvrnummer}
                  onChange={this.changeCvrNummer}
                  className="form-control"
                  placeholder="Indtast CVR-Nummer"
                  maxLength="8"
                  id="txtSearch" />

                {this.state.fejl ? <div className="form-control-feedback">Et CVR nummer skal bestå af 8 cifre</div> : null}

                <div className="input-group-btn">
                  <button className="btn btn-primary" type="submit">
                    <span className="fa fa-search"></span>
                  </button>
                </div>
              </div>
            </form>
          </div>
          <div className="col col-4" />

        </div>
      </div>);
  }

}
