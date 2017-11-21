import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import CvrVisning from './cvrvisning'
import { visVirksomhed } from '../../actions/';

class VirksomhedView extends Component {

  componentWillMount() {
    console.log(this.state);
    console.log(this.props);
    if (this.state===null && this.props.cvrnummerParam) {
      // Så er man navigeret direkte
      this.props.dispatch(visVirksomhed(this.props.cvrnummerParam, false));
    }
  }

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

const mapStateToProps = (state, ownProps) => {
  const { virksomhed } = state;
  return {
    showSpinner: virksomhed.isFetching,
    regnskaber: virksomhed.regnskaber,
    cvrdata: virksomhed.cvrdata,
    cvrnummerParam: ownProps.match.params.cvrnummer
  }
}

export default connect(mapStateToProps)(VirksomhedView);
