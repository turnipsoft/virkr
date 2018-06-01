import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import CvrVisning from './cvrvisning'
import { visVirksomhed, visDeltager, visEjerGraf } from '../../actions/';

class VirksomhedView extends Component {

  componentDidMount() {
    console.log(this.props);
    if (!this.props.cvrnummer && this.props.cvrnummerParam) {
      // Så er man navigeret direkte
      this.props.visVirksomhed(this.props.cvrnummerParam, false);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.cvrdata) {
      document.title = `virkr.dk - ${nextProps.cvrdata.virksomhedMetadata.nyesteNavn.navn} - ${nextProps.cvrdata.cvrNummer}`;
    }
  }

  render() {
    const { regnskaber, cvrdata, showSpinner, visDeltager, visVirksomhed, visEjerGraf, error } = this.props;

    return (
      <div>
        <PageHeader iconClassName="fa fa-factory" headerText='Virksomhedsinformationer' cvrdata={cvrdata} context="virksomhed" />
        {showSpinner && <Spinner/>}
        {cvrdata && <CvrVisning cvrdata={cvrdata}
                                regnskaber={regnskaber}
                                visDeltager={visDeltager}
                                visVirksomhed={visVirksomhed}
                                visEjerGraf={visEjerGraf} />}
        {error && <div className="alert alert-danger alert-margin-top" role="alert">Der er opstået en fejl under hentning af virksomheden</div>}
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
    error: virksomhed.error,
    router: state.router
  }
}

const mapDispatchToProps = {
  visVirksomhed, visDeltager, visEjerGraf
}

export default connect(mapStateToProps, mapDispatchToProps)(VirksomhedView);
