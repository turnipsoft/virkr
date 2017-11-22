import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import CvrVisning from './cvrvisning'
import { visVirksomhed } from '../../actions/';

class VirksomhedView extends Component {

  componentDidMount() {
    console.log(this.props);
    if (!this.props.cvrnummer && this.props.cvrnummerParam) {
      // Så er man navigeret direkte
      this.props.visVirksomhed(this.props.cvrnummerParam, false);
    }
  }

  render() {
    const { regnskaber, cvrdata, showSpinner } = this.props;

    if (this.props.hasError) {
      <div>
        <PageHeader headerText='Virksomhedsinformationer' />
        <div className="row">
          <div className="col">
            Fejl er opstået under fremfinding af virksomheden : {this.props.error}
          </div>
        </div>
      </div>
    }

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
  console.log(state);
  const { virksomhed } = state;
  return {
    showSpinner: virksomhed.isFetching,
    regnskaber: virksomhed.regnskaber,
    cvrdata: virksomhed.cvrdata,
    cvrnummer: virksomhed.cvrnummer,
    cvrnummerParam: ownProps.match.params.cvrnummer,
    hasError: virksomhed.hasError,
    error: virksomhed.error
  }
}

const mapDispatchToProps = {
  visVirksomhed
}

export default connect(mapStateToProps, mapDispatchToProps)(VirksomhedView);
