import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import CvrVisning from './cvrvisning'

class VirksomhedView extends Component {
  render() {
    const { regnskaber, cvrdata, showSpinner } = this.props;

    const header = cvrdata? cvrdata.virksomhedsnavn : 'Virksomhed';

    return (
      <div>
        <PageHeader headerText={header} />
        {showSpinner && <Spinner/>}
        {cvrdata &&<CvrVisning cvrdata={cvrdata} regnskaber={regnskaber} />}
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    showSpinner: state.isFetching,
    regnskaber: state.regnskaber,
    cvrdata: state.cvrdata
  }
}

export default connect(mapStateToProps)(VirksomhedView);
