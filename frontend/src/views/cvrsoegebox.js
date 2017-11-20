import React, { Component } from 'react';
//import { Redirect } from 'react-router'
import { connect } from 'react-redux'
import { soeg } from '../actions/';

class CvrSoegebox extends Component {

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
    let { soegning } = this.state;
    soegning = soegning.replace("/","{SLASH}");
    //const soegningUrl = `/soegeresultat/${soegning}`;
    this.props.dispatch(soeg(soegning));
    //this.setState({ fejl: false, soegningUrl: soegningUrl });
  }

  render() {
    /*if (this.state.soegningUrl) {
      return (
        <Redirect to={this.state.soegningUrl} />
      );
    }*/

    return (
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
    );
  }

}

const mapStateToProps = (state) => {
  return state
}

export default connect(mapStateToProps)(CvrSoegebox);
