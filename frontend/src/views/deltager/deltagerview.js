import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import DeltagerVisning from './deltager';
import { visDeltager, visVirksomhed, visDeltagerGraf } from '../../actions/'

class DeltagerView extends Component {

  componentWillMount() {
    const {enhedsnummer, enhedsnummerParam, visDeltager} = this.props;

    if (!enhedsnummer && enhedsnummerParam) {
      visDeltager(enhedsnummerParam, false);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.deltager) {
      document.title = `virkr.dk - ${nextProps.deltager.navn} - Deltager`;
    }
  }

  render() {
    console.log('DeltagerView-Render');
    const { showSpinner, deltager, visVirksomhed, visDeltagerGraf, error } = this.props;

    return(
      <div>
        <PageHeader iconClassName="fa fa-user" headerText="Deltager" deltager={deltager} context="deltager" />
        {showSpinner && <Spinner />}
        {deltager && <DeltagerVisning deltager={deltager} onVirksomhedClick={visVirksomhed} visDeltagerGraf={visDeltagerGraf} />}
        {error && <div className="alert alert-danger alert-margin-top" role="alert">Der er opstået en fejl under hentning af deltageren</div>}
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  console.log('DeltagerView');
  const {deltager} = state;

  return {
    showSpinner: deltager.isFetching,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    enhedsnummer: deltager.enhedsnummer,
    deltager: deltager.deltager,
    error: deltager.error
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager,
  visDeltagerGraf
}

export default connect(mapStateToProps, mapDispatchToProps)(DeltagerView);
