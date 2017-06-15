import React, { Component } from 'react';

export default class CvrSoegebox extends Component {

  constructor(props) {
    super(props)
    this.state = { soegning: '', fejl: false };

    this.changeSoegning = this.changeSoegning.bind(this);
    this.submitSoegning = this.submitSoegning.bind(this);
  }

  changeSoegning(event) {
    this.setState({ soegning: event.target.value });
  }

  submitSoegning(e) {
    e.preventDefault();

    const { soegning } = this.state;

    this.setState({ fejl: false }, this.props.opdaterCvr(soegning));
  }

  render() {
    return (
      <div className="cvr-input">
        <div className="row">
          <div className="col col-6 offset-3">
            <form onSubmit={this.submitSoegning} className={this.state.fejl ? 'has-warning' : null}>
              <div className="input-group">
                <input type="text"
                  value={this.state.cvrnummer}
                  onChange={this.changeSoegning}
                  className="form-control"
                  placeholder="CVR-Nr eller Navn"
                  maxLength="100"
                  id="txtSearch" />
                <div className="input-group-btn">
                  <button className="btn btn-primary" type="submit">
                    <span className="fa fa-search"></span>
                  </button>
                </div>
              </div>
            </form>
          </div>
          <div className="col col-3" />

        </div>
      </div>);
  }

}
