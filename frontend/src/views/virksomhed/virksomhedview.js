import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import CvrVisning from './cvrvisning'

class VirksomhedView extends Component {
  render() {
    const { regnskaber, cvrdata, showSpinner } = this.props;

    return (
      <div>
        <PageHeader headerText='Virksomhedsinformationer' />
        {showSpinner && <Spinner/>}
        {cvrdata && <CvrVisning cvrdata={cvrdata} regnskaber={regnskaber} />}
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  const { virksomhed } = state;
  return {
    showSpinner: virksomhed.isFetching,
    regnskaber: virksomhed.regnskaber,
    cvrdata: virksomhed.cvrdata
  }
}

export default connect(mapStateToProps)(VirksomhedView);
