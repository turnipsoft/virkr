import React, { Component } from 'react';
import { connect } from 'react-redux';
import DeltagerGraf from './deltagergraf';
import PageHeader from '../../common/pageheader';
import Spinner from '../../common/spinner';
import { visVirksomhed, visDeltager, visDeltagerGraf } from '../../../actions/';

class DeltagerGrafView extends Component {

  componentWillMount() {
    const {enhedsnummer, enhedsnummerParam, visDeltagerGraf} = this.props;

    if (!enhedsnummer && enhedsnummerParam) {
      visDeltagerGraf(enhedsnummerParam, false);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.deltagerGraf) {
      document.title = `virkr.dk - ${nextProps.deltagerGraf.deltager.navn} - EjerGraf`;
    }
  }

  render() {
    console.log('DeltagerGrafView-render');
    const { showSpinner, deltagerGraf, visVirksomhed, visDeltager, error } = this.props;

    const deltager = deltagerGraf?deltagerGraf.deltager:null;

    return(
      <div>
        <PageHeader headerText="Deltager Graf" iconClassName="fa fa-sitemap" deltager={deltager} context="deltagergraf"/>
        {showSpinner && <Spinner />}
        {deltagerGraf && <DeltagerGraf deltagerGraf={deltagerGraf}
                                       visVirksomhed={visVirksomhed}
                                       visDeltager={visDeltager} />}
        {error && <div className="alert alert-danger alert-margin-top" role="alert">Der er opstået en fejl under hentning af deltagergrafen</div>}
      </div>
    );
  }

}

const mapStateToProps = (state, ownProps) => {
  console.log('DeltagerGrafView');
  const { deltagerGraf } = state;

  return {
    deltagerGraf: deltagerGraf.deltagerGraf,
    showSpinner: deltagerGraf.isFetching,
    enhedsnummer: deltagerGraf.enhedsnummer,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    router: state.router,
    error: deltagerGraf.error
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager,
  visDeltagerGraf
}

export default connect(mapStateToProps, mapDispatchToProps)(DeltagerGrafView);
